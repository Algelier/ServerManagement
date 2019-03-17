package net.algelier.servermanagement.utils;

import com.google.gson.JsonElement;

public class JsonUtils {

    public static String getStringOrNull(JsonElement element) {
        return element.isJsonNull() ? null : element.getAsString();
    }
}
