package com.example.carteiravirtual

import android.content.Intent
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
import com.example.carteiravirtual.databinding.ActivityDepositarBinding
import com.example.carteiravirtual.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var carteira: Carteira
    private lateinit var carteiraDao: CarteiraDao
    private lateinit var saldo: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        carteiraDao = CarteiraDao(this)
        saldo = findViewById(R.id.textSaldo)

        carteira = carteiraDao.listarValores()
        setSaldo(carteira.real)

        binding.buttonDepositar.setOnClickListener{
            val intent = Intent(this, DepositarActivity::class.java)
            startActivity(intent)
        }

        binding.buttonListar.setOnClickListener{
            val intent = Intent(this, ListagemActivity::class.java)
            startActivity(intent)
        }

        binding.buttonConverter.setOnClickListener{
            val intent = Intent(this, ConverterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun listValores() {
        val valores = carteiraDao.listarValores()
        carteira = valores
    }

    fun setSaldo(novoSaldo: Float){
        saldo.text = "Seu saldo em Reais é de: R$ ${String.format("%.2f", novoSaldo)}"
    }

    override fun onResume() {
        super.onResume()
        listValores()
        setSaldo(carteira.real)

    }
}