package com.negocio.semana04comsumorest.service;

import com.negocio.semana04comsumorest.entity.Reserva;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservaApiService {
    // Endpoint para obtener la lista de reservas
    @GET("/listado") // Esta ruta se mantiene tal como está en tu controlador
    Call<List<Reserva>> obtenerReservas();

    // Endpoint para registrar una nueva reserva
    @POST("/reservas") // Esta ruta se mantiene
    Call<Reserva> registrarReserva(@Body Reserva reserva);


    @PUT("reservas/actualizar/{id}")
    Call<Reserva> updateReserva(@Path("id") int id, @Body Reserva reserva);




    // Endpoint para eliminar una reserva por ID
    @DELETE("/reservas/eliminar/{id}") // Esta ruta se mantiene
    Call<Void> eliminarReserva(@Path("id") int id);
}
