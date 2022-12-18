package com.example.diamondcircle.model;


public class Polje {

    private Element element;
    private boolean imaBonus;
    private boolean imaRupa;
    private Figura figura;
    private boolean imaFigura;
    public Polje()
    {

    }

    public Polje(Element element)
    {
        this.element=element;
        imaBonus=false;
        imaRupa=false;
        figura=null;
        imaFigura=false;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public boolean isImaBonus() {
        return imaBonus;
    }

    public void setImaBonus(boolean imaBonus) {
        this.imaBonus = imaBonus;
    }

    public boolean isImaRupa() {
        return imaRupa;
    }

    public void setImaRupa(boolean imaRupa) {
        this.imaRupa = imaRupa;
    }

    public Figura getFigura() {
        return figura;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public boolean isImaFigura() {
        return imaFigura;
    }

    public void setImaFigura(boolean imaFigura) {
        this.imaFigura = imaFigura;
    }
}
