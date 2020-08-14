package com.example.lol_history.model;

import com.google.gson.annotations.SerializedName;

public class SummonerIDinfo {
    @SerializedName("id")
    private String id;

    @SerializedName("accountId")
    private String accuontId;

    @SerializedName("name")
    private String name;

    @SerializedName("summonerLevel")
    private int summonerLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccuontId() {
        return accuontId;
    }

    public void setAccuontId(String accuontId) {
        this.accuontId = accuontId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
