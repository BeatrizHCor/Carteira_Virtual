package com.example.carteiravirtual.Model

data class Carteira(val real: Float, val dolar: Float, val euro: Float, val btc: Float, val eth: Float){
    fun getValue(string: String): Float{
        if(string.lowercase() == "real"){
            return this.real
        }
        if(string.lowercase() == "dolar"){
            return this.dolar
        }
        if(string.lowercase() == "euro"){
            return this.euro
        }
        if(string.lowercase() == "btc"){
            return this.btc
        }
        if(string.lowercase() == "eth"){
            return this.eth
        }
        return 0f
    }
}
