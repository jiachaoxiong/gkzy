package com.gkzy.recommend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gkzy.college.entity.AdmissionScore;
import com.gkzy.college.entity.College;
import com.gkzy.college.mapper.AdmissionScoreMapper;
import com.gkzy.college.mapper.CollegeMapper;
import com.gkzy.recommend.dto.AnalyzeDTO;
import com.gkzy.recommend.dto.RecommendCollegeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final AdmissionScoreMapper scoreMapper;
    private final CollegeMapper collegeMapper;

    // ====== 2025年山西省一分一段表 (分数→累计位次) ======
    // 数据来源: 山西省招生考试管理中心官方公布
    private static final TreeMap<Integer, Integer> RANK_2025_LIGONG = new TreeMap<>();
    private static final TreeMap<Integer, Integer> RANK_2025_WENSHI = new TreeMap<>();

    static {
        // 2025年 物理类(理工) - 数据来源: 山西省招生考试管理中心 2025-06-25公布
        // 使用逐10分关键点 + 线性插值覆盖所有分数
        int[][] ligong = {
            {690,84},{680,230},{670,478},{660,942},{650,1691},{640,2722},{630,3984},{620,5728},{610,7886},{600,10452},
            {590,13425},{580,16930},{570,20780},{560,25129},{550,29847},{540,35110},{530,40723},{520,46928},{510,53491},{500,60425},
            {490,67479},{480,74557},{470,81895},{460,89298},{450,96566},{440,103827},{430,111144},{420,118413},{410,125763},{400,133192},
            {390,140702},{380,148293},{370,155950},{360,163659},{350,171420},{340,179230},{330,187090},{320,195000},{310,202960},{300,210970}
        };
        for (int[] pair : ligong) RANK_2025_LIGONG.put(pair[0], pair[1]);

        // 2025年 历史类(文史) - 数据来源: 山西省招生考试管理中心 2025-06-25公布
        // 使用逐10分关键点 + 线性插值覆盖所有分数
        int[][] wenshi = {
            {671,11},{670,13},{660,42},{650,110},{640,256},{630,448},{620,807},{610,1270},{600,1918},
            {590,2792},{580,3874},{570,5154},{560,6593},{550,8406},{540,10416},{530,12559},{520,15009},{510,17574},{500,20270},
            {490,23133},{480,26003},{470,28696},{460,31489},{450,34070},{443,35877},
            {440,36588},{430,39307},{420,42032},{410,44763},{400,47500},{390,50243},{380,52992},{370,55747},{360,58508},{350,61275},
            {340,64048},{330,66827},{320,69612},{310,72403},{300,75200}
        };
        for (int[] pair : wenshi) RANK_2025_WENSHI.put(pair[0], pair[1]);
    }

    /**
     * 根据分数和科类查一分一段表，返回全省位次
     */
    private Integer scoreToRank(Integer score, String subjectType) {
        TreeMap<Integer, Integer> map = "文史".equals(subjectType) ? RANK_2025_WENSHI : RANK_2025_LIGONG;
        if (map.isEmpty()) return null;

        // 精确命中
        if (map.containsKey(score)) return map.get(score);

        // 线性插值：用最近两个已知数据点
        Integer lowerKey = map.lowerKey(score);   // 恰好低于输入分数的最高分
        Integer higherKey = map.higherKey(score);  // 恰好高于输入分数的最低分

        if (lowerKey != null && higherKey != null) {
            // 在两点之间线性插值
            double ratio = (double)(score - lowerKey) / (higherKey - lowerKey);
            int lowerRank = map.get(lowerKey);
            int higherRank = map.get(higherKey);
            return (int) Math.round(lowerRank + (higherRank - lowerRank) * ratio);
        } else if (lowerKey != null) {
            // 超出已知最低分，用最近数据
            return map.get(lowerKey);
        } else if (higherKey != null) {
            // 超出已知最高分，用最近数据
            return map.get(higherKey);
        }
        return null;
    }

    public List<RecommendCollegeVO> analyze(AnalyzeDTO dto) {
        // ====== 步骤0: 分数→位次自动换算 ======
        int studentRank = dto.getRank();
        Integer convertedRank = scoreToRank(dto.getScore(), dto.getSubjectType());
        if (convertedRank != null) {
            // 使用一分一段表换算的位次（比用户手动输入更准确）
            studentRank = convertedRank;
        }

        // ====== 步骤1: 查询近3年录取数据 ======
        // 兼容2025年山西高考改革：旧批次名(本科一批A) + 新批次名(本科批) 都要查
        int currentYear = 2026;
        List<String> batches = new ArrayList<>();
        batches.add(dto.getBatch());
        // 如果选了旧批次，也包含"本科批"（2025年新高考合并批次后的名称）
        if (dto.getBatch().startsWith("本科") && !"本科批".equals(dto.getBatch())) {
            batches.add("本科批");
        }

        List<AdmissionScore> scores = scoreMapper.selectList(
                new LambdaQueryWrapper<AdmissionScore>()
                        .in(AdmissionScore::getBatch, batches)
                        .eq(AdmissionScore::getSubjectType, dto.getSubjectType())
                        .isNull(AdmissionScore::getMajorId) // 只取院校投档线
                        .ge(AdmissionScore::getYear, currentYear - 3));

        // ====== 步骤2: 按院校聚合，计算加权平均参考位次 ======
        Map<Long, List<AdmissionScore>> collegeScoreMap = scores.stream()
                .collect(Collectors.groupingBy(AdmissionScore::getCollegeId));

        List<RecommendCollegeVO> results = new ArrayList<>();

        for (Map.Entry<Long, List<AdmissionScore>> entry : collegeScoreMap.entrySet()) {
            Long collegeId = entry.getKey();
            List<AdmissionScore> scoreList = entry.getValue();

            // 加权平均：最近年份权重更高 (2023:1, 2024:2, 2025:3)
            double weightedSum = 0;
            double weightTotal = 0;
            for (AdmissionScore s : scoreList) {
                double weight = 1.0 + (s.getYear() - (currentYear - 3));
                weightedSum += s.getMinRank() * weight;
                weightTotal += weight;
            }
            int refRank = (int) (weightedSum / weightTotal);

            // ====== 步骤3: 计算偏差率 ======
            // diff > 0 → 考生位次优于参考位次（录取希望大）
            // diff < 0 → 考生位次劣于参考位次（录取希望小）
            double diff = ((double) refRank - studentRank) / refRank;

            // ====== 步骤4: 平滑Sigmoid概率公式 ======
            // prob = 100 / (1 + e^(-k * (diff + offset)))
            // k=6(陡峭度), offset=0.05(使diff=0时概率≈57%)
            double sigmoid = 100.0 / (1.0 + Math.exp(-6.0 * (diff + 0.05)));
            BigDecimal probability = BigDecimal.valueOf(sigmoid)
                    .setScale(0, RoundingMode.HALF_UP);

            // ====== 步骤5: 策略分类 ======
            // 冲: 考生位次明显差于参考 → diff < -0.10
            // 稳: 考生位次接近参考     → -0.10 ≤ diff ≤ 0.10
            // 保: 考生位次明显优于参考 → diff > 0.10
            String strategy;
            if (diff < -0.10) {
                strategy = "冲";
                if (probability.compareTo(BigDecimal.valueOf(5)) < 0) {
                    probability = BigDecimal.valueOf(5.0); // 最低不低于5%
                }
            } else if (diff <= 0.10) {
                strategy = "稳";
            } else {
                strategy = "保";
                if (probability.compareTo(BigDecimal.valueOf(99)) > 0) {
                    probability = BigDecimal.valueOf(99.0); // 最高不超过99%
                }
            }

            College college = collegeMapper.selectById(collegeId);
            if (college == null) continue;

            RecommendCollegeVO vo = new RecommendCollegeVO();
            vo.setCollegeId(collegeId);
            vo.setCollegeName(college.getName());
            vo.setCollegeType(college.getType());
            vo.setCity(college.getCity());
            vo.setRefMinScore(scoreList.get(0).getMinScore());
            vo.setRefMinRank(refRank);
            vo.setAdmitProbability(probability);
            vo.setStrategy(strategy);
            vo.setBatch(scoreList.get(0).getBatch());
            vo.setLevel(college.getLevel());
            vo.setCode(college.getCode());
            vo.setProvince(college.getProvince());
            results.add(vo);
        }

        // ====== 步骤6: 排序 ======
        // 冲→稳→保 → 层次(985→211→双一流→普通) → 院校代码升序
        Map<String, Integer> strategyOrder = Map.of("冲", 0, "稳", 1, "保", 2);
        Map<String, Integer> levelOrder = Map.of("985", 0, "211", 1, "双一流", 2, "普通", 3);
        results.sort(Comparator
                .comparingInt((RecommendCollegeVO r) -> strategyOrder.getOrDefault(r.getStrategy(), 9))
                .thenComparingInt(r -> levelOrder.getOrDefault(r.getLevel(), 9))
                .thenComparing(RecommendCollegeVO::getCode, Comparator.nullsLast(String::compareTo)));

        return results;
    }
}
