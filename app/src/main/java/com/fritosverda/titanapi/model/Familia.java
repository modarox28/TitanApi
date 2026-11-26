package com.fritosverda.titanapi.model;

import java.io.Serializable;
import java.util.List;

public class Familia implements Serializable {

    private String family;
    private List<String> members;

    public String getFamily() { return family; }
    public List<String> getMembers() { return members; }
}
