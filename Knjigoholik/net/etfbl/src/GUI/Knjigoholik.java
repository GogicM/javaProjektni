/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Knjiga.Knjiga;
import Knjiga.PisanaKnjiga;
import Korisnik.Korisnik;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableViewBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 *
 * @author Milan
 */
public class Knjigoholik extends Application {
    
    GridPane grid;
    GridPane gridMain;
    GridPane gridTable;
    GridPane gridAccount;
    Scene sceneFirst;
    Scene sceneMain;
    Scene sceneTable;
    Scene sceneAccount;
    Scene sceneSearch;
    Text title;
    Text titleMain;
    Label userName;
    Label password;
    Label search;
    Label accountName;
    Label accountBalance;
    Label currency;
    TextField textField;
    TextField searchBar;
    PasswordField passwordField;
    Button signIn;
    Button buttonSearch;
    Button catalogue;
    Button  back;
    Button accountInfo;
    Button backFromAccInfo;
    Button backFromSearch;
    Button buy;
    Button rent;
    Stage stage;
    TableView table;
    //ovo ce ici drugacije, sad je probba
    //Kako se kupi neka knjiga, onda ce se u ovu listu stavljati preko get metode iz maticne klase
    public static final int TCP_PORT = 9000;
    public static final int TCP_PORT_BANK = 9500;

