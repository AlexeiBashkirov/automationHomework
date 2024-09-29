package com.homework.homeworkProject.dto;

import java.util.ArrayList;
import java.util.List;

public class HorseRiders {
    public List<Jockey> getHorseRiders() {
        return horseRiders;
    }

    public void setHorseRiders(List<Jockey> horseRiders) {
        this.horseRiders = horseRiders;
    }

    private List<Jockey> horseRiders = new ArrayList<>();
}
