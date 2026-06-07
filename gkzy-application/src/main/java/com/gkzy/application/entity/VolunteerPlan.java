package com.gkzy.application.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gkzy.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("volunteer_plan")
public class VolunteerPlan extends BaseEntity {
    private Long userId;
    private String name;
    private String batch;
    private String subjectType;
    private Integer totalScore;
    private Integer year;
    private String status; // 草稿/已提交
}
