package com.example.carteiravirtual

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carteiravirtual.DAO.CarteiraDao

class DepositarActivity : AppCompatActivity() {

    private lateinit var carteiraDao: CarteiraDao
    private lateinit var inputDepositar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_depositar2)

        carteiraDao = CarteiraDao(this)
        inputDepositar = findViewById(R.id.inputDepositar)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun saveDeposito(view: View) {
        val newDeposito = try {
            inputDepositar.text.toString().toFloat()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            if (newDeposito > 0) {
                carteiraDao.depositar(newDeposito)
                finish()

            } else {
                Toast.makeText(this, "Insira um valor positivo para depositar", Toast.LENGTH_SHORT).show()
            }
        }
        catch( e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }
}
