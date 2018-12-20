package com.lab.growbox.growbox.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {

    private static final String DATE_FORMAT = "dd.mm.yyyy";

    @Override
    public java.sql.Date deserialize(JsonElement jsonElement, Type typeOF,
                                     JsonDeserializationContext context) throws JsonParseException {
        try {
            java.util.Date utilDate =  new SimpleDateFormat(DATE_FORMAT).parse(jsonElement.getAsString());
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
        }
        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported format: " + DATE_FORMAT);
    }
}