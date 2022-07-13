package com.example.brauportv2.data.typeconverter

import androidx.room.TypeConverter
import com.example.brauportv2.model.recipeModel.Hopping
import com.example.brauportv2.model.recipeModel.MainBrew
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.model.recipeModel.Rest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class RoomConverters {
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
    fun fromRStockItem(rStockItem: RStockItem): String {
        return JSONObject().apply {
            put("rStockName", rStockItem.rStockName)
            put("rStockAmount", rStockItem.rStockAmount)
        }.toString()
    }

    @TypeConverter
    fun toRStockItem(source: String): RStockItem {
        val json = JSONObject(source)
        return RStockItem(json.getString("rStockName"), json.getString("rStockAmount"))
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

    @TypeConverter
    fun fromListHopping(value: List<Hopping>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toListHopping(value: String): List<Hopping> {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromListRStockItem(value: List<RStockItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RStockItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toListRStockItem(value: String): List<RStockItem> {
        val gson = Gson()
        val type = object : TypeToken<List<RStockItem>>() {}.type
        return gson.fromJson(value, type)
    }
}