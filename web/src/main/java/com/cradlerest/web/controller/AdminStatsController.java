package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.Stats;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Controller for querying information about general statistics
 */
@RestController
@RequestMapping("/api/stats")
public class AdminStatsController {
    private static final long STATISTICAL_TIME_PERIOD_IN_DAYS = 30;
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
        List<Patient> allPatients = patientManagerService.getAllPatients();
        List<Reading> readings = new ArrayList<>();
        List<Reading> readingsTrend = new ArrayList<>();



        int numberOfReadings = 0;
        int numberOfReds = 0 ;
        int numberOfGreens = 0;
        int numberOfYellows = 0;
        int numberOfReferrals = 0;


        gatherAllReadings(allPatients, readings, readingsTrend);

        Set<String> hashSetOfPatients = new HashSet<String>();
        Set<Integer> hashSetOfVHTs = new HashSet<Integer>();

        for (Reading reading : readings) {
            // gather total readings
            numberOfReadings++;

            // gather ReadingColours
            if(reading.getColour().isGreen()){
                numberOfGreens++;
            }
            if(reading.getColour().isRed()){
                numberOfReds++;
            }
            if(reading.getColour().isYellow()){
                numberOfYellows++;
            }

            //gather VHT and Patient data
            hashSetOfVHTs.add(reading.getCreatedBy());
            hashSetOfPatients.add(reading.getPatientId());

        }

        return new Stats(
                numberOfReadings,
                numberOfReds,
                numberOfGreens,
                numberOfYellows,
                hashSetOfPatients.size(),
                hashSetOfVHTs.size(),
                numberOfReferrals
        );
    }

    private void gatherAllReadings(
            List<Patient> allPatients,
            List<Reading> thisMonthReadings,
            List<Reading> lastMonthReadings
    ) {
        Instant oneMonthAgo = Instant.now();
        Instant twoMonthsAgo = Instant.now();
        oneMonthAgo = oneMonthAgo.minus(STATISTICAL_TIME_PERIOD_IN_DAYS, ChronoUnit.DAYS);
        twoMonthsAgo = twoMonthsAgo.minus(STATISTICAL_TIME_PERIOD_IN_DAYS * 2, ChronoUnit.DAYS);

        // gathersAllReadings
        for (Patient patient : allPatients) {
            try {
                List<ReadingView> allReadings = readingManager.getAllReadingViewsForPatient(patient.getId());
                for( ReadingView reading : allReadings){
                    Instant readingDate = reading.getTimestamp().toInstant();
                    if(readingDate.isAfter(oneMonthAgo)){
                        thisMonthReadings.add(reading);
                    }else if(readingDate.isAfter(twoMonthsAgo)){
                        lastMonthReadings.add(reading);
                    }
                }
            } catch (EntityNotFoundException e) { // should never happen since patients are bing read from Database first
                e.printStackTrace();
            }
        }
    }
}

