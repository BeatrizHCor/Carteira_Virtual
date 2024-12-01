package com.example.carteiravirtual

import android.content.Intent
import android.os.Bundle
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
        carteira = carteiraDao.listarValores()
        saldo = findViewById(R.id.textSaldo)
        saldo.text = "Seu saldo em Reais é de: R$ " + carteira.getValue("real").toString()

        binding.buttonDepositar.setOnClickListener{
            val intent = Intent(this, DepositarActivity::class.java)
            startActivity(intent)
        }

    }
}