package com.cradlerest.web.model;

public class DualMonthStats {
    public DualMonthStats(Stats stats, Stats statsTrend) {
        this.stats = stats;
        this.statsTrend = statsTrend;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public Stats getStatsTrend() {
        return statsTrend;
    }

    public void setStatsTrend(Stats statsTrend) {
        this.statsTrend = statsTrend;
    }

    private Stats stats;
    private Stats statsTrend;


}
