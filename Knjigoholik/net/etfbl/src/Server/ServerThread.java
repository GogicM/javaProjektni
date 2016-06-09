/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Knjiga.ElektronskaKnjiga;
import Knjiga.Knjiga;
import Knjiga.PisanaKnjiga;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Milan
 */
public class ServerThread extends Thread {
    
    private ObjectOutputStream oos;    
    private ObjectInputStream ois;
    //lista knjiga trazenih od korisnika prilikom pretrage
    ArrayList<Knjiga> lista = new ArrayList<>();
    //lista knjiga za trazenih za iznajmljivanje
    ArrayList<Knjiga> forRent = new ArrayList<>();
    
    public ServerThread(Socket sock, int value) {
        this.sock = sock;
        this.value = value;
        try {
            oos = new ObjectOutputStream(sock.getOutputStream());
            ois = new ObjectInputStream(sock.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        start();
    }
        //implementiraj run metodu
    public void run() {
        try {
            //obrada klijentskih zahtjeva 
            /*klijent posalje zahtjev, u vidu neke poruke, server ocita zahtjev, u zavisnosti od 
             same poruke izvrsi odredjenu akciju, i vrati klijentu odgovor*/
            // procitaj zahtjev
            String request = (String)ois.readObject();
            
            if(request.startsWith("LOGIN")){
                String[] loginElements = request.split("#");
                System.out.println(request);
                if (loginValidation(loginElements[1], loginElements[2])) {
                    oos.writeObject("OK");
                } else {
                    oos.writeObject("Korisnicko ime ili sifra pogresni");
                }
            }
            //obrada zahtjeva za pretragu knjige

            
            if(request.startsWith("SEARCH")) {
                String[] s = request.split("#");
            for(Knjiga k : Server.listaKnjiga) { //for petljom kroz listu knjiga
                if(s[1].equals(k.getAuthorName()) || k.getAuthorName().startsWith(s[1]) || //provejra da li u listi ima knjige
                        k.getAuthorName().endsWith(s[1]) || s[1].equals(k.getBookName()) ||//od unesenog autora
                         k.getBookName().startsWith(s[1]) || s[1].equals(k.getGenre()) || // zanra, ili naziva...
                                k.getGenre().startsWith(s[1]) || k.getGenre().endsWith(s[1])) { //&&!s[2].equals(null) || s[2].startsWith(k.getGenre()) || s[2].endsWith(k.getGenre())) {
                    lista.add(k); 
                    System.out.println(k.getGenre());
                    System.out.println(lista.get(0).toString());
                }
                }
            if(!lista.isEmpty()) {
             oos.writeObject(lista);
             oos.flush();
            }
            else {
                oos.writeObject(null);
            }
            }
//            if(request.startsWith("RENT")) {
//                
//            }
            oos.close();
            ois.close();
            sock.close();
            System.out.println("[Client " + value + "] se odjavio");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //pomocna metoda koja provjerava da li su dobri podaci za logovanje
    //cita podatke iz fajla, pravi odvojene stringove od username a i passworda 
    //i provjerava da su jednaki sa username i password koji su poslani sa klijentske strane
    private synchronized boolean loginValidation(String username, String password) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/Server/users.txt"));
            String s;
            while ((s = in.readLine()) != null) {
                if (s.startsWith(username)) {
                    String u = s.split("#")[0];
                    String p = s.split("#")[1];
                    if (u.equals(username) && p.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    private Socket sock;
    //redni broj klijenta
    private int value;

}