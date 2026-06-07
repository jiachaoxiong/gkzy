package com.gkzy.college.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gkzy.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("college")
public class College extends BaseEntity {
    private String name;
    private String code;
    private String type;
    private String province;
    private String city;
    private String level;
    private String website;
    private String logo;
}
