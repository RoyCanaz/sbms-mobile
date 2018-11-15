package org.totalit.sbms.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {
    private final String TAG = DateSerializer.class.getSimpleName();
    public static String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String dateFormatAsString = formatter.format(src);
        return new JsonPrimitive(dateFormatAsString);
    }
}
