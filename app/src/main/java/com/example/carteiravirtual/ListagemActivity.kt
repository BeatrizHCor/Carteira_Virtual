package com.example.carteiravirtual

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carteiravirtual.DAO.CarteiraDao
import com.example.carteiravirtual.Model.Carteira

class ListagemActivity : AppCompatActivity() {

    private lateinit var carteira: Carteira
    private lateinit var carteiraDao: CarteiraDao
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listagem)

        listView = findViewById(R.id.lvSaldos)
        carteiraDao = CarteiraDao(this)

        listaSaldos()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun listaSaldos() {
        var saldos = carteiraDao.listarValores()

        val listaExibicao = listOf(
            "Real: R$ ${String.format("%.2f", saldos.getValue("real"))}",
            "Dólar: $ ${String.format("%.2f", saldos.getValue("dolar"))}",
            "Euro: € ${String.format("%.2f", saldos.getValue("euro"))}",
            "BTC: ${String.format("%.5f", saldos.getValue("btc"))}",
            "ETH: Ξ ${String.format("%.5f", saldos.getValue("eth"))}"
        )

        listView.visibility = ListView.VISIBLE

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, listaExibicao
        )
        listView.adapter = adapter
    }
}