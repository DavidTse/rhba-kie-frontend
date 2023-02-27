package com.syngenta.sadie.kieserverclient.service;

import com.syngenta.dda.facts.DeriveSeason;

import java.util.ArrayList;
import java.util.List;

public class Payload {

    private List<DeriveSeason> deriveSeasons;

    public Payload() {
        deriveSeasons = new ArrayList<>();
    }

    public List<DeriveSeason> getDeriveSeasons() {
        return deriveSeasons;
    }

    public void setDeriveSeasons(List<DeriveSeason> deriveSeasons) {
        this.deriveSeasons = deriveSeasons;
    }

    public void addDerivedSeason(DeriveSeason deriveSeason){
        deriveSeasons.add(deriveSeason);
    }
}
