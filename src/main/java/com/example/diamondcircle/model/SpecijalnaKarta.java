package com.example.diamondcircle.model;


import com.example.diamondcircle.MainController;
import java.util.*;

import static com.example.diamondcircle.logger.FileLogger.*;
import static com.example.diamondcircle.model.DiamondCircle.*;

public class SpecijalnaKarta extends Karta {

    private int rupe;
    public SpecijalnaKarta()
    {

    }
    public SpecijalnaKarta(String putanja)
    {
        super(putanja);
        Random r=new Random();
        this.rupe=r.nextInt(dimenzije-2)+2;
    }

    public int getRupe() {
        return rupe;
    }

    public void setRupe(int rupe) {
        this.rupe = rupe;
    }

    public void kreirajRupe() {
        List<Polje> tempLista = new LinkedList<Polje>();
        Random random = new Random();
        int i = 0;
        while (i < this.rupe) {
            int z = random.nextInt(putanjaFigure.size());
            if (!putanjaFigure.get(z).isImaRupa() && !putanjaFigure.get(z).isImaBonus()) {
                MainController.mc.postaviRupu(putanjaFigure.get(z));
                putanjaFigure.get(z).setImaRupa(true);
                i++;
                tempLista.add(putanjaFigure.get(z));
                if (putanjaFigure.get(z).isImaFigura() && !(putanjaFigure.get(z).getFigura() instanceof LebdecaFigura))
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log(e);
                    }
                    putanjaFigure.get(z).getFigura().setZavrsilaKretanje(true);
                    putanjaFigure.get(z).setImaFigura(false);
                    putanjaFigure.get(z).setFigura(null);
                    MainController.mc.skloniFiguru(putanjaFigure.get(z));
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            log(e);
        }
        ListIterator<Polje> iterator = tempLista.listIterator();
        while (iterator.hasNext()) {
            Polje p = iterator.next();
            int index=putanjaFigure.indexOf(p);
            putanjaFigure.get(index).setImaRupa(false);
            MainController.mc.skloniRupu(p);
            iterator.remove();
        }
        MainController.mc.ocistiMapu();
    }

}

