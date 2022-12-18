package com.example.diamondcircle.model;

import com.example.diamondcircle.MainController;


import java.util.*;

import static com.example.diamondcircle.logger.FileLogger.*;
import static com.example.diamondcircle.model.DiamondCircle.*;

public class DuhFigura extends Thread {

    private static DuhFigura instance = null;

    public DuhFigura() {

    }

    public static DuhFigura getInstance() {
        if (instance == null) {
            instance = new DuhFigura();
        }
        return instance;
    }

    Random random = new Random();

    public void run() {
        while (!krajSimulacije) {
            int broj = random.nextInt(dimenzije - 2) + 2;
            int i = 0;
            List<Polje> tempList=new LinkedList<Polje>();
            while (i < broj) {
                if(krajSimulacije)
                {
                    break;
                }
                synchronized (lock_pause) {
                    if (pauza) {
                        try {
                            lock_pause.wait();
                        } catch (InterruptedException e) {
                            log(e);
                        }
                    }
                }
                int element = random.nextInt(putanjaFigure.size());
                if(!putanjaFigure.get(element).isImaBonus() && !putanjaFigure.get(element).isImaRupa())
                {
                    putanjaFigure.get(element).setImaBonus(true);
                    i++;
                    tempList.add(putanjaFigure.get(element));
                    MainController.mc.postaviDiamond(putanjaFigure.get(element));
                }
            }
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                log(e);
            }

            ListIterator<Polje> iterator= tempList.listIterator();
            while (iterator.hasNext())
            {
                Polje p=iterator.next();
                int index=putanjaFigure.indexOf(p);
                putanjaFigure.get(index).setImaBonus(false);
                MainController.mc.skloniDiamond(p);
                iterator.remove();
            }
            MainController.mc.ocistiMapuDiamond();
        }
    }
}
