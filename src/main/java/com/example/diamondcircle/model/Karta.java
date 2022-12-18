package com.example.diamondcircle.model;

public abstract class Karta {

    private String putanjaSlike;

    public Karta()
    {

    }

    public Karta(String putanja)
    {
        this.putanjaSlike=putanja;
    }

    public String getPutanjaSlike() {
        return putanjaSlike;
    }

    public void setPutanjaSlike(String putanjaSlike) {
        this.putanjaSlike = putanjaSlike;
    }
}
