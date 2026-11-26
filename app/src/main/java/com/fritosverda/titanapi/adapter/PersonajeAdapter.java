package com.fritosverda.titanapi.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fritosverda.titanapi.R;
import com.fritosverda.titanapi.model.Personaje;

import java.util.ArrayList;
import java.util.List;

public class PersonajeAdapter extends RecyclerView.Adapter<PersonajeAdapter.PersonajeViewHolder> {

    public interface OnPersonajeClick {
        void onClick(Personaje personaje);
    }

    private final List<Personaje> personajes = new ArrayList<>();
    private final OnPersonajeClick listener;

    public PersonajeAdapter(OnPersonajeClick listener) {
        this.listener = listener;
    }

    public void agregarPersonajes(List<Personaje> nuevos) {
        int inicio = personajes.size();
        personajes.addAll(nuevos);
        notifyItemRangeInserted(inicio, nuevos.size());
    }

    @NonNull
    @Override
    public PersonajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_personaje, parent, false);
        return new PersonajeViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonajeViewHolder holder, int position) {
        holder.bind(personajes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return personajes.size();
    }

    static class PersonajeViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPersonaje;
        private final TextView tvNombre, tvEstadoEspecie, tvOcupacion, tvResidencia;
        private final View viewEstado;

        PersonajeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPersonaje = itemView.findViewById(R.id.ivPersonaje);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvEstadoEspecie = itemView.findViewById(R.id.tvEstadoEspecie);
            tvOcupacion = itemView.findViewById(R.id.tvOcupacion);
            tvResidencia = itemView.findViewById(R.id.tvResidencia);
            viewEstado = itemView.findViewById(R.id.viewEstado);
        }

        void bind(Personaje p, OnPersonajeClick listener) {
            tvNombre.setText(p.getName());
            tvEstadoEspecie.setText(Personaje.texto(p.getStatus()) + " - " + Personaje.unir(p.getSpecies()));
            tvOcupacion.setText(itemView.getContext().getString(R.string.item_ocupacion, Personaje.texto(p.getOccupation())));
            tvResidencia.setText(itemView.getContext().getString(R.string.item_residencia, Personaje.texto(p.getResidence())));

            GradientDrawable circulo = new GradientDrawable();
            circulo.setShape(GradientDrawable.OVAL);
            circulo.setColor(ContextCompat.getColor(itemView.getContext(), colorEstado(p.getStatus())));
            viewEstado.setBackground(circulo);

            Glide.with(itemView.getContext())
                    .load(p.getImg())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .centerCrop()
                    .into(ivPersonaje);

            itemView.setOnClickListener(v -> listener.onClick(p));
        }

        static int colorEstado(String estado) {
            if ("Alive".equalsIgnoreCase(estado)) return R.color.vivo;
            if ("Deceased".equalsIgnoreCase(estado)) return R.color.muerto;
            return R.color.desconocido;
        }
    }
}
