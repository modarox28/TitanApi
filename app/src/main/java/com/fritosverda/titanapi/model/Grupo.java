package com.fritosverda.titanapi.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {

    private String name;

    @SerializedName("sub_groups")
    private List<String> subGroups;

    public String getName() { return name; }
    public List<String> getSubGroups() { return subGroups; }
}
