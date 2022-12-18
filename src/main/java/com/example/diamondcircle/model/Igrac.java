package com.example.diamondcircle.model;

import com.example.diamondcircle.MainController;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.diamondcircle.logger.FileLogger.*;
import static com.example.diamondcircle.model.DiamondCircle.*;

public class Igrac extends Thread {

    private static int id=1;
    private String ime;
    private List<Figura> figure=new ArrayList<Figura>();
    private Figura trenutnaFigura;
    private boolean igracZavrsio;
    private boolean krenuo;
    private int brojPolja;
    public Object player_lock=new Object();
    public Igrac()
    {
        ime="Player"+id;
        id++;
        this.igracZavrsio=false;
        Random random=new Random();
        int boja;
        while(true) {
            boja = random.nextInt(4);
            if(selectedColor.contains(boja))
            {
                continue;
            }
            else
            {
                break;
            }
        }
        selectedColor.add(boja);
        while(true)
        {
            int x=random.nextInt(3);
            if(figure.size()==4)
            {
                break;
            }
            if(x==0)
            {
                ObicnaFigura obicna=new ObicnaFigura(boja);
                figure.add(obicna);
            }
            else if(x==1)
            {
                LebdecaFigura lebdeca=new LebdecaFigura(boja);
                figure.add(lebdeca);
            }
            else if(x==2)
            {
                SuperBrzaFigura brza=new SuperBrzaFigura(boja);
                figure.add(brza);
            }
        }
        this.krenuo=false;
    }
    public Object getFigura(Igrac igrac)
    {
        return igrac.getFigure().stream().filter(e->!e.isZavrsilaKretanje() && !e.isStiglaDoCilja()).
                findFirst().orElse(null);
    }

    public void run()
    {
        krenuo=true;
        synchronized (player_lock)
        {
            while(true)
            {
                try {
                    player_lock.wait();
                } catch (InterruptedException e) {
                    log(e);
                }
                Object temp=getFigura(this);
                if(temp!=null) {
                    trenutnaFigura = (Figura) temp;
                    trenutnaFigura.setUkupniPomjeraj(brojPolja);
                    trenutnaFigura.setOdredisnoPolje(trenutnaFigura);
                     MainController.mc.prikaziDetaljanOpis(this,trenutnaFigura);
                    if (!trenutnaFigura.isKrenula()) {
                        trenutnaFigura.start();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            log(e);
                        }
                    }
                    synchronized (trenutnaFigura.figure_lock) {
                        trenutnaFigura.figure_lock.notify();
                        try {
                            trenutnaFigura.figure_lock.wait();
                        } catch (InterruptedException e) {
                            log(e);
                        }
                    }
                    player_lock.notify();
                }
                else
                {
                    player_lock.notify();
                    break;
                }
            }
        }
        igracZavrsio=true;
    }

    public Figura getTrenutnaFigura() {
        return trenutnaFigura;
    }

    public void setTrenutnaFigura(Figura trenutnaFigura) {
        this.trenutnaFigura = trenutnaFigura;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public List<Figura> getFigure() {
        return figure;
    }

    public void setFigure(CopyOnWriteArrayList<Figura> figure) {
        this.figure = figure;
    }

    public boolean isKrenuo() {
        return krenuo;
    }

    public void setKrenuo(boolean krenuo) {
        this.krenuo = krenuo;
    }

    public int getBrojPolja() {
        return brojPolja;
    }

    public void setBrojPolja(int brojPolja) {
        this.brojPolja = brojPolja;
    }

    @Override
    public String toString()
    {
        return "Igrac: " + ime + "\n Figure: " + figure;
    }

    public boolean isIgracZavrsio() {
        return igracZavrsio;
    }

    public void setIgracZavrsio(boolean igracZavrsio) {
        this.igracZavrsio = igracZavrsio;
    }
}
