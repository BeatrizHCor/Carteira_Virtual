package com.example.carteiravirtual.DB

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
            companion object {
                const val DATABASE_NAME = "carteira.db"
                const val DATABASE_VERSION = 1
                const val TABLE_NAME = "carteira"
            }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME(real FLOAT NOT NULL, dolar FLOAT NOT NULL, euro FLOAT NOT NULL, btc FLOAT NOT NULL, eth FLOAT NOT NULl);
        """.trimIndent()
        db.execSQL(createTable)
        val values = ContentValues().apply {
            put("real", 0f)
            put("dolar", 0f)
            put("euro", 0f)
            put("btc", 0f)
            put("eth", 0f)

        }
        db.insert(TABLE_NAME, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }
        }