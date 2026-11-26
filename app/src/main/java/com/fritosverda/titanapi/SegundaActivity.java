package com.fritosverda.titanapi;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.fritosverda.titanapi.model.Familia;
import com.fritosverda.titanapi.model.Grupo;
import com.fritosverda.titanapi.model.Personaje;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class SegundaActivity extends AppCompatActivity {

    public static final String EXTRA_PERSONAJE = "personaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return insets;
        });

        Personaje p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = getIntent().getSerializableExtra(EXTRA_PERSONAJE, Personaje.class);
        } else {
            p = (Personaje) getIntent().getSerializableExtra(EXTRA_PERSONAJE);
        }
        if (p == null) { finish(); return; }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(p.getName());
        toolbar.setNavigationOnClickListener(v -> finish());

        ImageView ivDetalle = findViewById(R.id.ivDetalle);
        TextView tvNombreDetalle = findViewById(R.id.tvNombreDetalle);
        TextView tvEstadoDetalle = findViewById(R.id.tvEstadoDetalle);
        TextView tvDatos = findViewById(R.id.tvDatos);
        Button btnVolver = findViewById(R.id.btnVolver);

        Glide.with(this)
                .load(p.getImg())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivDetalle);

        tvNombreDetalle.setText(p.getName());
        tvEstadoDetalle.setText(Personaje.texto(p.getStatus()));
        tvEstadoDetalle.setTextColor(ContextCompat.getColor(this, colorEstado(p.getStatus())));

        StringBuilder sb = new StringBuilder();
        linea(sb, getString(R.string.label_especie), Personaje.unir(p.getSpecies()));
        linea(sb, getString(R.string.label_genero), Personaje.texto(p.getGender()));
        linea(sb, getString(R.string.label_edad), Personaje.texto(p.getAge()));
        linea(sb, getString(R.string.label_estatura), Personaje.texto(p.getHeight()));
        linea(sb, getString(R.string.label_alias), Personaje.unir(p.getAlias()));
        linea(sb, getString(R.string.label_nacimiento), Personaje.texto(p.getBirthplace()));
        linea(sb, getString(R.string.label_residencia), Personaje.texto(p.getResidence()));
        linea(sb, getString(R.string.label_ocupacion), Personaje.texto(p.getOccupation()));
        linea(sb, getString(R.string.label_grupos), textoGrupos(p));
        linea(sb, getString(R.string.label_familia), textoFamilia(p));
        linea(sb, getString(R.string.label_roles), Personaje.unir(p.getRoles()));

        int episodios = 0;
        if (p.getEpisodes() != null) {
            for (String ep : p.getEpisodes()) {
                if (!ep.startsWith("http")) episodios++;
            }
        }
        linea(sb, getString(R.string.label_apariciones), episodios + " " + getString(R.string.episodios));

        tvDatos.setText(Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_COMPACT));

        btnVolver.setOnClickListener(v -> finish());
    }

    private void linea(StringBuilder sb, String titulo, String valor) {
        if (valor == null || valor.equals(getString(R.string.sin_dato))) return;
        sb.append("<b>").append(titulo).append(":</b> ")
          .append(Html.escapeHtml(valor)).append("<br>");
    }

    private String textoGrupos(Personaje p) {
        if (p.getGroups() == null || p.getGroups().isEmpty()) return getString(R.string.ninguno);
        StringBuilder s = new StringBuilder();
        for (Grupo g : p.getGroups()) {
            String nombre = Personaje.texto(g.getName());
            if (nombre.equals(getString(R.string.sin_dato))) continue;

            if (s.length() > 0) s.append("; ");
            s.append(nombre);

            if (g.getSubGroups() != null && !g.getSubGroups().isEmpty()) {
                List<String> subs = new ArrayList<>();
                for (String sub : g.getSubGroups()) {
                    String subT = Personaje.texto(sub);
                    if (!subT.equals(getString(R.string.sin_dato))) subs.add(subT);
                }
                if (!subs.isEmpty()) {
                    s.append(" (").append(String.join(", ", subs)).append(")");
                }
            }
        }
        return s.length() == 0 ? getString(R.string.ninguno) : s.toString();
    }

    private String textoFamilia(Personaje p) {
        if (p.getRelatives() == null || p.getRelatives().isEmpty()) return getString(R.string.desconocida);
        StringBuilder s = new StringBuilder();
        for (Familia f : p.getRelatives()) {
            String fam = Personaje.texto(f.getFamily());
            if (fam.equals(getString(R.string.sin_dato))) continue;

            if (s.length() > 0) s.append("; ");
            s.append(fam);

            if (f.getMembers() != null && !f.getMembers().isEmpty()) {
                List<String> miembros = new ArrayList<>();
                for (String m : f.getMembers()) {
                    String mT = Personaje.texto(m);
                    if (!mT.equals(getString(R.string.sin_dato))) miembros.add(mT);
                }
                if (!miembros.isEmpty()) {
                    s.append(": ").append(String.join(", ", miembros));
                }
            }
        }
        return s.length() == 0 ? getString(R.string.desconocida) : s.toString();
    }

    private int colorEstado(String estado) {
        if ("Alive".equalsIgnoreCase(estado)) return R.color.vivo;
        if ("Deceased".equalsIgnoreCase(estado)) return R.color.muerto;
        return R.color.desconocido;
    }
}
