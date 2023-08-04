package io.terence.recipesapp.config;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDate localDateFromString(String stringValue) {
        return stringValue == null ? null : LocalDate.parse(stringValue);
    }

    @TypeConverter
    public static String localDateToString(LocalDate localDate) {
        return localDate == null ? null : localDate.toString();
    }

    @TypeConverter
    public static LocalDateTime localDateTimeFromString(String stringValue) {
        return stringValue == null ? null : LocalDateTime.parse(stringValue);
    }

    @TypeConverter
    public static String localDateTimeToString(LocalDateTime localDate) {
        return localDate == null ? null : localDate.toString();
    }
}
