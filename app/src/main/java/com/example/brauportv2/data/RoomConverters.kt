package com.example.brauportv2.data

import androidx.room.TypeConverter
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.recipe.Hopping
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.Rest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class RoomConverters {
    @TypeConverter
    fun fromStockList(stockItemList: List<StockItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StockItem>>() {}.type
        return gson.toJson(stockItemList, type)
    }

    @TypeConverter
    fun toStockList(source: String): List<StockItem> {
        val gson = Gson()
        val type = object : TypeToken<List<StockItem>>() {}.type
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
    fun fromStockItem(stockItem: StockItem): String {
        return JSONObject().apply {
            put("id", stockItem.id)
            put("stockName", stockItem.stockName)
            put("itemType", stockItem.itemType)
            put("stockAmount", stockItem.stockAmount)
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
    fun fromRestList(restList: List<Rest>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Rest>>() {}.type
        return gson.toJson(restList, type)
    }

    @TypeConverter
    fun toRestList(source: String): List<Rest> {
        val gson = Gson()
        val type = object : TypeToken<List<Rest>>() {}.type
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