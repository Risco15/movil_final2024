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

    @GET("/listado")
    Call<List<Reserva>> obtenerReservas();


    @POST("/reservas")
    Call<Reserva> registrarReserva(@Body Reserva reserva);


    @PUT("reservas/actualizar/{id}")
    Call<Reserva> updateReserva(@Path("id") int id, @Body Reserva reserva);


    @DELETE("/reservas/eliminar/{id}")
    Call<Void> eliminarReserva(@Path("id") int id);
}
