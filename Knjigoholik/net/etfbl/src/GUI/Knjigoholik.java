/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Knjiga.Knjiga;
import Korisnik.Korisnik;
import Server.Server;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    Button download;
    Stage stage;
    TableView table;

    public static Korisnik user;
    //lista from server se stalno cisti,
    //da bi prilikom svakog poziva bile samo aktuelne knjige, a ne i one koje smo prije pretrazivali
    public static ArrayList<Knjiga> fromServer = new ArrayList<>();
    //knjige koje selektujemo u checkbox u, takodje se cisti lista da pamti samo trenutno selektovane
    public static ArrayList<Knjiga> purchasedBooks = new ArrayList<>();
    public static ArrayList<Knjiga> myCatalogue = new ArrayList<>();
    public static ArrayList<Knjiga> userCatalogue = new ArrayList<>();
    // kursna lista
    public HashMap<String, Double> courseList = new HashMap<String, Double>();
    public static final int TCP_PORT = 9000;
    public static final int TCP_PORT_BANK = 9500;
    //helper metoda za osvjezavanje podataka u tabeli
    public static <T> void refresh(final TableView<T> table, final List<T> tableList) {
        //Wierd JavaFX bug
        table.setItems(null);
        table.layout();
        table.setItems(FXCollections.observableList(tableList));
  }
    public void setCourseList() {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            Socket sock = new Socket(addr, TCP_PORT_BANK);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            
            out.writeObject("courseList");
            Map list = new HashMap();
            list = (HashMap) in.readObject();
            courseList.putAll(list);
            Set set = courseList.entrySet();
            Iterator i = set.iterator();
            while(i.hasNext()) {
                Map.Entry me = (Map.Entry)i.next();
                System.out.println(me.getKey() + ": " +me.getValue());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) throws IOException, FileNotFoundException {
        //za usera postavljamo 0 stanje, kao i prazno korisnicko ime , valuta i sifra
        //kada dobijemo info sa servera, tada setujemo te vrijednosti
        //#server #joy #objectparadigm #this that :D
//        setCourseList();
        user = new Korisnik("Milan Gogic", 0, "", "", "");
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
            //slanje parametara za prijavu
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
//                setCourseList();
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

                //za regulisanje kursne liste, pitati asistente
//            Timer timer = new java.util.Timer();
//
//            new Timer.schedule(new TimerTask() {
//                public void run() {
//                    Platform.runLater(new Runnable() {
//                        public void run() {
//                            try {
//                                InetAddress addr = InetAddress.getByName("127.0.0.1");
//                                // otvori socket prema drugom racunaru
//                                Socket sock = new Socket(addr, TCP_PORT_BANK);
//                                // inicijalizuj ulazni stream
//                                //slanje parametara za prijavu
//                                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
//                                ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
//                                out.writeChars("courseList");
//                                HashMap<String, Double> lista = new HashMap<String, Double>();
//                                lista = (HashMap<String, Double>) in.readObject();
                                  HBox hboxCourseList = new HBox();
                                  TableView tableCourseList = new TableView();
//                                stage.setScene(sceneMain);
//                                System.out.println(lista.toString());
//                            } catch(Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }, 0, 20000);
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
            try {
                InetAddress addr = InetAddress.getByName("127.0.0.1");
                Socket socket = new Socket(addr, TCP_PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ArrayList<Knjiga> list = new ArrayList<>();
                String request = searchBar.getText();
                if(!request.equals("")) {
                oos.writeObject("SEARCH" + "#" + request);
                fromServer.clear();
                fromServer.addAll((ArrayList<Knjiga>) ois.readObject());
                list.addAll(fromServer);

                AnchorPane root = new AnchorPane();
                sceneSearch = new Scene(root, 500, 300);
                for(Knjiga k : fromServer) {
                if(fromServer.isEmpty() && !fromServer.contains(k)) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Obavjestenje");
                    alert.setHeaderText(null);
                    alert.setContentText("Nemamo trazenu knjigu!");

                    alert.showAndWait();

                }

                }
                TableView<Knjiga> table = new TableView<Knjiga>();
                ObservableList<Knjiga> data =
                FXCollections.observableArrayList(list);
                //koristimo ovo da se rasporedi prostor izmedju postojecih kolona
                //ili ce od preostalog prostora napraviti jos jednu praznu kolonu
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn<Knjiga, String> nameCol = new TableColumn<Knjiga, String>("Naziv");
                TableColumn<Knjiga, String> authorCol = new TableColumn<Knjiga, String>("Autor");
                TableColumn<Knjiga, String> costCol = new TableColumn<Knjiga, String>("Cijena");
                Callback<TableColumn<Knjiga, Boolean>, TableCell<Knjiga, Boolean>> cellFactory;
                TableColumn checkCol= new TableColumn<Knjiga, Boolean>();

                table.setItems(data);
                nameCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("bookName"));
                nameCol.setMinWidth(150);
                authorCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("authorName"));
                costCol.setCellValueFactory(new PropertyValueFactory<Knjiga, String>("cost"));
                checkCol.setCellValueFactory(new PropertyValueFactory<Knjiga, Boolean>("checked"));
                cellFactory = new Callback<TableColumn<Knjiga, Boolean>, TableCell<Knjiga, Boolean>>() {
                @Override
                public TableCell<Knjiga, Boolean> call(final TableColumn<Knjiga, Boolean> param) {
                     final CheckBox checkBox = new CheckBox();
                     final TableCell cell = new TableCell() {

                   // @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            checkBox.setDisable(true);
                            checkBox.setSelected(false);
                        } else {
                            checkBox.setDisable(false);
                            checkBox.setSelected(item.equals(true) ? true : false);
                            checkBox.setOnAction((event) -> { // sta se desava kada selektujemo checkbox
                                Knjiga k =(Knjiga) getTableView().getItems().get(getIndex());                                
                                if(checkBox.isSelected()) {
                                        purchasedBooks.clear();
                                        k.setQuantity(k.getQuantity() - 1);
                                        k.setChecked(true);
                                        purchasedBooks.add(k);                                        
                                        myCatalogue.add(k);
                                        if(k.getQuantity() == 0) {
                                            purchasedBooks.remove(k);
                                            myCatalogue.remove(k);
                                            Server.bookList.remove(k);
                                            list.remove(k);
                                            table.getItems().remove(getIndex());
                                            refresh(table, list);
                                        }
                                    }
                                else if(!checkBox.isSelected()) {
                                    k.setChecked(false);
                                    k.setIsItAvailable(false);
                                }
                                  
//                                    }

                            });
                        commitEdit(checkBox.isSelected() ? true : false);
                        }
                    }
                };
                cell.setGraphic(checkBox);
                return cell;
            }
        };
                // System.out.println(myCatalogue.get(0).toString());
                checkCol.setCellValueFactory(new PropertyValueFactory("checked"));
                checkCol.setCellFactory(cellFactory);
                AnchorPane.setTopAnchor(table, 30.0);
                AnchorPane.setLeftAnchor(table, 10.0);
                AnchorPane.setRightAnchor(table, 10.0);
                AnchorPane.setBottomAnchor(table, 10.0);
                refresh(table, data);
                table.getColumns().setAll(nameCol, authorCol, costCol, checkCol);
                root.getChildren().add(table);
                root.getChildren().addAll(backFromSearch, buy, rent);
                stage.setScene(sceneSearch);

                }
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Obavjestenje");
                    alert.setHeaderText(null);
                    alert.setContentText("Morate unijeti tekst za pretragu!");

                    alert.showAndWait();
                }

                oos.close();
                ois.close();
                socket.close();
            } catch(Exception e) {
                e.printStackTrace();
            }

        }

    });
        buy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myCatalogue.clear();
                myCatalogue.addAll(purchasedBooks);
                try {
                InetAddress addr = InetAddress.getByName("127.0.0.1");
                Socket socket = new Socket(addr, TCP_PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                
                for(Knjiga k : myCatalogue) {
                    if(k.getIsItAvailable() && k.getQuantity() > 0
                            && user.getAccountBalance() > k.getCost() && k.getChecked()) { //jos obraditi koja je valuta i pretvoriti
                        //novac korisnika - cijena knjige
                        user.setAccountBalance(user.getAccountBalance() - k.getCost());
                        //pravimo direktorijum
                        File f = new File("src/Korisnik/" + user.getUserName() + "/MojKatalog");
                        f.mkdirs();
                        //umanjujemo kolicinu knjige za jedan
                        k.setQuantity(k.getQuantity() - 1);
                        //kopiramo u folder korisnika knjigu
                        String dPath = "src/Korisnik/" + user.getUserName()+"/MojKatalog/"+k.getBookName()+".txt";
                        String sPath = "src/Server/knjige/" + k.getBookName()+".txt";
                        userCatalogue.add(k);
                         try {
                             File source = new File(sPath);
                             File destination = new File(dPath);
                             Files.copy(source.toPath(),
                             destination.toPath(),REPLACE_EXISTING);
                             } catch(IOException e) {
                                    e.printStackTrace();
                             }
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Obavjestenje");
                        alert.setHeaderText("KNJIGOHOLIK KAZE :");
                        alert.setContentText("Uspjesno ste kupili knjigu. Hvala na povjerenju!");

                        alert.showAndWait();
                         }
            else if(k.getQuantity() < 1){
            Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Obavjestenje");
                alert.setHeaderText("KNJIGOHOLIK KAZE :");
                alert.setContentText("Nema knjige na stanju!");

                alert.showAndWait();
                Server.bookList.remove(k);
                
        }
            else if(user.getAccountBalance() < k.getCost()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Obavjestenje");
                alert.setHeaderText("KNJIGOHOLIK KAZE :");
                alert.setContentText("Nemate dovoljno novca za kupovinu!");

                alert.showAndWait();
            }
            else if(myCatalogue.isEmpty() || !k.getChecked()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Obavjestenje");
                alert.setHeaderText("KNJIGOHOLIK KAZE :");
                alert.setContentText("Odaberite neku knjigu");

                alert.showAndWait();
            
            }
                oos.close();
                ois.close();
                socket.close();
                }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                    }

        });
        rent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                   // myCatalogue.clear();
                    //  myCatalogue.addAll(purchasedBooks);
                    InetAddress addr = InetAddress.getByName("127.0.0.1");
                    Socket socket = new Socket(addr, TCP_PORT);
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    System.out.println(purchasedBooks.toString());
                    for(int i = 0; i < purchasedBooks.size(); i++) {
                        if(purchasedBooks.get(i).getChecked()) {
                            oos.writeObject("RENT" + "#" + purchasedBooks.get(i).getBookName());
                            String read = (String) ois.readObject();
                            System.out.println(read);
                            if(read.equals("OK") && purchasedBooks.get(i).getIsItAvailable()) {
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Obavjestenje");
                                alert.setHeaderText("KNJIGOHOLIK KAZE :");
                                alert.setContentText("Uspjesno ste iznajmili knjigu.");

                                alert.showAndWait();
                               // socket.setSoTimeout(10000);
                                
                            } else if(read.startsWith("ERROR")){
                                Alert alert = new Alert(AlertType.WARNING);
                                alert.setTitle("Obavjestenje");
                                alert.setHeaderText("KNJIGOHOLIK KAZE :");
                                alert.setContentText("Knjiga je vec iznajmljena, probajte ponovo za minut");
                                alert.showAndWait();
                                
                                //oos.writeObject("AVAILABLE");
                                String string = (String) ois.readObject();
                                System.out.println(string);

                                System.out.println(string);
                                String strings[] = string.split("#");
                                Alert alert1 = new Alert(AlertType.INFORMATION);
                                alert1.setTitle("Obavjestenje");
                                alert1.setHeaderText("KNJIGOHOLIK KAZE :");
                                alert1.setContentText("Knjiga " + strings[1] + " je ponovo dostupna za iznajmljivanje.");

                                alert1.showAndWait();
                               }    
                        }
                        
                    }
                    oos.close();
                    ois.close();
                    socket.close();
                } catch(Exception e) {
                    e.printStackTrace();
            }
            }
        });                
        
        //kreiranje dugmeta za prikaz mog kataloga
        catalogue = new Button("Moj katalog");
        //Definisemo akciju koja se desava klikom na dugme Moj Katalog
        catalogue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                //PRavimo table view
                TableView<Knjiga> table = new TableView<Knjiga>();
                AnchorPane root = new AnchorPane();                        
                //Pravimo observable listu, posebna lista koja ima data binding, tj. prati promjene za objekte Klase Knjiga
                //Prosledjujemo joj listu userCatalogue
                ObservableList<Knjiga> data = FXCollections.observableArrayList(userCatalogue);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn<Knjiga, String> imageCol = new TableColumn<Knjiga, String>();
                TableColumn<Knjiga, String> descriptionCol = new TableColumn<Knjiga, String>();
                Callback<TableColumn<Knjiga, String>, TableCell<Knjiga, String>> cellFactory;
                table.setItems(data);
                
                back = new Button("Nazad");
                sceneTable = new Scene(root, 500, 500);

                imageCol.setCellValueFactory(new PropertyValueFactory("mainPage"));
                descriptionCol.setCellValueFactory(new PropertyValueFactory("summary"));
                cellFactory = new Callback<TableColumn<Knjiga, String>, TableCell<Knjiga, String>>() {
                    @Override
                    public TableCell<Knjiga, String> call(TableColumn<Knjiga, String> param) {
                         HBox box= new HBox();                         
                         ImageView imageview = new ImageView();                      
                         return new TableCell<Knjiga, String> () {
                            @Override
                            public void updateItem(String item, boolean empty) {
                               super.updateItem(item, empty);
                               if (item != null) {
                                   box.setSpacing(10) ;
                                   box.setPadding(new Insets(10, 10, 10, 10));
                                   Image img = null;
                                   Knjiga k = getTableView().getItems().get(getIndex());
                                   img = new Image(new File(k.getMainPage()).toURI().toString());

                                   imageview.setImage(img);
                                   imageview.setFitHeight(320.0);
                                   imageview.setFitWidth(200.0);

                               }
                               // da se ne dodaje dva puta isti node, jer baca IllegalArgumentException
                               if(!box.getChildren().contains(imageview)) {
                               box.getChildren().add(imageview);    
                               setGraphic(box);
                               }
                           }
                         };
                    }
                };
                imageCol.setCellFactory(cellFactory);
                table.getColumns().addAll(imageCol, descriptionCol);
                AnchorPane.setTopAnchor(table, 30.0);
                AnchorPane.setLeftAnchor(table, 10.0);
                AnchorPane.setRightAnchor(table, 10.0);
                AnchorPane.setBottomAnchor(table, 10.0);
                root.getChildren().addAll(table, back);
                back.setOnAction(e -> ButtonClicked(e));

            stage.setScene(sceneTable);
            }   
        });
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
        gridAccount.add(accountName, 0, 3);
        gridAccount.add(accountBalance, 0, 5);
        gridAccount.add(currency, 0, 7);

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
                    ObjectInputStream in1 = new ObjectInputStream(sock1.getInputStream());
                    // inicijalizuj izlazni stream
                    ObjectOutputStream out1 = new ObjectOutputStream(sock1.getOutputStream());
                    //slanje parametara za prijavu
                    out1.writeObject(user.getUserName());
                    //dobijamo sa servera korisnicko ime
                    String accResponse = (String) in1.readObject();
                    //postavljamo username za korisnika
                    user.setUserName(accResponse);
                    accountName.setText("NALOG: " + accResponse);
                    //dobijamo sa servera stanje na racunu
                    accResponse = (String) in1.readObject();
                    accountBalance.setText("STANJE NA RACUNU: " + accResponse);
                    //postavljamo za korisnika stanje na racunu dobijeno sa servera
                    user.setAccountBalance(Double.parseDouble(accResponse));
                    //saljemo zahtjev da bi dobili valutu
                    accResponse = (String) in1.readObject();
                    currency.setText("VALUTA: " + accResponse);
                    //postavjlamo valutu za korisnika
                    user.setCurrency(accResponse);
                    stage.setScene(sceneAccount);
                    in1.close();
                    out1.close();
                    sock1.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
            }
        });
        download = new Button("Preuzmite katalog");
        download.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(!userCatalogue.isEmpty()) {
                        System.out.println("KORISNIK " + user.getUserName());
                        System.out.println(userCatalogue.toString());
                            File file = new File("src/Korisnik/" + user.getUserName() + "/" + "MojKatalog/MojKatalog.txt");
                            file.createNewFile();
                            FileWriter writer = new FileWriter(file); 
                           // System.out.println(k.getSummary());
                            writer.write(userCatalogue.toString());
                            writer.write("***********************************");
                            writer.write("\n");
                            writer.close();
                }
                else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Obavjestenje");
                    alert.setHeaderText("KNJIGOHOLIK KAZE :");
                    alert.setContentText("Morate nesto kupiti da bi imali katalog. Viva la Capitalism! :)");

                    alert.showAndWait();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        });
        hbox1.getChildren().add(download);
        backFromAccInfo = new Button("Nazad");
        backFromAccInfo.setOnAction(e -> ButtonClicked(e));
        gridAccount.add(backFromAccInfo, 0, 0);
        //pravljenje tabele, da vidim znam li :D
        //inicijalno ovako, kasnije moram obraditi da samo kupljene knjige budu dodane, bice cirkus sa nazivima i svim :D



        sceneMain = new Scene(gridMain, 600, 400);
        primaryStage.setTitle("Pocetni prozor");
        primaryStage.setScene(sceneFirst);
        primaryStage.show();
    }

public void ButtonClicked(ActionEvent e) {

    if(e.getSource() == back || e.getSource() == backFromAccInfo || e.getSource() == backFromSearch) {
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
