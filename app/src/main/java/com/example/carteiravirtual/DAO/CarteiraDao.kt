package com.example.carteiravirtual.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.carteiravirtual.DB.DBHelper
import com.example.carteiravirtual.Model.Carteira

class CarteiraDao(private val context: Context) {
    private val dbHelper = DBHelper(context)

    fun depositar(valor: Float) {
        val real = this.listarValores().real

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("real", real + valor)
        }

        db.update(DBHelper.TABLE_NAME, values, null, null)
        db.close()
    }

    fun converter(deMoeda: String, paraMoeda: String, valor: Float, valorConverter: Float){

        var listaModas = this.listarValores()
        val db = dbHelper.writableDatabase
        val valorOrigem: Float
        val valorDestino: Float

        valorOrigem = listaModas.getValue(deMoeda.toLowerCase())
        valorDestino  = listaModas.getValue(paraMoeda.toLowerCase())

        val values = ContentValues().apply {
            put(paraMoeda, valorDestino + valor)
            put(deMoeda, valorOrigem-valorConverter)
        }
        db.update(DBHelper.TABLE_NAME, values, null, null)
        db.close()

    }
    fun listarValores(): Carteira{
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)
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