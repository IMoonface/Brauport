package com.example.brauportv2.data.typeconverter

import androidx.room.TypeConverter
import com.example.brauportv2.model.recipeModel.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class RoomConverters {
    @TypeConverter
    fun fromListMalt(malts: List<Malt>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.toJson(malts, type)
    }

    @TypeConverter
    fun toMalt(source: String): List<Malt> {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromRest(rest: Rest): String {
        return JSONObject().apply {
            put("restTemp", rest.restTemp)
            put("restTime", rest.restTime)
        }.toString()
    }

    @TypeConverter
    fun toRest(source: String): Rest {
        val json = JSONObject(source)
        return Rest(json.getString("restTemp"), json.getString("restTime"))
    }

    @TypeConverter
    fun fromListHopping(hopping: List<Hopping>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.toJson(hopping, type)
    }

    @TypeConverter
    fun toListHopping(source: String): List<Hopping> {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromYeast(yeast: Yeast): String {
        return JSONObject().apply {
            put("rStockName", yeast.rStockName)
            put("itemType", yeast.itemType)
            put("rStockAmount", yeast.rStockAmount)
        }.toString()
    }

    @TypeConverter
    fun toYeast(source: String): Yeast {
        val json = JSONObject(source)
        return Yeast(json.getString("rStockName"), json.getInt("itemType"), json.getString("rStockAmount"))
    }

    @TypeConverter
    fun fromMainBrew(mainBrew: MainBrew): String {
        return JSONObject().apply {
            put("first", mainBrew.first)
            put("second", mainBrew.second)
        }.toString()
    }

    @TypeConverter
    fun toMainBrew(source: String): MainBrew {
        val json = JSONObject(source)
        return MainBrew(json.getString("first"), json.getString("second"))
    }
}