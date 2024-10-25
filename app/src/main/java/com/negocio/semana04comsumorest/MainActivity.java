package com.negocio.semana04comsumorest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.negocio.semana04comsumorest.entity.Reserva;
import com.negocio.semana04comsumorest.service.ReservaApiService;
import com.negocio.semana04comsumorest.util.ConnectionRest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ReservaApiService apiService;
    private TextView listaReservasTextView;
    private Button botonActualizar;
    private List<Reserva> reservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listaReservasTextView = findViewById(R.id.lista_reservas);
        botonActualizar = findViewById(R.id.boton_actualizar);

        ViewCompat.setOnApplyWindowInsetsListener(listaReservasTextView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiService = ConnectionRest.getConnecion().create(ReservaApiService.class);
        listarReservas();

        botonActualizar.setOnClickListener(v -> mostrarDialogoSeleccionarReserva());
    }

    private void listarReservas() {
        apiService.obtenerReservas().enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                if (response.isSuccessful()) {
                    reservas = response.body();
                    if (reservas != null && !reservas.isEmpty()) {
                        obtenerNombresYDescripciones(reservas);
                    } else {
                        mostrarMensaje("No hay reservas disponibles.");
                    }
                } else {
                    mostrarMensaje("Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                mostrarMensaje("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void obtenerNombresYDescripciones(List<Reserva> reservas) {
        Map<Integer, String> nombresMedicos = new HashMap<>();
        Map<Integer, String> descripcionesEspecialidad = new HashMap<>();

        // Cargar los nombres de médicos y descripciones de especialidades
        nombresMedicos.put(1, "Dr. Juan Pérez");
        nombresMedicos.put(2, "Dra. Ana Gómez");
        nombresMedicos.put(3, "Dr. Carlos Ruiz");

        descripcionesEspecialidad.put(1, "Cardiología");
        descripcionesEspecialidad.put(2, "Pediatría");
        descripcionesEspecialidad.put(3, "Dermatología");

        StringBuilder reservasText = new StringBuilder();
        for (Reserva reserva : reservas) {
            String nombreMedico = nombresMedicos.get(reserva.getIdMedico());
            String descripcionEspecialidad = descripcionesEspecialidad.get(reserva.getIdEspecialidad());

            reservasText.append("Número de Reserva: ").append(reserva.getNumReserva()).append("\n")
                    .append("DNI: ").append(reserva.getDni()).append("\n")
                    .append("Email: ").append(reserva.getEmail()).append("\n")
                    .append("Teléfono: ").append(reserva.getTelefono()).append("\n")
                    .append("Fecha de Cita: ").append(reserva.getFechaCita()).append("\n")
                    .append("Especialidad: ").append(descripcionEspecialidad != null ? descripcionEspecialidad : "No disponible").append("\n")
                    .append("Nombre del Médico: ").append(nombreMedico != null ? nombreMedico : "No disponible").append("\n")
                    .append("Nombre del Paciente: ").append(reserva.getNombrePaciente()).append("\n")
                    .append("Hora de Cita: ").append(reserva.getHoraCita()).append("\n\n");
        }

        listaReservasTextView.setText(reservasText.toString());
    }

    private void mostrarDialogoSeleccionarReserva() {
        if (reservas == null || reservas.isEmpty()) {
            mostrarMensaje("No hay reservas para seleccionar.");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Reserva");

        String[] opciones = new String[reservas.size()];
        for (int i = 0; i < reservas.size(); i++) {
            opciones[i] = "Reserva " + reservas.get(i).getNumReserva();
        }

        builder.setItems(opciones, (dialog, which) -> {
            Reserva reservaSeleccionada = reservas.get(which);
            mostrarDialogoActualizar(reservaSeleccionada);
        });

        builder.show();
    }

    private void mostrarDialogoActualizar(Reserva reserva) {
        if (isFinishing()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Reserva");
        builder.setMessage("¿Desea actualizar la reserva con número: " + reserva.getNumReserva() + "?");

        builder.setPositiveButton("Sí", (dialog, which) -> mostrarDialogoIngresoDatos(reserva));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarDialogoIngresoDatos(Reserva reserva) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Reserva " + reserva.getNumReserva());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_actualizar_reserva, null);
        EditText editTextDNI = dialogView.findViewById(R.id.editTextDNI);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);
        EditText editTextTelefono = dialogView.findViewById(R.id.editTextTelefono);
        EditText editTextFechaCita = dialogView.findViewById(R.id.editTextFechaCita);
        EditText editTextHoraCita = dialogView.findViewById(R.id.editTextHoraCita);
        EditText editTextNombrePaciente = dialogView.findViewById(R.id.editTextNombrePaciente);


        editTextDNI.setText(reserva.getDni());
        editTextEmail.setText(reserva.getEmail());
        editTextTelefono.setText(reserva.getTelefono());
        editTextFechaCita.setText(reserva.getFechaCita());
        editTextHoraCita.setText(reserva.getHoraCita());
        editTextNombrePaciente.setText(reserva.getNombrePaciente());

        builder.setView(dialogView);
        builder.setPositiveButton("Actualizar", null); // Inicialmente, no se asigna un Listener aquí.

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button btnActualizar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnActualizar.setOnClickListener(v -> {

                if (validarCampos(editTextDNI, editTextEmail, editTextTelefono, editTextFechaCita, editTextHoraCita, editTextNombrePaciente)) {
                    Reserva reservaActualizada = new Reserva();
                    reservaActualizada.setNumReserva(reserva.getNumReserva());
                    reservaActualizada.setDni(editTextDNI.getText().toString());
                    reservaActualizada.setEmail(editTextEmail.getText().toString());
                    reservaActualizada.setTelefono(editTextTelefono.getText().toString());
                    reservaActualizada.setFechaCita(editTextFechaCita.getText().toString());
                    reservaActualizada.setHoraCita(editTextHoraCita.getText().toString());
                    reservaActualizada.setNombrePaciente(editTextNombrePaciente.getText().toString());
                    dialog.dismiss();


                    apiService.updateReserva(reserva.getNumReserva(), reservaActualizada).enqueue(new Callback<Reserva>() {
                        @Override
                        public void onResponse(Call<Reserva> call, Response<Reserva> response) {



                        }

                        @Override
                        public void onFailure(Call<Reserva> call, Throwable t) {
                            listarReservas();
                        }
                    });

                } else {
                    mostrarMensaje("Por favor, complete todos los campos correctamente.");
                }
            });
        });

        dialog.show();
    }



    private boolean validarCampos(EditText... campos) {
        for (EditText campo : campos) {
            if (campo.getText().toString().isEmpty()) {
                return false; // Un campo está vacío
            }
        }
        return true;
    }
}
