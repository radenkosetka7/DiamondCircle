package com.example.diamondcircle.model;

import com.example.diamondcircle.MainController;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

import static com.example.diamondcircle.logger.FileLogger.*;


public class DiamondCircle {
    public static int dimenzije;
    public static int brojIgraca;
    public static List<Integer> selectedColor=new ArrayList<>();
    public static  Object lock_pause=new Object();
    public static Polje[][] mapa=new Polje[dimenzije][dimenzije];
    public static List<Polje> putanjaFigure=new LinkedList<>();
    public static boolean pauza=false;
    public static boolean krajSimulacije=false;
    public HashMap<Integer,Polje> mapper=new HashMap<>();

    private List<Karta> karte=new LinkedList<Karta>();
    private List<Igrac> igraci=new LinkedList<Igrac>();
    public long trajanjeIgre;
    private List<Figura> figure=new LinkedList<>();

    public Karta trenutnaKarta;
    private Igrac trenutniIgrac;

    public DiamondCircle()
    {
        setujPutanjuFigure();
        setujKarte();
        dodajIgrace();
        Collections.shuffle(igraci);
        Collections.shuffle(karte);
    }
    public List<Figura> getFigure()
    {
        return figure;
    }
    public List<Igrac> getIgraci()
    {
        return igraci;
    }
    public void dodajIgrace()
    {
        for(int i=0;i<brojIgraca;i++)
        {
            Igrac igrac=new Igrac();
            igraci.add(igrac);
            for(Figura f:igrac.getFigure())
            {
                figure.add(f);
            }
        }
    }

