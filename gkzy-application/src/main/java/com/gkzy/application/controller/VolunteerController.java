package com.gkzy.application.controller;

import com.gkzy.application.dto.VolunteerDetailVO;
import com.gkzy.application.entity.VolunteerDetail;
import com.gkzy.application.entity.VolunteerPlan;
import com.gkzy.application.service.VolunteerService;
import com.gkzy.common.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class VolunteerController {

    private final VolunteerService volunteerService;

    @GetMapping
    public R<List<VolunteerPlan>> list(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return R.ok(volunteerService.listPlans(userId));
    }

    @PostMapping
    public R<VolunteerPlan> create(@RequestBody VolunteerPlan plan, Authentication auth) {
        plan.setUserId((Long) auth.getPrincipal());
        return R.ok(volunteerService.createPlan(plan));
    }

    @GetMapping("/{id}")
    public R<VolunteerPlan> detail(@PathVariable Long id) {
        return R.ok(volunteerService.getPlan(id));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody VolunteerPlan plan) {
        plan.setId(id);
        volunteerService.updatePlan(plan);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        volunteerService.deletePlan(id);
        return R.ok();
    }

    @GetMapping("/{id}/details")
    public R<List<VolunteerDetailVO>> details(@PathVariable Long id) {
        return R.ok(volunteerService.getDetails(id));
    }

    @PostMapping("/{id}/details")
    public R<Void> addDetail(@PathVariable Long id, @RequestBody VolunteerDetail detail) {
        volunteerService.addDetail(id, detail);
        return R.ok();
    }

    @PutMapping("/{id}/details/{did}")
    public R<Void> updateDetail(@PathVariable Long did, @RequestBody VolunteerDetail detail) {
        volunteerService.updateDetail(did, detail);
        return R.ok();
    }

    @DeleteMapping("/{id}/details/{did}")
    public R<Void> deleteDetail(@PathVariable Long did) {
        volunteerService.deleteDetail(did);
        return R.ok();
    }

    @PostMapping("/{id}/submit")
    public R<Void> submit(@PathVariable Long id) {
        volunteerService.submitPlan(id);
        return R.ok();
    }
}
