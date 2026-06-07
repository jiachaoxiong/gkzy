package com.gkzy.college.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gkzy.college.entity.AdmissionScore;
import com.gkzy.college.entity.College;
import com.gkzy.college.entity.CollegeMajor;
import com.gkzy.college.mapper.AdmissionScoreMapper;
import com.gkzy.college.mapper.CollegeMajorMapper;
import com.gkzy.college.mapper.CollegeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeMapper collegeMapper;
    private final CollegeMajorMapper majorMapper;
    private final AdmissionScoreMapper scoreMapper;

    public List<String> getProvinces() {
        return collegeMapper.selectProvinces();
    }

    public Page<College> page(Integer page, Integer size, String keyword, String type, String level, String province) {
        LambdaQueryWrapper<College> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(College::getName, keyword);
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(College::getType, type);
        }
        if (StringUtils.hasText(level)) {
            wrapper.eq(College::getLevel, level);
        }
        if (StringUtils.hasText(province)) {
            wrapper.eq(College::getProvince, province);
        }
        // 层次排序: 985(1) → 211(2) → 双一流(3) → 普通(4)，同层次按院校代码升序
        wrapper.last("ORDER BY CASE level WHEN '985' THEN 1 WHEN '211' THEN 2 WHEN '双一流' THEN 3 ELSE 4 END, code ASC");
        return collegeMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public College getById(Long id) {
        return collegeMapper.selectById(id);
    }

    public List<CollegeMajor> getMajors(Long collegeId) {
        return majorMapper.selectList(
                new LambdaQueryWrapper<CollegeMajor>().eq(CollegeMajor::getCollegeId, collegeId));
    }

    public List<AdmissionScore> getScores(Long collegeId, String batch, String subjectType) {
        LambdaQueryWrapper<AdmissionScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdmissionScore::getCollegeId, collegeId);
        if (StringUtils.hasText(batch)) {
            wrapper.eq(AdmissionScore::getBatch, batch);
        }
        if (StringUtils.hasText(subjectType)) {
            wrapper.eq(AdmissionScore::getSubjectType, subjectType);
        }
        wrapper.orderByDesc(AdmissionScore::getYear);
        return scoreMapper.selectList(wrapper);
    }

    public Page<CollegeMajor> searchMajors(Integer page, Integer size, String keyword) {
        LambdaQueryWrapper<CollegeMajor> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(CollegeMajor::getName, keyword);
        }
        return majorMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
