package com.example.carteiravirtual

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.carteiravirtual.API.MoedaApi
import com.example.carteiravirtual.DAO.CarteiraDao
import com.example.carteiravirtual.Model.Carteira
import com.example.carteiravirtual.Model.Moeda
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConverterActivity : AppCompatActivity() {

    private lateinit var carteira: Carteira
    private lateinit var carteiraDao: CarteiraDao
    private lateinit var spinnerOrigem: Spinner
    private lateinit var spinnerDestino: Spinner
    private lateinit var moedaApi: MoedaApi
    private lateinit var inputConverter: EditText

    val options = listOf(
        Pair("Real", "BRL"),
        Pair("Dolar", "USD"),
        Pair("Euro", "EUR"),
        Pair("BTC", "BTC"),
        Pair("ETH", "ETH")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_converter)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerOrigem = findViewById(R.id.spinnerOrigem)
        spinnerDestino = findViewById(R.id.spinnerDestino)
        carteiraDao = CarteiraDao(this)
        inputConverter = findViewById(R.id.editTextValor)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        moedaApi = retrofit.create(MoedaApi::class.java)
        setSpinner()
    }

    fun setSpinner() {

        val names = options.map { it.first }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrigem.adapter = adapter
        spinnerDestino.adapter = adapter

    }

    fun converteMoeda(view: View) {
        try {
            val origem = spinnerOrigem.selectedItem.toString()
            val origemCodigo = options.find { it.first == origem }?.second
            val destino = spinnerDestino.selectedItem.toString()
            val destinoCodigo = options.find { it.first == destino }?.second
            var valorConverter = inputConverter.text.toString().toFloat()
            var criptoMoedasDestino = false
            var criptoMoedasOrigem = false

            lifecycleScope.launch {
                try {
                    val response: Response<*>?

                    if (origemCodigo in listOf("BTC", "ETH") && destinoCodigo in listOf("BTC", "ETH")) {
                        val responseOrigem = moedaApi.getCotacao(origemCodigo.toString(), "USD")
                        val responseDestino = moedaApi.getCotacao(destinoCodigo.toString(), "USD")

                        if (responseOrigem.isSuccessful && responseDestino.isSuccessful) {
                            val valorOrigemUSD = responseOrigem.body()?.values?.firstOrNull()?.bid?.toFloatOrNull() ?: 0f
                            val valorDestinoUSD = responseDestino.body()?.values?.firstOrNull()?.bid?.toFloatOrNull() ?: 0f

                            if (valorOrigemUSD > 0 && valorDestinoUSD > 0) {
                                val valorEmUSD = valorConverter * valorOrigemUSD
                                val valorFinal = valorEmUSD / valorDestinoUSD

                                validarMoeda(origemCodigo.toString(), valorConverter)
                                carteiraDao.converter(origem, destino, valorFinal, valorConverter)

                                val mensagem = formatarMensagemConversao(
                                    valorConverter,
                                    origemCodigo.toString(),
                                    valorFinal,
                                    destinoCodigo.toString()
                                )
                                Toast.makeText(
                                    this@ConverterActivity,
                                    mensagem,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        if (destinoCodigo in listOf("BTC", "ETH")) {
                            criptoMoedasDestino = true
                            response = moedaApi.getCotacao(destinoCodigo.toString(), origemCodigo.toString())
                        } else if (origemCodigo in listOf("BTC", "ETH")) {
                            criptoMoedasOrigem = true
                            response = moedaApi.getCotacao(origemCodigo.toString(), destinoCodigo.toString())
                        } else {
                            response = moedaApi.getCotacao(origemCodigo.toString(), destinoCodigo.toString())
                        }

                        if (response!!.isSuccessful) {
                            val corpo = response.body()
                            val moeda = corpo?.values?.firstOrNull()?.bid?.toFloatOrNull()
                            if (moeda != null) {
                                validarMoeda(origemCodigo.toString(), valorConverter)
                                val convertido = realizaConversao(valorConverter, moeda, criptoMoedasDestino)
                                carteiraDao.converter(origem, destino, convertido, valorConverter)

                                val mensagem = formatarMensagemConversao(
                                    valorConverter,
                                    origemCodigo.toString(),
                                    convertido,
                                    destinoCodigo.toString()
                                )
                                Toast.makeText(
                                    this@ConverterActivity,
                                    mensagem,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } catch (ex: Exception) {
                    Log.e(ex.message, ex.message, ex)
                    Toast.makeText(
                        this@ConverterActivity,
                        "Erro na conversão: ${ex.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Erro ao buscar a moeda", e)
        }
    }

    private fun formatarMensagemConversao(
        valorOrigem: Float,
        moedaOrigem: String,
        valorDestino: Float,
        moedaDestino: String
    ): String {
        val formatoOrigem = when (moedaOrigem) {
            "BTC" -> "%.8f"
            "ETH" -> "%.8f"
            else -> "%.2f"
        }

        val formatoDestino = when (moedaDestino) {
            "BTC" -> "%.8f"
            "ETH" -> "%.8f"
            else -> "%.2f"
        }

        val simboloOrigem = when (moedaOrigem) {
            "BTC" -> "₿"
            "ETH" -> "Ξ"
            "USD" -> "$"
            "EUR" -> "€"
            "BRL" -> "R$"
            else -> ""
        }

        val simboloDestino = when (moedaDestino) {
            "BTC" -> "₿"
            "ETH" -> "Ξ"
            "USD" -> "$"
            "EUR" -> "€"
            "BRL" -> "R$"
            else -> ""
        }

        return String.format(
            "%s%s %s -> %s%s %s",
            simboloOrigem,
            String.format(formatoOrigem, valorOrigem),
            moedaOrigem,
            simboloDestino,
            String.format(formatoDestino, valorDestino),
            moedaDestino
        )
    }

    private fun realizaConversao(
        valorOrigem: Float,
        taxaCambio: Float,
        criptoMoedasDestino: Boolean
    ): Float {
        if (criptoMoedasDestino)
            return valorOrigem / taxaCambio
        return valorOrigem * taxaCambio
    }

    private fun validarMoeda(origem: String, valorConverter: Float) {
        val carteira = carteiraDao.listarValores()
        val meuSaldo: Float

        when (origem) {
            "EUR" -> meuSaldo = carteira.euro
            "BRL" -> meuSaldo = carteira.real
            "USD" -> meuSaldo = carteira.dolar
            "BTC" -> meuSaldo = carteira.btc
            "ETH" -> meuSaldo = carteira.eth
            else -> {
                meuSaldo = 0f
            }
        }
        if (meuSaldo < valorConverter) {
            Toast.makeText(this, "Saldo insuficiente para comprar moeda", Toast.LENGTH_SHORT).show()
            throw Exception("Falta de saldo para comprar")
        }
    }
}