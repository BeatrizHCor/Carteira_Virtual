package com.example.carteiravirtual.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.carteiravirtual.DB.DBHelper
import com.example.carteiravirtual.Model.Carteira

class CarteiraDao(private val context: Context) {
    private val dbHelper = DBHelper(context)

    fun depositar(valor: Float){
        val db = dbHelper.writableDatabase
        val real = this.listarValores().real
        val values = ContentValues().apply {
            put("real", real + valor)
        }
        val whereArgs = arrayOf("")
        db.update(DBHelper.TABLE_NAME, values, "", whereArgs)
        db.close()
    }
    fun converter(deMoeda: String, paraMoeda: String, valor: Float){
        val db = dbHelper.writableDatabase
        val val1: Float = this.listarValores().getValue(deMoeda)
        val val2: Float = this.listarValores().getValue(paraMoeda)

    }
    fun listarValores(): Carteira{
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)
        println("Aqui")
        var real = 0f
        var dolar = 0f
        var euro = 0f
        var btc = 0f
        var eth = 0f
        while (cursor.moveToNext()){
        real = cursor.getFloat(cursor.getColumnIndexOrThrow("real"))
        dolar = cursor.getFloat(cursor.getColumnIndexOrThrow("dolar"))
        euro = cursor.getFloat(cursor.getColumnIndexOrThrow("euro"))
        btc = cursor.getFloat(cursor.getColumnIndexOrThrow("btc"))
        eth = cursor.getFloat(cursor.getColumnIndexOrThrow("eth"))
        }
        db.close()
        return Carteira(real, dolar, euro, btc, eth)
    }
}