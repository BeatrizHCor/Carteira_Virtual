package com.example.carteiravirtual.API

import com.example.carteiravirtual.Model.Moeda
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MoedaApi {
    @GET("last/{origem}-{destino}")
    suspend fun getCotacao(
        @Path("origem") origem: String,
        @Path("destino") destino: String
    ): Response<Map<String, Moeda>>
}