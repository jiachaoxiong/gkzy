package com.gkzy.college.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gkzy.college.entity.AdmissionScore;
import com.gkzy.college.entity.College;
import com.gkzy.college.entity.CollegeMajor;
import com.gkzy.college.service.CollegeService;
import com.gkzy.common.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;

    @GetMapping("/provinces")
    public R<List<String>> provinces() {
        return R.ok(collegeService.getProvinces());
    }

    @GetMapping("/colleges")
    public R<Page<College>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String province) {
        return R.ok(collegeService.page(page, size, keyword, type, level, province));
    }

    @GetMapping("/colleges/{id}")
    public R<College> detail(@PathVariable Long id) {
        return R.ok(collegeService.getById(id));
    }

    @GetMapping("/colleges/{id}/majors")
    public R<List<CollegeMajor>> majors(@PathVariable Long id) {
        return R.ok(collegeService.getMajors(id));
    }

    @GetMapping("/colleges/{id}/scores")
    public R<List<AdmissionScore>> scores(
            @PathVariable Long id,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String subjectType) {
        return R.ok(collegeService.getScores(id, batch, subjectType));
    }

    @GetMapping("/majors")
    public R<Page<CollegeMajor>> searchMajors(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword) {
        return R.ok(collegeService.searchMajors(page, size, keyword));
    }
}
