package com.syngenta.sadie.kieserverclient.service;

import com.syngenta.dda.facts.DeriveSeason;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private List<DeriveSeason> seasons;

    public Response() {
        seasons = new ArrayList<>();
    }

    public List<DeriveSeason> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<DeriveSeason> seasons) {
        this.seasons = seasons;
    }

    public void addDerivedSeason(DeriveSeason deriveSeason){
        seasons.add(deriveSeason);
    }
}
