package com.example.carteiravirtual

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carteiravirtual.DAO.CarteiraDao
import com.example.carteiravirtual.Model.Carteira

class ConverterActivity : AppCompatActivity() {

    private lateinit var carteira: Carteira
    private lateinit var carteiraDao: CarteiraDao
    private lateinit var spinnerOrigem: Spinner
    private lateinit var spinnerDestino: Spinner

    val options = arrayOf("Opção 1", "Opção 2", "Opção 3", "Opção 4")

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


        setSpinner()



    }

    fun setSpinner(){

            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerOrigem.adapter = adapter
            spinnerDestino.adapter = adapter

    }
}