    @Override
    public void start(Stage primaryStage) throws IOException, FileNotFoundException {
        //za usera postavljamo 0 stanje, kao i prazno korisnicko ime , valuta i sifra
        //kada dobijemo info sa servera, tada setujemo te vrijednosti
        //#server #joy #objectparadigm #this that :D
        Korisnik user = new Korisnik("Milan Gogic", 0, "", "", "");
        stage = primaryStage;
        //postavljamo izgled, u nasem slucaju grid sa redovima i kolonama
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER); //pozicioniramo
        grid.setHgap(10); //prostor izmedju redova i kolona
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25)); //prostor koji grid zauzima u pikselima
        
       
        sceneFirst = new Scene(grid, 300, 275); //kreiramo scenu, povrsinu sa gridom kao glavnim nodom
        primaryStage.setScene(sceneFirst); //setujemo scenu
        
        title = new Text("Dobrodosli"); //tekst u aplikaciji
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));//font
        grid.add(title, 1, 0, 2, 1); //dodajemo naslov u grid kolona 2, red 1
        
        userName = new Label("User name: "); //labela sa tekstom
        grid.add(userName, 0, 1); //dodajemo u grid
        
        textField = new TextField(); //polje za unos teksta
        textField.setPromptText("Unesite korisnicko ime");
        grid.add(textField, 1, 1);
        
                
        password = new Label("Password: "); //labela za sifru
        grid.add(password , 0, 2);
        
        passwordField = new PasswordField(); //polje za unos sifre
        passwordField.setPromptText("Unesite sifru");
        grid.add(passwordField, 1, 2);
        final Label wrongLogin = new Label();
        grid.add(wrongLogin, 1, 3);
        signIn = new Button("Prijavi se"); //login dugme
        HBox hbox = new HBox(10); 
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.getChildren().add(signIn);
        grid.add(hbox, 1, 4);
        //obrada dogadjaja
        //kada kliknemo na dugme za logovanje
        //definisemo akciju koja ce se desiti 
        signIn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        try {
            // odredi adresu racunara sa kojim se povezujemo
            // (povezujemo se sa nasim racunarom)
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            // otvori socket prema drugom racunaru
            Socket sock = new Socket(addr, TCP_PORT);
            // inicijalizuj ulazni stream
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    sock.getInputStream()));
//            // inicijalizuj izlazni stream
//            PrintWriter out = new PrintWriter(
//                    new BufferedWriter(new OutputStreamWriter(
//                                    sock.getOutputStream())), true);  
//            //slanje parametara za prijavu
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            out.writeObject("LOGIN" + "#" + textField.getText() + "#" + passwordField.getText());
               // System.out.println(in.readLine());
            if("OK".equals(in.readObject().toString())) {
                //kad kreiram objekat klase Korisnik, umjesto za username i password posaljem prazan String
                // ako se ispravno loguje, tek tada postavljam ta dva polja na osnovu teksta iz tekst i password polja
                user.setUserName(textField.getText());
                user.setPassword(passwordField.getText());
                stage.setScene(sceneMain);
            }
                else {
                    wrongLogin.setText("Incorrect username or password.");
                    wrongLogin.setTextFill(Color.RED);
                    stage.setScene(sceneFirst);
                       // stage.show();
                    }
                in.close();
                out.close();
                sock.close();    
            }catch (Exception e1) {
                    e1.printStackTrace();
                }     
        }
        }); 

        
        //elementi za drugi prozor, poslije logovanja
        search = new Label("Pretraga: ");
        //samo da ima nesto kad se klikne pretraga, kasnije cu obrisati
        Label search1 = new Label();
        titleMain = new Text("Dobrodosli u KNJIGOHOLIK!"); //tekst u aplikaciji
        titleMain.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));//font
        gridMain = new GridPane(); //pravimo gridPane
        gridMain.setAlignment(Pos.CENTER); //pozicioniramo
        gridMain.setHgap(10); //prostor izmedju redova i kolona
        gridMain.setVgap(10);
        gridMain.setPadding(new Insets(0, 10, 0, 10));
        searchBar = new TextField(); //tekst polje u koje upisujemo pretragu
        searchBar.setPrefColumnCount(5);
        searchBar.setPromptText("Pretraga po zanru ili naslovu"); 
        //dodavanje u grid
        gridMain.add(titleMain, 1, 0, 1, 1);
        gridMain.add(search, 0, 1);
        gridMain.add(searchBar, 1, 1);
        gridMain.add(search1, 1, 5);
        buttonSearch = new Button("Pretrazi");
        buttonSearch.setLayoutX(50);
        HBox hbox1 = new HBox(10); 
        hbox1.getChildren().add(buttonSearch); //dodavanje dugmeta u hbox, stavljanje naziva, pozicioniranje
        gridMain.add(hbox1, 1, 4);
        backFromSearch = new Button("Nazad"); //pravimo potrebnu dugmad
        backFromSearch.setOnAction(e -> ButtonClicked(e));
        buy = new Button("Kupi");
        rent = new Button("Iznajmi");
        backFromSearch.setLayoutX(10); //podesavamo pozicije
        buy.setLayoutX(70);
        rent.setLayoutX(120);
        buttonSearch.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            //povezivanje na server
            //slanje zahtjeva za pretragu
            //server trazi po zanru ili po naslovu
            //vraca rezultate
            //kada vrati, aktivira se nova scena koja ce sadrzavati tabelu sa knjigama
            //check box za odabir kao i dugmad nazad,kupi i iznajmi
            //za sada samo 
            try {
                InetAddress addr = InetAddress.getByName("127.0.0.1");
                Socket socket = new Socket(addr, TCP_PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                
                ArrayList<Knjiga> list = new ArrayList<>();
                String request = searchBar.getText();
                oos.writeObject("SEARCH" + "#" + request);
                Knjiga[] knjige = (Knjiga[]) ois.readObject();
                for(Knjiga k : knjige) {
                    list.add(k);
                }
                TableView<Knjiga> table = new TableView<Knjiga>();
                ObservableList<Knjiga> data =
                FXCollections.observableArrayList(list);

                TableColumn<Knjiga,String> nameCol = new TableColumn<Knjiga,String>("Naziv");
                TableColumn<Knjiga,String> authorCol = new TableColumn<Knjiga,String>("Autor");
                TableColumn<Knjiga,String> costCol = new TableColumn<Knjiga,String>("Cijena");
                TableColumn<Knjiga,CheckBox> checkCol = new TableColumn<Knjiga,CheckBox>("");
                table.setItems(data);
                nameCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("bookName"));
                authorCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("authorName"));
                costCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("cost"));
                checkCol.setCellValueFactory(new PropertyValueFactory<Knjiga, CheckBox>(""));  
                table.getColumns().setAll(nameCol, authorCol, costCol, checkCol);
                oos.close();
                ois.close();
                socket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
