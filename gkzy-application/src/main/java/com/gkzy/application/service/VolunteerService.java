package com.gkzy.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gkzy.application.dto.VolunteerDetailVO;
import com.gkzy.application.entity.VolunteerDetail;
import com.gkzy.application.entity.VolunteerPlan;
import com.gkzy.application.mapper.VolunteerDetailMapper;
import com.gkzy.application.mapper.VolunteerPlanMapper;
import com.gkzy.college.entity.College;
import com.gkzy.college.entity.CollegeMajor;
import com.gkzy.college.mapper.CollegeMajorMapper;
import com.gkzy.college.mapper.CollegeMapper;
import com.gkzy.common.BusinessException;
import com.gkzy.common.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerPlanMapper planMapper;
    private final VolunteerDetailMapper detailMapper;
    private final CollegeMapper collegeMapper;
    private final CollegeMajorMapper collegeMajorMapper;

    public List<VolunteerPlan> listPlans(Long userId) {
        return planMapper.selectList(
                new LambdaQueryWrapper<VolunteerPlan>()
                        .eq(VolunteerPlan::getUserId, userId)
                        .orderByDesc(VolunteerPlan::getCreateTime));
    }

    public VolunteerPlan getPlan(Long id) {
        return planMapper.selectById(id);
    }

    public VolunteerPlan createPlan(VolunteerPlan plan) {
        plan.setStatus("草稿");
        planMapper.insert(plan);
        return plan;
    }

    public void updatePlan(VolunteerPlan plan) {
        planMapper.updateById(plan);
    }

    @Transactional
    public void deletePlan(Long id) {
        detailMapper.delete(new LambdaQueryWrapper<VolunteerDetail>().eq(VolunteerDetail::getPlanId, id));
        planMapper.deleteById(id);
    }

    public List<VolunteerDetailVO> getDetails(Long planId) {
        List<VolunteerDetail> details = detailMapper.selectList(
                new LambdaQueryWrapper<VolunteerDetail>()
                        .eq(VolunteerDetail::getPlanId, planId)
                        .orderByAsc(VolunteerDetail::getSortOrder));

        // 批量查院校信息，组装VO
        List<Long> collegeIds = details.stream()
                .map(VolunteerDetail::getCollegeId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, College> collegeMap;
        if (collegeIds.isEmpty()) {
            collegeMap = Map.of();
        } else {
            collegeMap = collegeMapper.selectBatchIds(collegeIds).stream()
                    .collect(Collectors.toMap(College::getId, c -> c));
        }

        // 批量查专业信息
        List<Long> majorIds = new ArrayList<>();
        for (VolunteerDetail d : details) {
            addIfNotNull(majorIds, d.getMajor1());
            addIfNotNull(majorIds, d.getMajor2());
            addIfNotNull(majorIds, d.getMajor3());
            addIfNotNull(majorIds, d.getMajor4());
            addIfNotNull(majorIds, d.getMajor5());
            addIfNotNull(majorIds, d.getMajor6());
        }
        Map<Long, String> majorNameMap;
        if (majorIds.isEmpty()) {
            majorNameMap = Map.of();
        } else {
            majorNameMap = collegeMajorMapper.selectBatchIds(majorIds).stream()
                    .collect(Collectors.toMap(CollegeMajor::getId, CollegeMajor::getName));
        }

        return details.stream().map(d -> {
            VolunteerDetailVO vo = new VolunteerDetailVO();
            vo.setId(d.getId());
            vo.setPlanId(d.getPlanId());
            vo.setSortOrder(d.getSortOrder());
            vo.setCollegeId(d.getCollegeId());
            vo.setStrategy(d.getStrategy());
            vo.setAdmitProbability(d.getAdmitProbability());

            College c = collegeMap.get(d.getCollegeId());
            if (c != null) {
                vo.setCollegeName(c.getName());
                vo.setCollegeCode(c.getCode());
                vo.setCollegeLevel(c.getLevel());
                vo.setCollegeProvince(c.getProvince());
                vo.setCollegeCity(c.getCity());
                vo.setCollegeType(c.getType());
            }

            vo.setMajor1Name(majorNameMap.get(d.getMajor1()));
            vo.setMajor2Name(majorNameMap.get(d.getMajor2()));
            vo.setMajor3Name(majorNameMap.get(d.getMajor3()));
            vo.setMajor4Name(majorNameMap.get(d.getMajor4()));
            vo.setMajor5Name(majorNameMap.get(d.getMajor5()));
            vo.setMajor6Name(majorNameMap.get(d.getMajor6()));
            return vo;
        }).collect(Collectors.toList());
    }

    public void addDetail(Long planId, VolunteerDetail detail) {
        // 检查志愿数量
        long count = detailMapper.selectCount(
                new LambdaQueryWrapper<VolunteerDetail>().eq(VolunteerDetail::getPlanId, planId));
        if (count >= 8) {
            throw new BusinessException(ResultCode.PLAN_FULL);
        }
        detail.setPlanId(planId);
        detail.setSortOrder((int) count + 1);
        detailMapper.insert(detail);
    }

    public void updateDetail(Long detailId, VolunteerDetail detail) {
        detail.setId(detailId);
        detailMapper.updateById(detail);
    }

    public void deleteDetail(Long detailId) {
        detailMapper.deleteById(detailId);
    }

    public void submitPlan(Long planId) {
        VolunteerPlan plan = planMapper.selectById(planId);
        plan.setStatus("已提交");
        planMapper.updateById(plan);
    }

    private static void addIfNotNull(List<Long> list, Long value) {
        if (value != null) {
            list.add(value);
        }
    }
}
