package com.cradlerest.web.model;

public class DualMonthStats {
    public DualMonthStats(Stat stat, Stat statTrend) {
        this.stat = stat;
        this.statTrend = statTrend;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public Stat getStatTrend() {
        return statTrend;
    }

    public void setStatTrend(Stat statTrend) {
        this.statTrend = statTrend;
    }

    private Stat stat;
    private Stat statTrend;


}
