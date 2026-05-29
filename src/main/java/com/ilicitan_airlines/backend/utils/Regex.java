package com.ilicitan_airlines.backend.utils;

public class Regex {
    private Regex() {}

    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$";
    public static final String FLIGHT_IATA = "^IL-[1-9]\\d{2}$";
    public static final String REGISTRATION = "^[A-Z0-9]{2}-[A-Z0-9]{3}$";
    public static final String SEAT = "^[A-Z]\\d{1,3}$";
    public static final String PHONE_PREFIX = "^\\+\\d{1,4}$";
    public static final String PHONE_NUMBER = "^\\d{6,15}$";
    public static final String AIRPORT_IATA = "^[A-Z]{3}$";
    public static final String BOARDING_GATE = "^[A-Z]{1,2}\\d{1,3}$";
}