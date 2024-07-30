package com.example.learningenglish.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.ParameterizedType

object JsonParse {
    private val mGson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .setLenient() //            .registerTypeAdapter(Date.class, new DateTypeAdapter())
        //.registerTypeAdapter(JSONObject.class, new JSONObjectDeserializer())
        //.registerTypeAdapter(Good.Tag.class, new Good.Tag.TagDeserializer())
        .create()

    fun obj2Map(o: Any?): Map<String, Any> {
        return fromJson(toJson(o), object : TypeToken<Map<String?, Any?>?>() {
        }.type)
    }

    fun <T> toList(jsonArray: JSONArray?, typeOfT: java.lang.reflect.Type?): List<T> {
        val resultList: MutableList<T> = ArrayList()
        if (jsonArray != null && jsonArray.length() > 0) {
            var i = 0
            val size = jsonArray.length()
            while (i < size) {
                try {
                    resultList.add(mGson.fromJson(jsonArray.getString(i), typeOfT))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                i++
            }
        }
        return resultList
    }

    fun <T> toList(jsonArrayText: String?, typeOfT: T): List<T> {
        return mGson.fromJson(jsonArrayText, object : TypeToken<ArrayList<T>?>() {
        }.type)
    }

    fun <T> toList(jsonArrayText: String?, typeOfT: java.lang.reflect.Type?): List<T> {
        var jsonArray: JSONArray? = null
        try {
            jsonArray = JSONArray(jsonArrayText)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val resultList: MutableList<T> = ArrayList()
        if (jsonArray != null && jsonArray.length() > 0) {
            var i = 0
            val size = jsonArray.length()
            while (i < size) {
                try {
                    resultList.add(mGson.fromJson(jsonArray.getString(i), typeOfT))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                i++
            }
        }
        return resultList
    }

    fun <T> fromJson(json: String?, typeOfT: java.lang.reflect.Type?): T {
        return mGson.fromJson(json, typeOfT)
    }

    fun <T> fromJson(json: String?, typeOfT: Class<T>): T {
        return mGson.fromJson(json, typeOfT)
    }

    fun toJson(o: Any?): String {
        return mGson.toJson(o)
    }


    fun toJsons(vararg objects: Any?): String {
        val builder = StringBuilder()
        for (o in objects) {
            if (o != null) {
                val s = mGson.toJson(o)
                if (builder.length > 0) {
                    builder.append(s)
                    builder.deleteCharAt(builder.length - s.length)
                } else {
                    builder.append(s)
                }
                builder.deleteCharAt(builder.length - 1)
                builder.append(",")
            }
        }
        if (builder.length > 0) {
            builder.deleteCharAt(builder.length - 1)
            builder.append("}")
        }
        return builder.toString()
    }


}
