package com.example.diamondcircle.model;

public class ObicnaKarta extends Karta{


    private int pomjeraj;
    public ObicnaKarta()
    {

    }

    public ObicnaKarta(String putanja)
    {
        super(putanja);
        if(putanja.endsWith("1.png"))
        {
            this.pomjeraj=1;
        }
        else if(putanja.endsWith("2.png"))
        {
            this.pomjeraj=2;
        }
        else if(putanja.endsWith("3.png"))
        {
            this.pomjeraj=3;
        }
        else
        {
            this.pomjeraj=4;
        }
    }

    public int getPomjeraj() {
        return pomjeraj;
    }

    public void setPomjeraj(int pomjeraj) {
        this.pomjeraj = pomjeraj;
    }
}
