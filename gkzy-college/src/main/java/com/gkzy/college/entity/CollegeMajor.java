package com.gkzy.college.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gkzy.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("college_major")
public class CollegeMajor extends BaseEntity {
    private Long collegeId;
    private String name;
    private String code;
    private String category;
    private String level;
    private String features;
}
