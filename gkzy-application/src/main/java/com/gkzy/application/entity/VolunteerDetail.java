package com.gkzy.application.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gkzy.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("volunteer_detail")
public class VolunteerDetail extends BaseEntity {
    private Long planId;
    private Integer sortOrder;
    private Long collegeId;
    private Long major1;
    private Long major2;
    private Long major3;
    private Long major4;
    private Long major5;
    private Long major6;
    private String strategy;
    private BigDecimal admitProbability;
    private Integer isObeyAdjust;
}
