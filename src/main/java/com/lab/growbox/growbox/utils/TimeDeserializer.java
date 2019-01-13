package com.lab.growbox.growbox.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDeserializer implements JsonDeserializer {

    private static final String DATE_FORMAT = "HH:mm:ss";

    @Override
    public Object deserialize(JsonElement jsonElement, Type typeOF,
                              JsonDeserializationContext context) throws JsonParseException {
        try {
            Date utilDate =  new SimpleDateFormat(DATE_FORMAT).parse(jsonElement.getAsString());
            return utilDate.getTime();
        } catch (ParseException e) {
        }
        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported format: " + DATE_FORMAT);
    }
}