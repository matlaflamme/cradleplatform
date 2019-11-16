package com.cradlerest.web.controller;

import com.cradlerest.web.model.PatientWithLatestReadingView;
import com.cradlerest.web.model.Stats;
import com.cradlerest.web.service.PatientManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for querying information about general statistics
 */
@RestController
@RequestMapping("/api/stats")
public class AdminStatsController {

    public AdminStatsController(PatientManagerService patientManagerService) {

    }

    @GetMapping("/overview")
    public Stats overview() {
        return new Stats();
    }
}

