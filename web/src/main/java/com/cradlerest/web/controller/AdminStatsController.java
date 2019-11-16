package com.cradlerest.web.controller;

import com.cradlerest.web.model.PatientWithLatestReadingView;
import com.cradlerest.web.model.Stats;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for querying information about general statistics
 */
@RestController
@RequestMapping("/api/stats")
public class AdminStatsController {
    private PatientManagerService patientManagerService;
    private ReadingManager readingManager;

    public AdminStatsController(PatientManagerService patientManagerService,
                                ReadingManager readingManager
    ) {
        this.patientManagerService = patientManagerService;
        this.readingManager = readingManager;

    }

    @GetMapping("/overview")
    public Stats overview() {

        return new Stats();
    }
}

