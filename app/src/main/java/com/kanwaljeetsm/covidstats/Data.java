package com.kanwaljeetsm.covidstats;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private String activeCases;
    private String totalCases;
    private String recovered;
    private String deaths;
    private String nActiveCases;
    private String nRecovered;
    private String nDeaths;
    private List<String> region = new ArrayList<>();
    private List<String> totalInfected = new ArrayList<>();
    private List<String> stateRecovered = new ArrayList<>();
    private String dateTimeUpdate;

    public Data() {}

    public String getDateTimeUpdate() {
        return dateTimeUpdate;
    }

    public void setDateTimeUpdate(String dateTimeUpdate) {
        this.dateTimeUpdate = dateTimeUpdate;
    }

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

    public List<String> getStateRecovered() {
        return stateRecovered;
    }

    public void setStateRecovered(List<String> stateRecovered) {
        this.stateRecovered = stateRecovered;
    }

    public String getnActiveCases() {
        return nActiveCases;
    }

    public void setnActiveCases(String nActiveCases) {
        this.nActiveCases = nActiveCases;
    }

    public String getnRecovered() {
        return nRecovered;
    }

    public void setnRecovered(String nRecovered) {
        this.nRecovered = nRecovered;
    }

    public String getnDeaths() {
        return nDeaths;
    }

    public void setnDeaths(String nDeaths) {
        this.nDeaths = nDeaths;
    }
}