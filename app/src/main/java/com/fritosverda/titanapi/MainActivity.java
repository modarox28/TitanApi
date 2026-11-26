package com.fritosverda.titanapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fritosverda.titanapi.adapter.PersonajeAdapter;
import com.fritosverda.titanapi.api.RetrofitClient;
import com.fritosverda.titanapi.model.Info;
import com.fritosverda.titanapi.model.RespuestaPersonajes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPersonajes;
    private PersonajeAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar pbCarga;
    private TextView tvContador;
    private LinearLayout layoutError;

    private int paginaActual = 0;
    private int totalPaginas = 1;
    private int totalPersonajes = 0;
    private boolean cargando = false;
    private boolean hayMasPaginas = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        rvPersonajes = findViewById(R.id.rvPersonajes);
        pbCarga = findViewById(R.id.pbCarga);
        tvContador = findViewById(R.id.tvContador);
        layoutError = findViewById(R.id.layoutError);
        Button btnReintentar = findViewById(R.id.btnReintentar);

        layoutManager = new LinearLayoutManager(this);
        rvPersonajes.setLayoutManager(layoutManager);

        adapter = new PersonajeAdapter(personaje -> {
            Intent intent = new Intent(this, SegundaActivity.class);
            intent.putExtra(SegundaActivity.EXTRA_PERSONAJE, personaje);
            startActivity(intent);
        });
        rvPersonajes.setAdapter(adapter);

        rvPersonajes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy <= 0) return;

                int visibles = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int primeraVisible = layoutManager.findFirstVisibleItemPosition();

                if (!cargando && hayMasPaginas && (primeraVisible + visibles) >= total - 5) {
                    cargarPagina(paginaActual + 1);
                }
            }
        });

        btnReintentar.setOnClickListener(v -> cargarPagina(paginaActual + 1));

        cargarPagina(1);
    }

    private void cargarPagina(int pagina) {
        cargando = true;
        pbCarga.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);

        RetrofitClient.getServicio().obtenerPersonajes(pagina).enqueue(new Callback<RespuestaPersonajes>() {
            @Override
            public void onResponse(@NonNull Call<RespuestaPersonajes> call,
                                   @NonNull Response<RespuestaPersonajes> response) {
                cargando = false;
                pbCarga.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    RespuestaPersonajes datos = response.body();
                    Info info = datos.getInfo();

                    paginaActual = pagina;
                    totalPaginas = info.getPages();
                    totalPersonajes = info.getCount();
                    hayMasPaginas = info.getNextPage() != null;

                    adapter.agregarPersonajes(datos.getResults());
                    actualizarContador();
                } else {
                    mostrarError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RespuestaPersonajes> call, @NonNull Throwable t) {
                cargando = false;
                pbCarga.setVisibility(View.GONE);
                mostrarError();
            }
        });
    }

    private void actualizarContador() {
        int resId = hayMasPaginas ? R.string.contador_formato : R.string.contador_completo;
        tvContador.setText(getString(resId, adapter.getItemCount(), totalPersonajes, paginaActual, totalPaginas));
    }

    private void mostrarError() {
        if (adapter.getItemCount() == 0) {
            layoutError.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.error_conexion, Toast.LENGTH_SHORT).show();
        }
    }
}
