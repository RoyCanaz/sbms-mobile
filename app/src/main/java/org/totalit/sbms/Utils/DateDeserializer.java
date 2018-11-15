package org.totalit.sbms.Utils;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer implements JsonDeserializer<Date> {
    private final String TAG = DateDeserializer.class.getSimpleName();

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String date = json.getAsString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date returnDate = null;
        try {
            returnDate = formatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Date parser exception:", e);
            returnDate = null;
        }
        return returnDate;
    }

}
