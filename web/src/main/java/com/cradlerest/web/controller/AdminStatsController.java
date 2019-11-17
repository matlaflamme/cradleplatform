package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.EntityNotFoundException;
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
    public Stats overview() {
        List<Patient> allPatients = patientManagerService.getAllPatients();

        List<Reading> readings = new ArrayList<>();
        List<Reading> readingsTrend = new ArrayList<>();
        List<ReferralView> thisMonthReferrals = new ArrayList<ReferralView>();
        List<ReferralView> lastMonthReferrals = new ArrayList<ReferralView>();
        Stats thisMonth = new Stats();
        Stats lastMonth = new Stats();


        int numberOfReferrals = 0;



        gatherAllReferrals(thisMonthReferrals, lastMonthReferrals);

        gatherAllReadings(allPatients, readings, readingsTrend);

        thisMonth = GenerateStats(readings);
        lastMonth = GenerateStats(readingsTrend);

        return new Stats();
    }

    private void gatherAllReferrals(List<ReferralView> thisMonthReferrals, List<ReferralView> lastMonthReferrals) {
        Instant oneMonthAgo = Instant.now();
        Instant twoMonthsAgo = Instant.now();
        oneMonthAgo = oneMonthAgo.minus(STATISTICAL_TIME_PERIOD_IN_DAYS, ChronoUnit.DAYS);
        twoMonthsAgo = twoMonthsAgo.minus(STATISTICAL_TIME_PERIOD_IN_DAYS * 2, ChronoUnit.DAYS);
        List<ReferralView> allReferrals = referralManagerService.findAllByOrderByTimestampDesc();
        for (ReferralView referral : allReferrals) {

        }

        int i = thisMonthReferrals.size();
        i = lastMonthReferrals.size();
    }

    private Stats GenerateStats(List<Reading> readings) {
        Stats stats = new Stats();
        Set<String> hashSetOfPatients = new HashSet<String>();
        Set<Integer> hashSetOfVHTs = new HashSet<Integer>();
        int numberOfReadings = 0;
        int numberOfReds = 0 ;
        int numberOfGreens = 0;
        int numberOfYellows = 0;
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
        stats.setNumberOfGreens(numberOfGreens);
        stats.setNumberOfPatientsSeen(hashSetOfPatients.size());
        stats.setNumberOfReadings(numberOfReadings);
        stats.setNumberOfReds(numberOfReds);
        stats.setNumberOfYellows(numberOfYellows);
        stats.setNumberOfVHTs(hashSetOfVHTs.size());

        return stats;
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

