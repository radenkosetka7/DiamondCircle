package com.example.diamondcircle.model;


import com.example.diamondcircle.MainController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.diamondcircle.logger.FileLogger.*;
import static com.example.diamondcircle.model.DiamondCircle.*;

public abstract class Figura extends Thread {

    private BojaFigure boja;
    private String naziv;
    private static int id=1;
    private Polje trenutnoPolje;
    private boolean stiglaDoCilja=false;
    private List<Polje> predjenaPutanja=new ArrayList<Polje>();
    private int ukupniPomjeraj;
    private long vrijemeKretanja;
    private long pocetakMjerenja;
    private int bonus;
    private boolean zavrsilaKretanje=false;
    private Polje pocetnoPolje;
    private boolean krenula=false;
    private Polje odredisnoPolje;
    public Object figure_lock=new Object();

    public Figura(int x)
    {
        naziv="Figura"+id;
        if(x==0)
        {
            this.boja=BojaFigure.CRVENA;
        }
        else if(x==1)
        {
            this.boja=BojaFigure.PLAVA;
        }
        else if(x==2)
        {
            this.boja=BojaFigure.ZELENA;
        }
        else if(x==3)
        {
            this.boja=BojaFigure.ZUTA;
        }
        id++;
        this.bonus=0;
        pocetnoPolje=putanjaFigure.get(0);
        trenutnoPolje=pocetnoPolje;
    }
    public void zapocniMjerenjeVremena()
    {
        pocetakMjerenja=Calendar.getInstance().getTimeInMillis();
    }
    public Polje getNarednoPolje(Polje polje)
    {
        int index=putanjaFigure.indexOf(polje);
        return putanjaFigure.get(index+1);
    }
    public void run() {
        try {
            krenula = true;
            predjenaPutanja.add(trenutnoPolje);
            int index = putanjaFigure.indexOf(trenutnoPolje);
            putanjaFigure.get(index).setFigura(this);
            putanjaFigure.get(index).setImaFigura(true);
            MainController.mc.postaviFiguru(trenutnoPolje, this);
            synchronized (figure_lock) {
                while (!trenutnoPolje.equals(putanjaFigure.get(putanjaFigure.size() - 1))) {
                    if (krenula) {
                        zapocniMjerenjeVremena();
                    }
                    try {
                        figure_lock.wait();
                    } catch (Exception e) {
                        log(e);
                    }
                    while (trenutnoPolje != odredisnoPolje) {
                        synchronized (lock_pause) {
                            if (pauza) {
                                try {
                                    lock_pause.wait();
                                } catch (InterruptedException e) {
                                    log(e);
                                }
                            }
                        }

                        int ib=putanjaFigure.indexOf(trenutnoPolje);
                        if (putanjaFigure.get(ib).isImaBonus()) {
                            bonus++;
                            putanjaFigure.get(ib).setImaBonus(false);
                            MainController.mc.skloniDiamond(putanjaFigure.get(ib));
                        }
                        Polje naredno = getNarednoPolje(trenutnoPolje);
                        int tmp = putanjaFigure.indexOf(naredno);
                        if (putanjaFigure.get(tmp).isImaFigura()) {
                            int in = putanjaFigure.indexOf(trenutnoPolje);
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                log(e);
                            }
                            MainController.mc.skloniFiguru(trenutnoPolje);
                            putanjaFigure.get(in).setFigura(null);
                            putanjaFigure.get(in).setImaFigura(false);
                            trenutnoPolje = naredno;
                            predjenaPutanja.add(trenutnoPolje);
                            int it=putanjaFigure.indexOf(trenutnoPolje);
                            if (putanjaFigure.get(it).isImaBonus()) {
                                bonus++;
                                putanjaFigure.get(it).setImaBonus(false);
                                MainController.mc.skloniDiamond(putanjaFigure.get(it));
                            }
                            int i = 1;
                            while (true) {
                                Polje helped = getNarednoPolje(trenutnoPolje);
                                trenutnoPolje = helped;
                                predjenaPutanja.add(trenutnoPolje);
                                int tmpIndex = putanjaFigure.indexOf(trenutnoPolje);
                                if (putanjaFigure.get(tmpIndex).isImaBonus()) {
                                    bonus++;
                                    putanjaFigure.get(tmpIndex).setImaBonus(false);
                                    MainController.mc.skloniDiamond(putanjaFigure.get(tmpIndex));
                                }
                                if (putanjaFigure.get(tmpIndex).isImaFigura()) {
                                    i++;
                                    continue;
                                } else {
                                    break;
                                }
                            }
                            try {
                                Thread.sleep(1000 * i);
                            } catch (Exception e) {
                                log(e);
                            }
                            int ind = putanjaFigure.indexOf(trenutnoPolje);
                            putanjaFigure.get(ind).setFigura(this);
                            putanjaFigure.get(ind).setImaFigura(true);
                            predjenaPutanja.add(trenutnoPolje);
                            if (putanjaFigure.get(ind).isImaBonus()) {
                                bonus++;
                                putanjaFigure.get(ind).setImaBonus(false);
                                MainController.mc.skloniDiamond(putanjaFigure.get(ind));
                            }
                            MainController.mc.postaviFiguru(trenutnoPolje, this);
                            continue;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            log(e);
                        }
                        int in = putanjaFigure.indexOf(trenutnoPolje);
                        putanjaFigure.get(in).setFigura(null);
                        putanjaFigure.get(in).setImaFigura(false);
                        MainController.mc.skloniFiguru(trenutnoPolje);
                        trenutnoPolje = naredno;
                        predjenaPutanja.add(trenutnoPolje);
                        int ind = putanjaFigure.indexOf(trenutnoPolje);
                        putanjaFigure.get(ind).setFigura(this);
                        putanjaFigure.get(ind).setImaFigura(true);
                        MainController.mc.postaviFiguru(trenutnoPolje, this);
                    }
                    long end = Calendar.getInstance().getTimeInMillis();
                    vrijemeKretanja += (end - pocetakMjerenja);
                    if (trenutnoPolje.equals(putanjaFigure.get(putanjaFigure.size() - 1))) {
                        stiglaDoCilja = true;
                        zavrsilaKretanje = true;
                        int iz=putanjaFigure.indexOf(trenutnoPolje);
                        putanjaFigure.get(iz).setImaFigura(false);
                        putanjaFigure.get(iz).setFigura(null);
                        MainController.mc.skloniFiguru(putanjaFigure.get(iz));
                    } else {
                        figure_lock.notify();

                    }
                }
                figure_lock.notify();
            }
        }
        catch (Exception exception)
        {
            log(exception);
        }
    }


    public BojaFigure getBoja() {
        return boja;
    }


    public String getNaziv() {
        return naziv;
    }


    @Override
    public String toString()
    {
        return "Figura: " + naziv;
    }

    public Polje pronadjiSlobodnoPolje(int index,Figura figura)
    {
        Polje p=putanjaFigure.stream().skip(index).filter(e->!e.isImaFigura()).findFirst().get();
        int num=putanjaFigure.indexOf(p)-putanjaFigure.indexOf(odredisnoPolje);
        if(figura instanceof Brzina)
        {
            setUkupniPomjeraj((num + getUkupniPomjeraj())/2);

        }
        else
        {
            setUkupniPomjeraj(num + getUkupniPomjeraj());
        }
        return p;
    }

    public Polje getTrenutnoPolje() {
        return trenutnoPolje;
    }

    public boolean isStiglaDoCilja() {
        return stiglaDoCilja;
    }

    public List<Polje> getPredjenaPutanja() {
        return predjenaPutanja;
    }

    public int getUkupniPomjeraj() {
        return this.ukupniPomjeraj;
    }

    public void setUkupniPomjeraj(int ukupniPomjeraj) {
        if(this instanceof Brzina)
        {
            if(bonus>0)
            {
                this.ukupniPomjeraj =(ukupniPomjeraj+bonus)*2;
                bonus=0;
            }
            else
            {
                this.ukupniPomjeraj =(ukupniPomjeraj)*2;
            }
        }
        else
        {
            if(bonus>0)
            {
                this.ukupniPomjeraj =(ukupniPomjeraj+bonus);
                bonus=0;
            }
            else
            {
                this.ukupniPomjeraj =ukupniPomjeraj;
            }
        }

    }
    public boolean isZavrsilaKretanje() {
        return zavrsilaKretanje;
    }

    public void setZavrsilaKretanje(boolean zavrsilaKretanje) {
        this.zavrsilaKretanje = zavrsilaKretanje;
    }

    public boolean isKrenula() {
        return krenula;
    }

    public Polje getOdredisnoPolje() {
        return odredisnoPolje;
    }

    public void setOdredisnoPolje(Figura figura) {
        int index=putanjaFigure.indexOf(trenutnoPolje);
        if(index+getUkupniPomjeraj() >= putanjaFigure.size())
        {
            this.odredisnoPolje=putanjaFigure.get(putanjaFigure.size()-1);
        }
        else
        {
            this.odredisnoPolje=putanjaFigure.get((index+getUkupniPomjeraj()));
        }
        if(odredisnoPolje.isImaFigura())
        {
            this.odredisnoPolje=pronadjiSlobodnoPolje(index+getUkupniPomjeraj(),figura);
        }
    }
}
