package com.example.brauportv2

import android.app.Application
import com.example.brauportv2.data.StockDatabase

class BaseApplication : Application() {
    val database: StockDatabase by lazy { StockDatabase.getDatabase(this) }
}