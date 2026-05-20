package com.example.factu_smart.data.remote

import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ServicioApi {

    @GET("facturas")
    suspend fun obtenerFacturas(): List<Factura>

    @POST("facturas")
    suspend fun guardarFactura(@Body factura: Factura): Response<Unit>

    @Headers("Prefer: resolution=merge-duplicates")
    @POST("usuarios")
    suspend fun guardarUsuario(@Body usuario: Usuario): Response<Unit>

    @GET("usuarios")
    suspend fun obtenerUsuarioPorCorreo(
        @Query("correo") correo: String
    ): List<Usuario>
}