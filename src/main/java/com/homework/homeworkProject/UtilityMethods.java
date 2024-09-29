package com.homework.homeworkProject;

import com.homework.homeworkProject.dto.Jockey;

public class UtilityMethods {
    public static Jockey createPerson(String firstName, String lastName, String horseName, String country){
        Jockey rider = new Jockey();
        rider.setFirstName(firstName);
        rider.setLastName(lastName);
        rider.setHorseName(horseName);
        rider.setCountry(country);
        rider.setId(idGenerator());
        return rider;
    }
    public static Jockey updatePerson(Jockey jockeyToUpdate, String firstName, String lastName, String horseName, String country){
        jockeyToUpdate.setFirstName(firstName);
        jockeyToUpdate.setLastName(lastName);
        jockeyToUpdate.setHorseName(horseName);
        jockeyToUpdate.setCountry(country);
        return jockeyToUpdate;
    }
    public static long idGenerator() {
        return System.currentTimeMillis();
    }
}
