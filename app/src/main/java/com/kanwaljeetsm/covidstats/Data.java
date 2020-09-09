package com.kanwaljeetsm.covidstats;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private String activeCases;
    private String totalCases;
    private String recovered;
    private String deaths;
    private List<String> region = new ArrayList<>();
    private List<String> totalInfected = new ArrayList<>();

    public Data() {}

    public String getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(String activeCases) {
        this.activeCases = activeCases;
    }

    public String getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(String totalCases) {
        this.totalCases = totalCases;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public List<String> getRegion() {
        return region;
    }

    public void setRegion(List<String> region) {
        this.region = region;
    }

    public List<String> getTotalInfected() {
        return totalInfected;
    }

    public void setTotalInfected(List<String> totalInfected) {
        this.totalInfected = totalInfected;
    }
}