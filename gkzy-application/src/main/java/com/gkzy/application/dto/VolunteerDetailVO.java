package com.gkzy.application.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 志愿明细VO — 包含院校名称、层次、省份等展示信息
 */
@Data
public class VolunteerDetailVO {
    private Long id;
    private Long planId;
    private Integer sortOrder;
    private Long collegeId;
    private String strategy;
    private BigDecimal admitProbability;

    // === 院校信息（关联查询） ===
    private String collegeName;   // 院校名称
    private String collegeCode;   // 院校代码
    private String collegeLevel;  // 层次: 985/211/双一流/普通
    private String collegeProvince; // 省份
    private String collegeCity;   // 城市
    private String collegeType;   // 类型: 综合/理工/师范/...

    // === 专业信息（关联查询，最多6个） ===
    private String major1Name;
    private String major2Name;
    private String major3Name;
    private String major4Name;
    private String major5Name;
    private String major6Name;
}
