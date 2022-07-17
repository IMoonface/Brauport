package com.example.brauportv2.data

import androidx.room.TypeConverter
import com.example.brauportv2.model.recipeModel.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class RoomConverters {
    @TypeConverter
    fun fromRStockList(rStockItemList: List<RStockItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RStockItem>>() {}.type
        return gson.toJson(rStockItemList, type)
    }

    @TypeConverter
    fun toRStockList(source: String): List<RStockItem> {
        val gson = Gson()
        val type = object : TypeToken<List<RStockItem>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromTimeList(timeList: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<RStockItem>>() {}.type
        return gson.toJson(timeList, type)
    }

    @TypeConverter
    fun toTimeList(source: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<RStockItem>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromRStockItem(rStockItem: RStockItem): String {
        return JSONObject().apply {
            put("rStockName", rStockItem.rStockName)
            put("rItemType", rStockItem.rItemType)
            put("rStockAmount", rStockItem.rStockAmount)
        }.toString()
    }

    @TypeConverter
    fun toRStockItem(source: String): RStockItem {
        val json = JSONObject(source)
        return RStockItem(
            json.getString("rStockName"),
            json.getInt("rItemType"),
            json.getString("rStockAmount")
        )
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