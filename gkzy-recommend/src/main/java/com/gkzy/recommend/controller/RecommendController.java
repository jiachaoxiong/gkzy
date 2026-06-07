package com.gkzy.recommend.controller;

import com.gkzy.common.R;
import com.gkzy.recommend.dto.AnalyzeDTO;
import com.gkzy.recommend.dto.RecommendCollegeVO;
import com.gkzy.recommend.service.RecommendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/analyze")
    public R<List<RecommendCollegeVO>> analyze(@Valid @RequestBody AnalyzeDTO dto) {
        return R.ok(recommendService.analyze(dto));
    }
}
