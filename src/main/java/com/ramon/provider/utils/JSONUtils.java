package com.ramon.provider.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    protected static final ObjectMapper mapper;
    protected static final ObjectWriter writer;
    protected static final ObjectReader readerObject;
    protected static final ObjectReader readerListString;
    protected static final ObjectReader readerMap;
    protected static final ObjectReader readerResponseBody;
    protected static final String JSON_ERROR = "Error converting JSON";

    static {
        mapper = new ObjectMapper();
        SimpleDateFormat d = new SimpleDateFormat(TimeUtils.EDateFormat.UTC_ISO_3.getStrDateFormat());
        d.setTimeZone(TimeUtils.EDateFormat.UTC_ISO_3.getTimeZ());
        mapper.setDateFormat(d);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
        writer = mapper.writer();
        readerObject = mapper.readerFor(new TypeReference<>() {
        });
        readerListString = mapper.readerFor(new TypeReference<List<String>>() {
        });
        readerMap = mapper.readerFor(new TypeReference<Map>() {
        });
        readerResponseBody = mapper.readerFor(new TypeReference<ResponseBody>() {
        });
    }

    public static <T extends Object> T convertValue(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }

    public static byte[] toByteArray(Object o) {
        byte[] res = null;
        try {
            res = mapper.writeValueAsBytes(o);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException ex) {
            throw new RuntimeException(JSON_ERROR, ex);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new RuntimeException(JSON_ERROR, ex);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz, Object defaultObject) {
        if (ObjectUtils.isEmpty(json)) {
            return (T) defaultObject;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(JSON_ERROR, e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> type, Object defaultObject) {
        if (ObjectUtils.isEmpty(json)) {
            return (T) defaultObject;
        }
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(JSON_ERROR, e);
        }
    }

    public static String writeJSON(ObjectWriter w, Object o) {
        if (o == null) {
            return null;
        }
        try {
            return w.writeValueAsString(o);
        } catch (IOException ex) {
            throw new RuntimeException("Error writing JSON of Object: " + o, ex);
        }
    }

    public static Object readJSON(ObjectReader r, String json) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            return r.readValue(json);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading JSON: " + json, ex);
        }
    }

    public static Object readJSON(ObjectReader r, String json, Object defaultReturn) {
        Object res = readJSON(r, json);
        if (res == null) {
            return defaultReturn;
        }
        return res;
    }

    public static String toJSON(Object o) {
        return writeJSON(writer, o);
    }

    public static Object toObject(String json) {
        return (Object) readJSON(readerObject, json, new Object());
    }

    public static Map toMap(String json) {
        return (Map) readJSON(readerMap, json, new HashMap<>());
    }

    public static Map toMap(Object object) {
        String json = toJSON(object);
        return toMap(json);
    }

    public static List<String> toListString(String json) {
        return (List<String>) readJSON(readerListString, json, new ArrayList<>());
    }
}
