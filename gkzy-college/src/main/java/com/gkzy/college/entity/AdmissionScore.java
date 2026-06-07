package com.gkzy.college.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admission_score")
public class AdmissionScore {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long collegeId;
    private Long majorId;
    private Integer year;
    private String batch;
    private String subjectType;
    private Integer minScore;
    private Integer minRank;
    private Integer avgScore;
    private Integer maxScore;
    private Integer enrollCount;
    private LocalDateTime createTime;
}
