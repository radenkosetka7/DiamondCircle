package com.example.diamondcircle;

import com.example.diamondcircle.model.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.*;
import java.net.URL;
import java.util.*;


import static com.example.diamondcircle.Figure.fig;
import static com.example.diamondcircle.logger.FileLogger.*;
import static com.example.diamondcircle.model.DiamondCircle.*;

public class MainController implements Initializable {


    @FXML
    public  GridPane Matrica=new GridPane();
    @FXML
    public Label brojIgara=new Label();
    @FXML
    public Button start;
    @FXML
    public Button pause;
    @FXML
    public GridPane players=new GridPane();
    @FXML
    public ListView<String> figuresList=new ListView<>();
    @FXML
    public Label cardDescription= new Label();
    @FXML
    public Label time= new Label();
    @FXML
    public Label currentCard= new Label();
    @FXML
    public Button results;
    @FXML
    public TextArea description= new TextArea();
    @FXML
    public ImageView TreutnaKarta=new ImageView();

    public static Object lock=new Object();
    public static MainController mc;
    private static int counter=0;
    public static int row1;
    public static int col1;
    public static DiamondCircle diamondCircle;
    public static StringBuilder builder= new StringBuilder();

    public void setLabelText(Label label)
    {
        File path=new File("src"+File.separator+"main"+File.separator+"java"+File.separator+
                "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"results");
        int count = path.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.endsWith(".txt") && name.startsWith("IGRA"));
            }
        }).length;
        label.setText("Trenutni broj odigranih igara: "+String.valueOf(count));
    }


    public void postaviDiamond(Polje p1)
    {
        Circle circle=new Circle(10,Color.DEEPPINK);
        Platform.runLater(()->Matrica.add(circle,p1.getElement().getY(),p1.getElement().getX()));
    }
    public void skloniDiamond(Polje p1)
    {
        try {
            Node currentNode = null;
            ObservableList<Node> childrens = Matrica.getChildren();
            for (Node node : childrens) {
                if (node instanceof Circle && Matrica.getRowIndex(node) == p1.getElement().getX() && Matrica.getColumnIndex(node) == p1.getElement().getY()) {
                    currentNode = node;
                }
            }
            synchronized (lock) {
                Node finalCurrentNode = currentNode;
                if (finalCurrentNode != null) {
                    Platform.runLater(() -> Matrica.getChildren().remove(finalCurrentNode));
                }
            }
        }
        catch (Exception e)
        {
            log(e);
        }
    }
    @FXML
    public void skloniRupu(Polje p1) {
        try {
            Node currentNode = null;
            ObservableList<Node> childrens = Matrica.getChildren();
            for (Node node : childrens) {
                if (node instanceof Ellipse && Matrica.getRowIndex(node) == p1.getElement().getX() && Matrica.getColumnIndex(node) == p1.getElement().getY()) {
                    currentNode = node;
                }
            }
            synchronized (lock) {
                Node finalCurrentNode = currentNode;
                if (finalCurrentNode != null) {
                    Platform.runLater(() -> Matrica.getChildren().remove(finalCurrentNode));
                }
            }
        }
        catch (Exception e)
        {
            log(e);
        }

    }

    public void ocistiMapu()
    {
        try {
            Node currentNode = null;
            ObservableList<Node> childrens = Matrica.getChildren();
            for (Node node : childrens) {
                if (node instanceof Ellipse) {
                    currentNode = node;
                }
                synchronized (lock) {
                    Node finalCurrentNode = currentNode;
                    if (finalCurrentNode != null) {
                        Platform.runLater(() -> Matrica.getChildren().remove(finalCurrentNode));
                    }
                }
            }
        }
        catch (Exception e)
        {
            log(e);
        }

    }
    public void ocistiMapuDiamond()
    {
        try {
            Node currentNode = null;
            ObservableList<Node> childrens = Matrica.getChildren();
            for (Node node : childrens) {
                if (node instanceof Circle) {
                    currentNode = node;
                }
                synchronized (lock) {
                    Node finalCurrentNode = currentNode;
                    if (finalCurrentNode != null) {
                        Platform.runLater(() -> Matrica.getChildren().remove(finalCurrentNode));
                    }
                }
            }
        }
        catch (Exception e)
        {
            log(e);
        }

    }
    @FXML
    public void postaviRupu(Polje p1)
    {
        Ellipse elipse=new Ellipse(15,15);
        elipse.setFill(Color.BLACK);
        Platform.runLater(()->Matrica.add(elipse,p1.getElement().getY(),p1.getElement().getX()));
    }

    @FXML
    public void postaviFiguru(Polje p1,Figura figura)
    {
        Rectangle rectangle=new Rectangle(row1,col1);
        Label label=new Label();
        if(figura.getBoja().equals(BojaFigure.CRVENA))
        {
            rectangle.setFill(Color.RED);
            if(figura instanceof ObicnaFigura)
            {
                label.setText("OF");
            }
            else if(figura instanceof SuperBrzaFigura)
            {
                label.setText("SF");
            }
            else
            {
                label.setText("LF");
            }
        }
        else if(figura.getBoja().equals(BojaFigure.ZELENA))
        {
            rectangle.setFill(Color.GREEN);
            if(figura instanceof ObicnaFigura)
            {
                label.setText("OF");
            }
            else if(figura instanceof SuperBrzaFigura)
            {
                label.setText("SF");

            }
            else
            {
                label.setText("LF");
            }
        }
        else if(figura.getBoja().equals(BojaFigure.PLAVA))
        {
            rectangle.setFill(Color.BLUE);
            if(figura instanceof ObicnaFigura)
            {
                label.setText("OF");
            }
            else if(figura instanceof SuperBrzaFigura)
            {
                label.setText("SF");
            }
            else
            {
                label.setText("LF");
            }
        }
        else
        {
            rectangle.setFill(Color.YELLOW);
            if(figura instanceof ObicnaFigura)
            {
                label.setText("OF");
            }
            else if(figura instanceof SuperBrzaFigura)
            {
                label.setText("SF");
            }
            else
            {
                label.setText("LF");
            }
        }
        Platform.runLater(()->
        {
        Matrica.add(rectangle,p1.getElement().getY(),p1.getElement().getX());
        Matrica.add(label,p1.getElement().getY(),p1.getElement().getX());
        });
    }

    @FXML
    public void skloniFiguru(Polje p)
    {
        try {
            Node currentNode = null;
            Node currentNode1 = null;
            ObservableList<Node> childrens = Matrica.getChildren();
            for (Node node : childrens) {
                if (node instanceof Rectangle && Matrica.getRowIndex(node) == p.getElement().getX() && Matrica.getColumnIndex(node) == p.getElement().getY()) {
                    currentNode = node;
                } else if (node instanceof Label && Matrica.getRowIndex(node) == p.getElement().getX() && Matrica.getColumnIndex(node) == p.getElement().getY()) {
                    currentNode1 = node;
                }
            }
            synchronized (lock) {
                Node finalCurrentNode = currentNode;
                Node finalCurrentNode1 = currentNode1;
                if (finalCurrentNode != null) {
                    Platform.runLater(() -> Matrica.getChildren().remove(finalCurrentNode));
                }
                if (finalCurrentNode1 != null) {
                    Platform.runLater(() -> Matrica.getChildren().remove(finalCurrentNode1));
                }
            }
        }
        catch (Exception e)
        {
            log(e);
        }
    }
    public Thread trajanjeIgre()
    {
        return new Thread(() ->
        {
            int s=0;
            while(!diamondCircle.krajSimulacije)
            {
                if(!diamondCircle.isPauza()) {
                    diamondCircle.trajanjeIgre=s;
                    String time1 = s + " [s]";
                    Platform.runLater(() -> time.setText("Vrijeme trajanja igre: " + time1));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        log(e);
                    }
                    s++;
                }
                else
                {
                    System.out.print("");
                }
            }
        });
    }

    public void prikaziKartu(Karta karta)
    {
        File file=new File(karta.getPutanjaSlike());
        Image image=new Image(file.toURI().toString(),300,300,false,false);
        Platform.runLater(() -> TreutnaKarta.setImage(image));

    }
    public void prikaziSpecijalnuOpis(Karta trenutnaKarta)
    {
        builder.append("Specijalna karta, kreirano: " +((SpecijalnaKarta)trenutnaKarta).getRupe() + " rupa.");
        String x=builder.toString();
        Platform.runLater(() -> description.setText(x));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log(e);
        }
        builder.delete(0,builder.length());
    }
    public void prikaziOpis(Karta trenutnaKarta,Igrac trenutniIgrac)
    {
        builder.append("Na potezu je " + trenutniIgrac.getIme() + " , ");
    }
    public void prikaziDetaljanOpis(Igrac igrac,Figura trenutnaFigura)
    {
        builder.append("Na potezu je " + igrac.getIme() + " , ");
        int x=0,y=0;
        for(Map.Entry<Integer,Polje> temp:diamondCircle.mapper.entrySet())
        {
            if(temp.getValue().equals(trenutnaFigura.getTrenutnoPolje()))
            {
                x=temp.getKey();
            }
            else if(temp.getValue().equals(trenutnaFigura.getOdredisnoPolje()))
            {
                y=temp.getKey();
            }
        }
        builder.append(trenutnaFigura.getNaziv() + " prelazi " + trenutnaFigura.getUkupniPomjeraj() + " polja, sa pozicije " );
        builder.append( x+ " na poziciju ");
        builder.append( y+ " .");
        String z=builder.toString();
        Platform.runLater(() -> description.setText(z));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log(e);
        }
        builder.delete(0,builder.length());
    }

    @FXML
    protected void pokreniSimulaciju()
    {

        if(counter==0) {
            Thread vrijeme=trajanjeIgre();
            vrijeme.start();
            new Thread(()->diamondCircle.pocetakIgre()).start();
            counter++;
        }
        start.setVisible(false);
        pause.setVisible(true);
        diamondCircle.setPauza(false);
        diamondCircle.setPauzu();
    }

    public void setSizes()
    {
        if(dimenzije==7)
        {
            row1=45;
            col1=45;
        }
        else if(dimenzije==8)
        {
            row1=40;
            col1=40;
        }
        else if(dimenzije==9)
        {
            row1=35;
            col1=35;
        }
        else if(dimenzije==10)
        {
            row1=30;
            col1=30;
        }
    }

    public void pauzirajSimulaciju(ActionEvent actionEvent) {

        pause.setVisible(false);
        start.setVisible(true);
        diamondCircle.setPauza(true);
        diamondCircle.setPauzu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        int dimenzija;
        int brojIgraca;
        try {
            Properties p = new Properties();
            FileInputStream stream = new FileInputStream("src"+ File.separator+"main"+File.separator+"java"+File.separator+
                    "com"+File.separator+"example"+File.separator+"diamondcircle"+File.separator+"configuration"+File.separator+"config.properties");
            p.load(stream);
            brojIgraca = Integer.parseInt(p.getProperty("Broj_igraca"));
            dimenzija = Integer.parseInt(p.getProperty("Dimenzija"));

            if ((dimenzija < 7 || dimenzija > 10) || (brojIgraca < 2 || brojIgraca > 4)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Neispavan unos!");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
                System.exit(0);
            } else {
                dimenzije = dimenzija;
                DiamondCircle.brojIgraca = brojIgraca;
                DiamondCircle.mapa = new Polje[dimenzije][dimenzije];
                setSizes();
                for (int i = 0; i < dimenzija; i++) {
                    ColumnConstraints col = new ColumnConstraints();
                    col.setMinWidth(col1);
                    col.setPrefWidth(col1);
                    Matrica.getColumnConstraints().add(col);
                }
                for (int i = 0; i < dimenzija; i++) {
                    RowConstraints row = new RowConstraints();
                    row.setMinHeight(row1);
                    row.setPrefHeight(row1);
                    Matrica.getRowConstraints().add(row);
                }
                int content=1;
                for (int i = 0; i < dimenzija; i++) {
                    for (int j = 0; j < dimenzija; j++) {
                        Text text = new Text(" " + content);
                        text.setStyle("-fx-text-alignment: center");
                        Matrica.add(text, j, i);
                        content++;
                    }
                }
            }
            mc=this;
            diamondCircle=new DiamondCircle();
            setLabelText(brojIgara);
            List<Igrac> temp=new ArrayList<>(diamondCircle.getIgraci());
            Collections.sort(temp, new Comparator<Igrac>() {
                @Override
                public int compare(Igrac o1, Igrac o2) {
                    if(o1.getId()>o2.getId())
                    {
                        return 1;
                    }
                    else if(o1.getId()<o2.getId())
                    {
                        return -1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            });
            for(int i=0;i<temp.size();i++)
            {
                if(temp.get(i).getFigure().get(0).getBoja().equals(BojaFigure.PLAVA))
                {
                    Label label=new Label(temp.get(i).getIme()+ " ");
                    label.setTextFill(Color.BLUE);
                    label.setStyle("-fx-font-size: 16");
                    players.add(label,i,0);
                }
                else if(temp.get(i).getFigure().get(0).getBoja().equals(BojaFigure.CRVENA))
                {
                    Label label=new Label(temp.get(i).getIme()+" ");
                    label.setTextFill(Color.RED);
                    label.setStyle("-fx-font-size: 16");
                    players.add(label,i,0);
                }
                else if(temp.get(i).getFigure().get(0).getBoja().equals(BojaFigure.ZELENA))
                {
                    Label label=new Label(temp.get(i).getIme()+ " ");
                    label.setTextFill(Color.GREEN);
                    label.setStyle("-fx-font-size: 16");
                    players.add(label,i,0);
                }
                else
                {
                    Label label=new Label(temp.get(i).getIme()+" ");
                    label.setTextFill(Color.YELLOW);
                    label.setStyle("-fx-font-size: 14");
                    players.add(label,i,0);
                }
            }
            List<String> naziviFigura=new ArrayList<>();
            for(Figura f:diamondCircle.getFigure())
            {
                naziviFigura.add(f.getNaziv());
            }
            figuresList.getItems().addAll(naziviFigura);
        }
        catch (Exception exception)
        {
            log(exception);
        }
    }

    public void showResults(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("results.fxml"));
            loaderMethod(fxmlLoader);
        }
        catch (Exception e)
        {
            log(e);
        }
    }

    public void showFigureMovement(MouseEvent mouseEvent) {
        try {
            String selectedItem = figuresList.getSelectionModel().getSelectedItem();
            Figura izabranaFigura=null;
            if(selectedItem==null)
            {
                return;
            }
            else
            {
                for(Figura f:diamondCircle.getFigure())
                {
                    if(f.getNaziv().equals(selectedItem))
                    {
                        izabranaFigura=f;
                        break;
                    }
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("figure.fxml"));
            loaderMethod(fxmlLoader);
            fig.prikaziPredjeniPut(izabranaFigura);
        }
        catch (Exception e)
        {
            log(e);
        }

    }

    private void loaderMethod(FXMLLoader fxmlLoader) throws IOException {
        Parent parent=fxmlLoader.load();
        Scene scene = new Scene(parent, 800, 600, Color.GRAY);
        Stage newStage=new Stage();
        newStage.setTitle("Results");
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.show();
    }
}