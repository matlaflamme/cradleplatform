package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
import com.cradlerest.web.model.DualMonthStats;
import com.cradlerest.web.model.Patient;
import com.cradlerest.web.model.Reading;
import com.cradlerest.web.model.Stats;
import com.cradlerest.web.model.view.ReadingView;
import com.cradlerest.web.model.view.ReferralView;
import com.cradlerest.web.service.PatientManagerService;
import com.cradlerest.web.service.ReadingManager;
import com.cradlerest.web.service.ReferralManagerService;
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
    private ReferralManagerService referralManagerService;

    public AdminStatsController(PatientManagerService patientManagerService,
                                ReadingManager readingManager,
                                ReferralManagerService referralManagerService
    ) {
        this.patientManagerService = patientManagerService;
        this.readingManager = readingManager;
        this.referralManagerService = referralManagerService;

    }

    @GetMapping("/overview")
    public DualMonthStats overview() {
        List<Reading> readings = new ArrayList<>();
        List<Reading> readingsTrend = new ArrayList<>();
        List<ReferralView> referrals = new ArrayList<>();
        List<ReferralView> referralsTrend = new ArrayList<>();

        gatherAllReferrals(referrals, referralsTrend);
        gatherAllReadings(readings, readingsTrend);

        Stats thisMonth = GenerateStats(readings, referrals);
        Stats lastMonth = GenerateStats(readingsTrend, referralsTrend);

        return new DualMonthStats(thisMonth,lastMonth);
    }

    private Stats GenerateStats(List<Reading> readings, List<ReferralView> referrals) {
        Stats stats = new Stats();
        // readings Stats
        Set<String> hashSetOfPatients = new HashSet<>();
        Set<Integer> hashSetOfVHTs = new HashSet<>();
        int numberOfReds = 0 ;
        int numberOfGreens = 0;
        int numberOfYellows = 0;
        for (Reading reading : readings) {
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
        stats.setNumberOfGreens(numberOfGreens);
        stats.setNumberOfReds(numberOfReds);
        stats.setNumberOfYellows(numberOfYellows);
        stats.setNumberOfPatientsSeen(hashSetOfPatients.size());
        stats.setNumberOfReadings(readings.size());
        stats.setNumberOfVHTs(hashSetOfVHTs.size());

        //referralStats
        stats.setNumberOfReferrals(referrals.size());
        return stats;
    }

    private void gatherAllReferrals(List<ReferralView> thisMonthReferrals, List<ReferralView> lastMonthReferrals) {
        Instant oneMonthAgo = Instant.now();
        Instant twoMonthsAgo = Instant.now();
        oneMonthAgo = oneMonthAgo.minus(STATISTICAL_TIME_PERIOD_IN_DAYS, ChronoUnit.DAYS);
        twoMonthsAgo = twoMonthsAgo.minus(STATISTICAL_TIME_PERIOD_IN_DAYS * 2, ChronoUnit.DAYS);
        List<ReferralView> allReferrals = referralManagerService.findAllByOrderByTimestampDesc();

        for (ReferralView referral : allReferrals) {
            Instant referralDate = referral.getTimestamp().toInstant();
            if(referralDate.isAfter(oneMonthAgo)){
                thisMonthReferrals.add(referral);
            }else if (referralDate.isAfter(twoMonthsAgo)){
                lastMonthReferrals.add(referral);
            }
        }
    }

    private void gatherAllReadings(
            List<Reading> thisMonthReadings,
            List<Reading> lastMonthReadings
    ) {
        List<Patient> allPatients = patientManagerService.getAllPatients();

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

