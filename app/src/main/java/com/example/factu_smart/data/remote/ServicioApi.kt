package com.example.factu_smart.data.remote

import com.example.factu_smart.data.model.Factura
import com.example.factu_smart.data.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ServicioApi {

    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("facturas")
    suspend fun guardarFactura(
        @Body factura: Factura
    ): retrofit2.Response<Void>

    @GET("facturas")
    suspend fun obtenerFacturas(): List<Factura>

    @GET("facturas")
    suspend fun verificarFacturaDuplicada(
        @Query("serie") serie: String,
        @Query("numero_documento") numero: String
    ): List<Factura>

    @Headers(
        "Content-Type: application/json",
        "Prefer: resolution=merge-duplicates"
    )
    @POST("usuarios")
    suspend fun guardarUsuario(
        @Body usuario: Usuario
    ): retrofit2.Response<Void>

    @GET("facturas")
    suspend fun buscarFacturaPorAutorizacion(
        @Query("numero_autorizacion") autorizacion: String
    ): List<Factura>

    @GET("usuarios")
    suspend fun obtenerUsuarioPorCorreo(
        @Query("correo") correo: String
    ): List<Usuario>
}