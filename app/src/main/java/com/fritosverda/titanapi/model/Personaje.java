package com.fritosverda.titanapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Personaje implements Serializable {

    private int id;
    private String name;
    private String img;
    private List<String> alias;
    private List<String> species;
    private String gender;
    private String age;
    private String height;
    private List<Familia> relatives;
    private String birthplace;
    private String residence;
    private String status;
    private String occupation;
    private List<Grupo> groups;
    private List<String> roles;
    private List<String> episodes;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImg() { return img; }
    public List<String> getAlias() { return alias; }
    public List<String> getSpecies() { return species; }
    public String getGender() { return gender; }
    public String getAge() { return age; }
    public String getHeight() { return height; }
    public List<Familia> getRelatives() { return relatives; }
    public String getBirthplace() { return birthplace; }
    public String getResidence() { return residence; }
    public String getStatus() { return status; }
    public String getOccupation() { return occupation; }
    public List<Grupo> getGroups() { return groups; }
    public List<String> getRoles() { return roles; }
    public List<String> getEpisodes() { return episodes; }

    public static String unir(List<String> lista) {
        if (lista == null || lista.isEmpty()) return "Desconocido";
        List<String> limpios = new ArrayList<>();
        for (String s : lista) {
            String val = texto(s);
            if (!val.equals("Desconocido")) limpios.add(val);
        }
        if (limpios.isEmpty()) return "Desconocido";
        return String.join(", ", limpios);
    }

    public static String texto(String valor) {
        if (valor == null || valor.trim().isEmpty()) return "Desconocido";
        if (valor.contains("http://") || valor.contains("https://")) return "Desconocido";
        String minus = valor.trim().toLowerCase();
        if (minus.equals("unknown") || minus.equals("none") || minus.equals("null") || minus.equals("n/a")) {
            return "Desconocido";
        }
        return valor.trim();
    }
}
