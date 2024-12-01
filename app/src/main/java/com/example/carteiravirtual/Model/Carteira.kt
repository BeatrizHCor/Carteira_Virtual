package com.example.carteiravirtual.Model

data class Carteira(val real: Float, val dolar: Float, val euro: Float, val btc: Float, val eth: Float){
    fun getValue(string: String): Float{
        if(string == "real"){
            return this.real
        }
        if(string == "dolar"){
            return this.dolar
        }
        if(string == "euro"){
            return this.euro
        }
        if(string == "btc"){
            return this.btc
        }
        if(string == "eth"){
            return this.eth
        }
        return 0f
    }
}
