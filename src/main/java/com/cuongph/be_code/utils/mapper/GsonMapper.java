package com.cuongph.be_code.utils.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonMapper {

    private static final GsonBuilder builder = new GsonBuilder();
    private static final Gson gson = builder.setPrettyPrinting().create();

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            T obj = gson.fromJson(json, clazz);
            return obj;
        } catch (Exception var4) {
            throw new IllegalArgumentException(var4);
        }
    }

    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception var4) {
            throw new IllegalArgumentException(var4);
        }
    }


    public static <T> List<T> toListObject(String json, Class<T> valueType) {
        try {
            Type gsonType = TypeToken.getParameterized(ArrayList.class, valueType).getType();
            return gson.fromJson(json, gsonType);
        } catch (Exception var4) {
            throw new IllegalArgumentException(var4);
        }
    }

//    public static <T> List<T> toArrayList(String json, Class<T> valueType) {
//        Type listType = TypeToken.getParameterized(ArrayList.class, valueType).getType();
//        return gson.fromJson(json, listType);
//    }

}
