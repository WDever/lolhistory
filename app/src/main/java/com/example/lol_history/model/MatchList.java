package com.example.lol_history.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MatchList {
    @SerializedName("matches")
    private List<Match> matches = new ArrayList<>();

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public static class Match {
        @SerializedName("gameId")
        private String gameId;

        @SerializedName("champion")
        private String champion;


        @SerializedName("queue")
        private String queue;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getChampion() {
            return champion;
        }

        public void setChampion(String champion) {
            this.champion = champion;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }

    }
}
