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
    fun fromHoppingList(hoppingList: List<Hopping>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.toJson(hoppingList, type)
    }

    @TypeConverter
    fun toHoppingList(source: String): List<Hopping> {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
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
    fun fromRestList(restList: List<Rest>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.toJson(restList, type)
    }

    @TypeConverter
    fun toRestList(source: String): List<Rest> {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromMainBrew(mainBrew: MainBrew): String {
        return JSONObject().apply {
            put("first", mainBrew.firstBrew)
            put("second", mainBrew.secondBrew)
        }.toString()
    }

    @TypeConverter
    fun toMainBrew(source: String): MainBrew {
        val json = JSONObject(source)
        return MainBrew(json.getString("first"), json.getString("second"))
    }
}