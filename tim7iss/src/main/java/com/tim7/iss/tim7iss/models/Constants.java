package com.tim7.iss.tim7iss.models;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static String stringDateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    public static DateTimeFormatter customDateTimeFormat = DateTimeFormatter.ofPattern(stringDateTimeFormat);;
}
