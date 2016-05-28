/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Milan
 */
public class Knjigoholik extends Application {
    
    GridPane grid;
    GridPane gridMain;
    GridPane gridTable;
    Scene sceneFirst;
    Scene sceneMain;
    Scene sceneTable;
    Text title;
    Text titleMain;
    Label userName;
    Label password;
    Label search;
    TextField textField;
    TextField searchBar;
    PasswordField passwordField;
    Button signIn;
    Button buttonSearch;
    Button catalogue;
    Stage stage;
    TableView table;
    
    @Override
    public void start(Stage primaryStage) {
        
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
        
        signIn = new Button("Prijavi se"); //login dugme
        signIn.setOnAction(e -> ButtonClicked(e));
        HBox hbox = new HBox(10); 
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.getChildren().add(signIn);
        grid.add(hbox, 1, 4);
        
        //elementi za drugi prozor, poslije logovanja
        search = new Label("Pretraga: ");
        titleMain = new Text("Dobrodosli u KNJIGOHOLIK!"); //tekst u aplikaciji
        titleMain.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));//font
        gridMain = new GridPane();
        gridMain.setAlignment(Pos.CENTER); //pozicioniramo
        gridMain.setHgap(10); //prostor izmedju redova i kolona
        gridMain.setVgap(10);
        gridMain.setPadding(new Insets(0, 10, 0, 10));
        searchBar = new TextField();
        searchBar.setPrefColumnCount(5);
        searchBar.setPromptText("Pretraga po zanru ili naslovu");
        gridMain.add(titleMain, 1, 0, 1, 1);
        gridMain.add(search, 0, 1);
        gridMain.add(searchBar, 1, 1);
        buttonSearch = new Button("Pretrazi");
        //buttonSearch.setOnAction(e -> ButtonClicked(e));
        HBox hbox1 = new HBox(10); 
        hbox1.setAlignment(Pos.BOTTOM_RIGHT);
        hbox1.getChildren().add(buttonSearch);
        gridMain.add(hbox1, 1, 4);
        HBox hbox2 = new HBox(10);
        catalogue = new Button("Moj katalog");
        catalogue.setOnAction(e -> ButtonClicked(e));
        hbox2.setAlignment(Pos.BOTTOM_LEFT);
        hbox2.getChildren().add(catalogue);
        gridMain.add(hbox2, 1, 4);
        //pravljenje tabele
        TableView table = new TableView();
        table.setEditable(true);

        TableColumn image = new TableColumn();
        TableColumn text = new TableColumn();

        table.getColumns().addAll(image, text);

        StackPane root = new StackPane();
        root.getChildren().add(table);
        sceneTable = new Scene(root, 300, 300);
                
        sceneMain = new Scene(gridMain, 600, 400);
        primaryStage.setTitle("Pocetni prozor");
        primaryStage.show();
    }

public void ButtonClicked(ActionEvent e) {
    //prvi dio koda ce provjeravati da li je ispravno korisnicko ime i sifra
    //ako jeste, otvara se novi prozor, ako nije onda da se ispise poruka o netacnom unosu
    //za sada, ovako
    if(e.getSource() == signIn ) {
        stage.setScene(sceneMain);
    } else if (e.getSource() == catalogue) {
        stage.setScene(sceneTable);
    } else {
        stage.setScene(sceneFirst);
    }
}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
