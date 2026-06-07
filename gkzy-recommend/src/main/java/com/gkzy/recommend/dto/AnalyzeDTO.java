package com.gkzy.recommend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnalyzeDTO {
    @NotNull(message = "高考分数不能为空")
    private Integer score;
    @NotNull(message = "全省位次不能为空")
    private Integer rank;
    @NotBlank(message = "科类不能为空")
    private String subjectType;
    @NotBlank(message = "批次不能为空")
    private String batch;
}
