package com.example.diamondcircle;

import com.example.diamondcircle.model.Figura;
import com.example.diamondcircle.model.Polje;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.diamondcircle.MainController.col1;
import static com.example.diamondcircle.MainController.row1;
import static com.example.diamondcircle.model.DiamondCircle.dimenzije;

public class Figure implements Initializable {


    public GridPane figurePane=new GridPane();

    public static Figure fig;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        for (int i = 0; i < dimenzije; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(col1);
            col.setPrefWidth(col1);
            figurePane.getColumnConstraints().add(col);
        }
        for (int i = 0; i < dimenzije; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(row1);
            row.setPrefHeight(row1);
            figurePane.getRowConstraints().add(row);
        }
        int content=1;
        for (int i = 0; i < dimenzije; i++) {
            for (int j = 0; j < dimenzije; j++) {
                Text text = new Text(" " + String.valueOf(content));
                text.setStyle("-fx-text-alignment: center");
                figurePane.add(text, j, i);
                content++;
            }
        }
        fig=this;

    }

    public void prikaziPredjeniPut(Figura figura)
    {
        for(Polje p:figura.getPredjenaPutanja())
        {
            Rectangle rec=new Rectangle(row1,col1);
            rec.setFill(Color.CYAN);
            Platform.runLater(()->
            {
                figurePane.add(rec,p.getElement().getY(),p.getElement().getX());
            });
        }
    }
}