    public void pocetakIgre()
    {
        DuhFigura duhFigura=DuhFigura.getInstance();
        duhFigura.start();
        int pomeraj=0;
        List<Igrac> pomocniIgraci=new LinkedList<Igrac>(igraci);
        while(!krajSimulacije)
        {
            trenutnaKarta=karte.remove(0);
            MainController.mc.prikaziKartu(trenutnaKarta);
            if(trenutnaKarta instanceof ObicnaKarta)
            {
                pomeraj=((ObicnaKarta) trenutnaKarta).getPomjeraj();
                trenutniIgrac=pomocniIgraci.remove(0);
                trenutniIgrac.setBrojPolja(pomeraj);
                if(!trenutniIgrac.isKrenuo()) {
                    trenutniIgrac.start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        log(e);
                    }
                }
                synchronized (trenutniIgrac.player_lock)
                    {
                        trenutniIgrac.player_lock.notify();
                        try
                        {
                            trenutniIgrac.player_lock.wait();
                            if(!trenutniIgrac.isIgracZavrsio())
                            {
                                pomocniIgraci.add(trenutniIgrac);
                            }
                            else
                            {
                                if(pomocniIgraci.size()<1)
                                {
                                    krajSimulacije=true;
                                }
                            }
                        }
                        catch (Exception e) {
                            log(e);
                        }
                    }
            }
            else if(trenutnaKarta instanceof SpecijalnaKarta)
            {
                ((SpecijalnaKarta)trenutnaKarta).kreirajRupe();
                MainController.mc.prikaziSpecijalnuOpis(trenutnaKarta);
            }
            karte.add(trenutnaKarta);
        }
        ispisiRezultate();
        MainController.mc.start.setDisable(true);
        MainController.mc.pause.setDisable(true);

    }

    public boolean isPauza() {
        return pauza;
    }

    public void setPauza(boolean pauza) {
        this.pauza = pauza;
    }

    public void setPauzu()
    {
        synchronized (lock_pause)
        {
            if(!pauza) {
                lock_pause.notifyAll();
            }
        }

    }

    public void ispisiRezultate()
    {
        int id=1;
        String tip;
        String fileName= "src"+File.separator+"main"+File.separator+"java"+File.separator+
                "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"results"+ File.separator+String.format("IGRA_%d.txt",Calendar.getInstance().getTimeInMillis());
        try {
            PrintWriter printer = new PrintWriter(fileName);
            for(int i=0;i<igraci.size();i++)
            {
                String igrac="Igrac " + id + " - ";
                printer.println(igrac + igraci.get(i).getIme());
                id++;
                for(Figura figura:igraci.get(i).getFigure())
                {
                    if(figura instanceof ObicnaFigura)
                    {
                        tip="Obicna figura";
                    }
                    else if(figura instanceof SuperBrzaFigura)
                    {
                        tip="Super brza figura";
                    }
                    else
                    {
                        tip="Lebdeca figura";
                    }
                    StringBuilder builder=new StringBuilder();
                    for(int j=0;j<figura.getPredjenaPutanja().size();j++)
                    {
                        int k=j;
                        for (Map.Entry<Integer, Polje> entry : mapper.entrySet()) {
                            if (entry.getValue().equals(figura.getPredjenaPutanja().get(k))) {
                                builder.append(entry.getKey());
                                builder.append(" - ");
                            }
                        }
                    }
                    String cilj=figura.isStiglaDoCilja()? "da" : "ne";
                    System.out.println("\t"+ figura.getNaziv() + " ( "+
                            tip+" , "+figura.getBoja()+" ) - "  +"predjeni put"+" ( "+ builder+" ) " +
                            " stigla do cilja - " + cilj );
                    printer.println("\t"+ figura.getNaziv() + " ( "+
                                tip+" , "+figura.getBoja()+" ) -"  +"predjeni put "+" ( "+ builder+" ) " +
                            " stigla do cilja - " + cilj );
                }
                printer.println("");
                printer.println("");
            }
            printer.println("");
            printer.println("");
            printer.println("Ukupno vrijeme trajanja igre: " + trajanjeIgre + "[s]");
            printer.close();
        }
        catch (Exception e)
        {
            log(e);
        }
    }
    public void setujKarte()
    {
        for(int i=0;i<10;i++)
        {
            karte.add(new ObicnaKarta("src"+File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"assets"+File.separator+"1.png"));
            karte.add(new ObicnaKarta("src"+File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"assets"+File.separator+"2.png"));
            karte.add(new ObicnaKarta("src"+File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"assets"+File.separator+"3.png"));
            karte.add(new ObicnaKarta("src"+File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"assets"+File.separator+"4.png"));
        }

        for(int i=0;i<12;i++)
        {
            karte.add(new SpecijalnaKarta("src"+File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"assets"+File.separator+"joker.png"));
        }
    }

    public void setujPutanjuFigure()
    {

        if(dimenzije==7)
        {
            putanjaFigure.add(new Polje(new Element(0,3)));
            mapper.put(4,putanjaFigure.get(0));
            putanjaFigure.add(new Polje(new Element(1,4)));
            mapper.put(12,putanjaFigure.get(1));
            putanjaFigure.add(new Polje(new Element(2,5)));
            mapper.put(20,putanjaFigure.get(2));
            putanjaFigure.add(new Polje(new Element(3,6)));
            mapper.put(28,putanjaFigure.get(3));
            putanjaFigure.add(new Polje(new Element(4,5)));
            mapper.put(34,putanjaFigure.get(4));
            putanjaFigure.add(new Polje(new Element(5,4)));
            mapper.put(40,putanjaFigure.get(5));
            putanjaFigure.add(new Polje(new Element(6,3)));
            mapper.put(46,putanjaFigure.get(6));
            putanjaFigure.add(new Polje(new Element(5,2)));
            mapper.put(38,putanjaFigure.get(7));
            putanjaFigure.add(new Polje(new Element(4,1)));
            mapper.put(30,putanjaFigure.get(8));
            putanjaFigure.add(new Polje(new Element(3,0)));
            mapper.put(22,putanjaFigure.get(9));
            putanjaFigure.add(new Polje(new Element(2,1)));
            mapper.put(16,putanjaFigure.get(10));
            putanjaFigure.add(new Polje(new Element(1,2)));
            mapper.put(10,putanjaFigure.get(11));
            putanjaFigure.add(new Polje(new Element(1,3)));
            mapper.put(11,putanjaFigure.get(12));
            putanjaFigure.add(new Polje(new Element(2,4)));
            mapper.put(19,putanjaFigure.get(13));
            putanjaFigure.add(new Polje(new Element(3,5)));
            mapper.put(27,putanjaFigure.get(14));
            putanjaFigure.add(new Polje(new Element(4,4)));
            mapper.put(33,putanjaFigure.get(15));
            putanjaFigure.add(new Polje(new Element(5,3)));
            mapper.put(39,putanjaFigure.get(16));
            putanjaFigure.add(new Polje(new Element(4,2)));
            mapper.put(31,putanjaFigure.get(17));
            putanjaFigure.add(new Polje(new Element(3,1)));
            mapper.put(23,putanjaFigure.get(18));
            putanjaFigure.add(new Polje(new Element(2,2)));
            mapper.put(17,putanjaFigure.get(19));
            putanjaFigure.add(new Polje(new Element(2,3)));
            mapper.put(18,putanjaFigure.get(20));
            putanjaFigure.add(new Polje(new Element(3,4)));
            mapper.put(26,putanjaFigure.get(21));
            putanjaFigure.add(new Polje(new Element(4,3)));
            mapper.put(32,putanjaFigure.get(22));
            putanjaFigure.add(new Polje(new Element(3,2)));
            mapper.put(24,putanjaFigure.get(23));
            putanjaFigure.add(new Polje(new Element(3,3)));
            mapper.put(25,putanjaFigure.get(24));

        }
        else if(dimenzije==8)
        {
            putanjaFigure.add(new Polje(new Element(0,4)));
            mapper.put(5,putanjaFigure.get(0));
            putanjaFigure.add(new Polje(new Element(1,5)));
            mapper.put(14,putanjaFigure.get(1));
            putanjaFigure.add(new Polje(new Element(2,6)));
            mapper.put(23,putanjaFigure.get(2));
            putanjaFigure.add(new Polje(new Element(3,7)));
            mapper.put(32,putanjaFigure.get(3));
            putanjaFigure.add(new Polje(new Element(4,6)));
            mapper.put(39,putanjaFigure.get(4));
            putanjaFigure.add(new Polje(new Element(5,5)));
            mapper.put(46,putanjaFigure.get(5));
            putanjaFigure.add(new Polje(new Element(6,4)));
            mapper.put(53,putanjaFigure.get(6));
            putanjaFigure.add(new Polje(new Element(7,3)));
            mapper.put(60,putanjaFigure.get(7));
            putanjaFigure.add(new Polje(new Element(6,2)));
            mapper.put(51,putanjaFigure.get(8));
            putanjaFigure.add(new Polje(new Element(5,1)));
            mapper.put(42,putanjaFigure.get(9));
            putanjaFigure.add(new Polje(new Element(4,0)));
            mapper.put(33,putanjaFigure.get(10));
            putanjaFigure.add(new Polje(new Element(3,1)));
            mapper.put(26,putanjaFigure.get(11));
            putanjaFigure.add(new Polje(new Element(2,2)));
            mapper.put(19,putanjaFigure.get(12));
            putanjaFigure.add(new Polje(new Element(1,3)));
            mapper.put(12,putanjaFigure.get(13));
            putanjaFigure.add(new Polje(new Element(1,4)));
            mapper.put(13,putanjaFigure.get(14));
            putanjaFigure.add(new Polje(new Element(2,5)));
            mapper.put(22,putanjaFigure.get(15));
            putanjaFigure.add(new Polje(new Element(3,6)));
            mapper.put(31,putanjaFigure.get(16));
            putanjaFigure.add(new Polje(new Element(4,5)));
            mapper.put(38,putanjaFigure.get(17));
            putanjaFigure.add(new Polje(new Element(5,4)));
            mapper.put(45,putanjaFigure.get(18));
            putanjaFigure.add(new Polje(new Element(6,3)));
            mapper.put(52,putanjaFigure.get(19));
            putanjaFigure.add(new Polje(new Element(5,2)));
            mapper.put(43,putanjaFigure.get(20));
            putanjaFigure.add(new Polje(new Element(4,1)));
            mapper.put(34,putanjaFigure.get(21));
            putanjaFigure.add(new Polje(new Element(3,2)));
            mapper.put(27,putanjaFigure.get(22));
            putanjaFigure.add(new Polje(new Element(2,3)));
            mapper.put(20,putanjaFigure.get(23));
            putanjaFigure.add(new Polje(new Element(2,4)));
            mapper.put(21,putanjaFigure.get(24));
            putanjaFigure.add(new Polje(new Element(3,5)));
            mapper.put(30,putanjaFigure.get(25));
            putanjaFigure.add(new Polje(new Element(4,4)));
            mapper.put(37,putanjaFigure.get(26));
            putanjaFigure.add(new Polje(new Element(5,3)));
            mapper.put(44,putanjaFigure.get(27));
            putanjaFigure.add(new Polje(new Element(4,2)));
            mapper.put(35,putanjaFigure.get(28));
            putanjaFigure.add(new Polje(new Element(3,3)));
            mapper.put(28,putanjaFigure.get(29));
            putanjaFigure.add(new Polje(new Element(3,4)));
            mapper.put(29,putanjaFigure.get(30));
            putanjaFigure.add(new Polje(new Element(4,3)));
            mapper.put(36,putanjaFigure.get(31));
        }
        else if(dimenzije==9)
        {
            putanjaFigure.add(new Polje(new Element(0,4)));
            mapper.put(5,putanjaFigure.get(0));
            putanjaFigure.add(new Polje(new Element(1,5)));
            mapper.put(15,putanjaFigure.get(1));
            putanjaFigure.add(new Polje(new Element(2,6)));
            mapper.put(25,putanjaFigure.get(2));
            putanjaFigure.add(new Polje(new Element(3,7)));
            mapper.put(35,putanjaFigure.get(3));
            putanjaFigure.add(new Polje(new Element(4,8)));
            mapper.put(45,putanjaFigure.get(4));
            putanjaFigure.add(new Polje(new Element(5,7)));
            mapper.put(53,putanjaFigure.get(5));
            putanjaFigure.add(new Polje(new Element(6,6)));
            mapper.put(61,putanjaFigure.get(6));
            putanjaFigure.add(new Polje(new Element(7,5)));
            mapper.put(69,putanjaFigure.get(7));
            putanjaFigure.add(new Polje(new Element(8,4)));
            mapper.put(77,putanjaFigure.get(8));
            putanjaFigure.add(new Polje(new Element(7,3)));
            mapper.put(67,putanjaFigure.get(9));
            putanjaFigure.add(new Polje(new Element(6,2)));
            mapper.put(57,putanjaFigure.get(10));
            putanjaFigure.add(new Polje(new Element(5,1)));
            mapper.put(47,putanjaFigure.get(11));
            putanjaFigure.add(new Polje(new Element(4,0)));
            mapper.put(37,putanjaFigure.get(12));
            putanjaFigure.add(new Polje(new Element(3,1)));
            mapper.put(29,putanjaFigure.get(13));
            putanjaFigure.add(new Polje(new Element(2,2)));
            mapper.put(21,putanjaFigure.get(14));
            putanjaFigure.add(new Polje(new Element(1,3)));
            mapper.put(13,putanjaFigure.get(15));
            putanjaFigure.add(new Polje(new Element(1,4)));
            mapper.put(14,putanjaFigure.get(16));
            putanjaFigure.add(new Polje(new Element(2,5)));
            mapper.put(24,putanjaFigure.get(17));
            putanjaFigure.add(new Polje(new Element(3,6)));
            mapper.put(34,putanjaFigure.get(18));
            putanjaFigure.add(new Polje(new Element(4,7)));
            mapper.put(44,putanjaFigure.get(19));
            putanjaFigure.add(new Polje(new Element(5,6)));
            mapper.put(52,putanjaFigure.get(20));
            putanjaFigure.add(new Polje(new Element(6,5)));
            mapper.put(60,putanjaFigure.get(21));
            putanjaFigure.add(new Polje(new Element(7,4)));
            mapper.put(68,putanjaFigure.get(22));
            putanjaFigure.add(new Polje(new Element(6,3)));
            mapper.put(58,putanjaFigure.get(23));
            putanjaFigure.add(new Polje(new Element(5,2)));
            mapper.put(48,putanjaFigure.get(24));
            putanjaFigure.add(new Polje(new Element(4,1)));
            mapper.put(38,putanjaFigure.get(25));
            putanjaFigure.add(new Polje(new Element(3,2)));
            mapper.put(30,putanjaFigure.get(26));
            putanjaFigure.add(new Polje(new Element(2,3)));
            mapper.put(22,putanjaFigure.get(27));
            putanjaFigure.add(new Polje(new Element(2,4)));
            mapper.put(23,putanjaFigure.get(28));
            putanjaFigure.add(new Polje(new Element(3,5)));
            mapper.put(33,putanjaFigure.get(29));
            putanjaFigure.add(new Polje(new Element(4,6)));
            mapper.put(43,putanjaFigure.get(30));
            putanjaFigure.add(new Polje(new Element(5,5)));
            mapper.put(51,putanjaFigure.get(31));
            putanjaFigure.add(new Polje(new Element(6,4)));
            mapper.put(59,putanjaFigure.get(32));
            putanjaFigure.add(new Polje(new Element(5,3)));
            mapper.put(49,putanjaFigure.get(33));
            putanjaFigure.add(new Polje(new Element(4,2)));
            mapper.put(39,putanjaFigure.get(34));
            putanjaFigure.add(new Polje(new Element(3,3)));
            mapper.put(31,putanjaFigure.get(35));
            putanjaFigure.add(new Polje(new Element(3,4)));
            mapper.put(32,putanjaFigure.get(36));
            putanjaFigure.add(new Polje(new Element(4,5)));
            mapper.put(42,putanjaFigure.get(37));
            putanjaFigure.add(new Polje(new Element(5,4)));
            mapper.put(50,putanjaFigure.get(38));
            putanjaFigure.add(new Polje(new Element(4,3)));
            mapper.put(40,putanjaFigure.get(39));
            putanjaFigure.add(new Polje(new Element(4,4)));
            mapper.put(41,putanjaFigure.get(40));
        }
        else if(dimenzije==10)
        {
            putanjaFigure.add(new Polje(new Element(0,5)));
            mapper.put(6,putanjaFigure.get(0));
            putanjaFigure.add(new Polje(new Element(1,6)));
            mapper.put(17,putanjaFigure.get(1));
            putanjaFigure.add(new Polje(new Element(2,7)));
            mapper.put(28,putanjaFigure.get(2));
            putanjaFigure.add(new Polje(new Element(3,8)));
            mapper.put(39,putanjaFigure.get(3));
            putanjaFigure.add(new Polje(new Element(4,9)));
            mapper.put(50,putanjaFigure.get(4));
            putanjaFigure.add(new Polje(new Element(5,8)));
            mapper.put(59,putanjaFigure.get(5));
            putanjaFigure.add(new Polje(new Element(6,7)));
            mapper.put(68,putanjaFigure.get(6));
            putanjaFigure.add(new Polje(new Element(7,6)));
            mapper.put(77,putanjaFigure.get(7));
            putanjaFigure.add(new Polje(new Element(8,5)));
            mapper.put(86,putanjaFigure.get(8));
            putanjaFigure.add(new Polje(new Element(9,4)));
            mapper.put(95,putanjaFigure.get(9));
            putanjaFigure.add(new Polje(new Element(8,3)));
            mapper.put(84,putanjaFigure.get(10));
            putanjaFigure.add(new Polje(new Element(7,2)));
            mapper.put(73,putanjaFigure.get(11));
            putanjaFigure.add(new Polje(new Element(6,1)));
            mapper.put(62,putanjaFigure.get(12));
            putanjaFigure.add(new Polje(new Element(5,0)));
            mapper.put(51,putanjaFigure.get(13));
            putanjaFigure.add(new Polje(new Element(4,1)));
            mapper.put(42,putanjaFigure.get(14));
            putanjaFigure.add(new Polje(new Element(3,2)));
            mapper.put(33,putanjaFigure.get(15));
            putanjaFigure.add(new Polje(new Element(2,3)));
            mapper.put(24,putanjaFigure.get(16));
            putanjaFigure.add(new Polje(new Element(1,4)));
            mapper.put(15,putanjaFigure.get(17));
            putanjaFigure.add(new Polje(new Element(1,5)));
            mapper.put(16,putanjaFigure.get(18));
            putanjaFigure.add(new Polje(new Element(2,6)));
            mapper.put(27,putanjaFigure.get(19));
            putanjaFigure.add(new Polje(new Element(3,7)));
            mapper.put(38,putanjaFigure.get(20));
            putanjaFigure.add(new Polje(new Element(4,8)));
            mapper.put(49,putanjaFigure.get(21));
            putanjaFigure.add(new Polje(new Element(5,7)));
            mapper.put(58,putanjaFigure.get(22));
            putanjaFigure.add(new Polje(new Element(6,6)));
            mapper.put(67,putanjaFigure.get(23));
            putanjaFigure.add(new Polje(new Element(7,5)));
            mapper.put(76,putanjaFigure.get(24));
            putanjaFigure.add(new Polje(new Element(8,4)));
            mapper.put(85,putanjaFigure.get(25));
            putanjaFigure.add(new Polje(new Element(7,3)));
            mapper.put(74,putanjaFigure.get(26));
            putanjaFigure.add(new Polje(new Element(6,2)));
            mapper.put(63,putanjaFigure.get(27));
            putanjaFigure.add(new Polje(new Element(4,1)));
            mapper.put(52,putanjaFigure.get(28));
            putanjaFigure.add(new Polje(new Element(4,2)));
            mapper.put(43,putanjaFigure.get(29));
            putanjaFigure.add(new Polje(new Element(3,3)));
            mapper.put(34,putanjaFigure.get(30));
            putanjaFigure.add(new Polje(new Element(2,4)));
            mapper.put(25,putanjaFigure.get(31));
            putanjaFigure.add(new Polje(new Element(2,5)));
            mapper.put(26,putanjaFigure.get(32));
            putanjaFigure.add(new Polje(new Element(3,6)));
            mapper.put(37,putanjaFigure.get(33));
            putanjaFigure.add(new Polje(new Element(4,7)));
            mapper.put(48,putanjaFigure.get(34));
            putanjaFigure.add(new Polje(new Element(5,6)));
            mapper.put(57,putanjaFigure.get(35));
            putanjaFigure.add(new Polje(new Element(6,5)));
            mapper.put(66,putanjaFigure.get(36));
            putanjaFigure.add(new Polje(new Element(7,4)));
            mapper.put(75,putanjaFigure.get(37));
            putanjaFigure.add(new Polje(new Element(6,3)));
            mapper.put(64,putanjaFigure.get(38));
            putanjaFigure.add(new Polje(new Element(5,2)));
            mapper.put(53,putanjaFigure.get(39));
            putanjaFigure.add(new Polje(new Element(4,3)));
            mapper.put(44,putanjaFigure.get(40));
            putanjaFigure.add(new Polje(new Element(3,4)));
            mapper.put(35,putanjaFigure.get(41));
            putanjaFigure.add(new Polje(new Element(3,5)));
            mapper.put(36,putanjaFigure.get(42));
            putanjaFigure.add(new Polje(new Element(4,6)));
            mapper.put(47,putanjaFigure.get(43));
            putanjaFigure.add(new Polje(new Element(5,5)));
            mapper.put(56,putanjaFigure.get(44));
            putanjaFigure.add(new Polje(new Element(6,4)));
            mapper.put(65,putanjaFigure.get(45));
            putanjaFigure.add(new Polje(new Element(5,3)));
            mapper.put(54,putanjaFigure.get(46));
            putanjaFigure.add(new Polje(new Element(4,4)));
            mapper.put(45,putanjaFigure.get(47));
            putanjaFigure.add(new Polje(new Element(4,5)));
            mapper.put(46,putanjaFigure.get(48));
            putanjaFigure.add(new Polje(new Element(5,4)));
            mapper.put(55,putanjaFigure.get(49));
        }
    }
}