//            TableView<Knjiga> table = new TableView<Knjiga>();
            
//            ObservableList<Knjiga> data =
//            FXCollections.observableArrayList(
//                    new PisanaKnjiga("Gozba za vrane -deo prvi", "978-86-743-6449-9", "Dzordz R.R. Martin",
//                    362,"","Laguna", "src/GUI/Images/basara.jpg", 19.50 , 2, "epska fantastika, politicka strategija"));
//        
//            TableColumn<Knjiga,String> nameCol = new TableColumn<Knjiga,String>("Naziv");
//            TableColumn<Knjiga,String> authorCol = new TableColumn<Knjiga,String>("Autor");
//            TableColumn<Knjiga,String> costCol = new TableColumn<Knjiga,String>("Cijena");
//            TableColumn<Knjiga,CheckBox> checkCol = new TableColumn<Knjiga,CheckBox>("");
//            table.setItems(data);
//            nameCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("bookName"));
//            authorCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("authorName"));
//            costCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("cost"));
//            checkCol.setCellValueFactory(new PropertyValueFactory<Knjiga, CheckBox>(""));  
//            table.getColumns().setAll(nameCol, authorCol, costCol, checkCol);
            AnchorPane root = new AnchorPane();
            AnchorPane.setTopAnchor(table, 30.0);
            AnchorPane.setLeftAnchor(table, 10.0);
            AnchorPane.setRightAnchor(table, 10.0);
            AnchorPane.setBottomAnchor(table, 10.0);
            root.getChildren().add(table);
            root.getChildren().addAll(backFromSearch, buy, rent);
            sceneSearch = new Scene(root, 500, 300);
            stage.setScene(sceneSearch);
        }
    });

        //kreiranje dugmeta za prikaz mog kataloga
        catalogue = new Button("Moj katalog");
        catalogue.setOnAction(e -> ButtonClicked(e));
        hbox1.setAlignment(Pos.BOTTOM_LEFT);
        hbox1.getChildren().add(catalogue);
        accountInfo = new Button("Stanje na racunu");
        hbox1.getChildren().add(accountInfo);
        //pravljenje scene i grida za prikaz stanja na racunu
        gridAccount = new GridPane();
        sceneAccount = new Scene(gridAccount, 300, 300);
        gridAccount.setAlignment(Pos.CENTER); //pozicioniramo
        gridAccount.setHgap(10); //prostor izmedju redova i kolona
        gridAccount.setVgap(10);
        gridAccount.setPadding(new Insets(25, 25, 25, 25)); //prostor koji grid zauzima u pikselima
        accountName = new Label();
        accountBalance = new Label();
        currency = new Label();
        gridAccount.add(accountName, 0, 1);
        gridAccount.add(accountBalance, 1, 1);
        gridAccount.add(currency, 2, 1);       
               
        accountInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // odredi adresu racunara sa kojim se povezujemo
                    // (povezujemo se sa nasim racunarom)
                    InetAddress addr1 = InetAddress.getByName("127.0.0.1");
                    // otvori socket prema drugom racunaru
                    Socket sock1 = new Socket(addr1, TCP_PORT_BANK);
                    // inicijalizuj ulazni stream
                    BufferedReader in1 = new BufferedReader(new InputStreamReader(
                                            sock1.getInputStream()));
                    // inicijalizuj izlazni stream
                    PrintWriter out1 = new PrintWriter(
                                        new BufferedWriter(new OutputStreamWriter(
                                            sock1.getOutputStream())), true);  
                    //slanje parametara za prijavu
                    System.out.println(user.getUserName());
                    out1.println(user.getUserName());
                    accountName.setText("NALOG: " + in1.readLine());
                    //user.setAccountBalance(Double.parseDouble(in1.readLine())); //setujemo stanje na racunu za objekat user
                    accountBalance.setText("\nStanje na racunu: " + in1.readLine());
                    currency.setText("\nVALUTA: " + in1.readLine());
                    user.setCurrency(in1.readLine()); //setujemo valutu za objekat user
                    stage.setScene(sceneAccount);
                    in1.close();
                    out1.close();
                    sock1.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
            }
        }); 
        backFromAccInfo = new Button("Nazad");
        backFromAccInfo.setOnAction(e -> ButtonClicked(e));
        gridAccount.add(backFromAccInfo, 0, 0);
        //pravljenje tabele, da vidim znam li :D
        //inicijalno ovako, kasnije moram obraditi da samo kupljene knjige budu dodane, bice cirkus sa nazivima i svim :D
        
        TableColumn<File, Image> imageColumn = TableColumnBuilder.<File, Image>create().text("").build();
            imageColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, Image>, ObservableValue<Image>>() {
                @Override
                public ObservableValue<Image> call(TableColumn.CellDataFeatures<File, Image> p) {
                    File file = p.getValue();
                    return new SimpleObjectProperty<>(new Image(file.toURI().toString(), 300, 300, true, true, true));
                }
            });
            imageColumn.setCellFactory(new Callback<TableColumn<File, Image>, TableCell<File, Image>>(){

                @Override
                public TableCell<File, Image> call(TableColumn<File, Image> p) {
                    return new TableCell<File, Image>(){

                        @Override
                        protected void updateItem(Image i, boolean empty) { //pozivamo da bi se postavila tableCell
                            super.updateItem(i, empty);
                            setText(null);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            ImageView imageView = (i == null || empty) ? null : ImageViewBuilder.create().image(i).build();
                            setGraphic(imageView);
                        }                    
                    };
                }
            });
            //
            TableColumn<File, String> pathColumn = TableColumnBuilder.<File, String>create().text("").build();
            pathColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                   String summaryPath = "src/Knjiga/Opis/";
                   ArrayList<String> list = new ArrayList<>();
                   list.add("basara.txt");
                   list.add("buka.txt");
                   list.add("cuvari.txt");
                   list.add("demijan.txt");
                   String string="";
                   File file = null;
                   try{
                   for(int i = 0; i < list.size(); i++)
                   {    
                        String path1 = summaryPath + list.get(i);
                        file = new File(path1);
                        System.out.println(file);
                        string = Knjiga.fromFileToString(file);
                   }

                   } catch(IOException e) { e.printStackTrace();}

                    return new SimpleStringProperty(string); 
                }
            });

    TableView<File> tableView = TableViewBuilder.<File>create().columns(imageColumn, pathColumn).columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY).build();

    String path = "src/GUI/Images";
    File folder = new File(path);
    File[] files = folder.listFiles(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
        }
    });
    if (files != null) {
        for (File file : files) {
            tableView.getItems().add(file);
        }
    }
        
        //
        back = new Button("Nazad");
        back.setOnAction(e -> ButtonClicked(e));
        AnchorPane root = new AnchorPane();
        AnchorPane.setTopAnchor(tableView, 30.0);
        AnchorPane.setLeftAnchor(tableView, 10.0);
        AnchorPane.setRightAnchor(tableView, 10.0);
        AnchorPane.setBottomAnchor(tableView, 10.0);
        root.getChildren().add(tableView);
        root.getChildren().add(back);
       // root.add(hbox, 1, 4);

 
        sceneTable = new Scene(root, 500, 500);
 
        sceneMain = new Scene(gridMain, 600, 400);
        primaryStage.setTitle("Pocetni prozor");
        primaryStage.setScene(sceneFirst);
        primaryStage.show();
    }

public void ButtonClicked(ActionEvent e) {
    //prvi dio koda ce provjeravati da li je ispravno korisnicko ime i sifra
    //ako jeste, otvara se novi prozor, ako nije onda da se ispise poruka o netacnom unosu
    //za sada, ovako
    // 
    if (e.getSource() == catalogue) {
        stage.setScene(sceneTable);
    } else if(e.getSource() == back || e.getSource() == backFromAccInfo || e.getSource() == backFromSearch) {
        stage.setScene(sceneMain);
    }
    else {
        stage.setScene(sceneFirst);
    }
}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // ServerThread ser = new ServerThread();
        launch(args);
        
    }
    
}
