package com.gkzy.recommend.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RecommendCollegeVO {
    private Long collegeId;
    private String collegeName;
    private String collegeType;
    private String city;
    private Integer refMinScore;
    private Integer refMinRank;
    private BigDecimal admitProbability;
    private String strategy; // 冲/稳/保
    private String batch;    // 参考批次
    private String level;    // 层次: 985/211/双一流/普通
    private String code;     // 院校代码
    private String province; // 省份

    /**
     * 内部临时字段（不序列化到JSON）：偏差率，用于策略分类排序
     */
    @com.fasterxml.jackson.annotation.JsonIgnore
    private transient double diffTemp;
}
