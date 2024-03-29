package com.example.brauport.data

import androidx.room.TypeConverter
import com.example.brauport.model.recipe.Hopping
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.model.recipe.Rest
import com.example.brauport.model.stock.StockItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class RoomConverters {
    @TypeConverter
    fun fromStockList(list: List<StockItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StockItem>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toStockList(source: String): List<StockItem> {
        val gson = Gson()
        val type = object : TypeToken<List<StockItem>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromHoppingList(list: List<Hopping>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toHoppingList(source: String): List<Hopping> {
        val gson = Gson()
        val type = object : TypeToken<List<Hopping>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromStockItem(item: StockItem): String {
        return JSONObject().apply {
            put("id", item.id)
            put("itemType", item.itemType)
            put("stockName", item.name)
            put("stockAmount", item.amount)
        }.toString()
    }

    @TypeConverter
    fun toStockItem(source: String): StockItem {
        val json = JSONObject(source)
        return StockItem(
            json.getInt("id"),
            json.getInt("itemType"),
            json.getString("stockName"),
            json.getString("stockAmount")
        )
    }

    @TypeConverter
    fun fromRestList(list: List<Rest>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Rest>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toRestList(source: String): List<Rest> {
        val gson = Gson()
        val type = object : TypeToken<List<Rest>>() {}.type
        return gson.fromJson(source, type)
    }

    @TypeConverter
    fun fromMainBrew(item: MainBrew): String {
        return JSONObject().apply {
            put("first", item.firstBrew)
            put("second", item.secondBrew)
        }.toString()
    }

    @TypeConverter
    fun toMainBrew(source: String): MainBrew {
        val json = JSONObject(source)
        return MainBrew(json.getString("first"), json.getString("second"))
    }
}