/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Knjiga.Knjiga;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Milan
 */
public class ServerThread extends Thread {
    
    public ObjectOutputStream oos;    
    public ObjectInputStream ois;
    //lista knjiga trazenih od korisnika prilikom pretrage
    ArrayList<Knjiga> lista = new ArrayList<>();
    //lista knjiga  trazenih za iznajmljivanje
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
            sock.setKeepAlive(true);
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
            if(request.startsWith("RENT")) {
                String[] strings = request.split("#");
                System.out.println(strings[0] + " " + strings[1]);
                for(int i = 0; i < Server.bookList.size(); i++) {  
                    if(Server.bookList.get(i).getBookName().equals(strings[1]) &&
                        Server.bookList.get(i).getIsItAvailable()) {
                        oos.writeObject("OK");
                      //  oos.flush();
                        Server.bookList.get(i).setIsItAvailable(false);
                        Knjiga knjiga = Server.bookList.get(i);
                        try {
                            Random r = new Random();
                            int rand = r.nextInt(8000) + 3000;
                            sleep(rand);
                        } catch(Exception e) {
                            e.printStackTrace();
                            
                        }
                        Server.bookList.get(i).setIsItAvailable(true);

                      System.out.println("Poslije treda: " + Server.bookList.get(i).toString());
                    } 
                    if(!Server.bookList.get(i).getIsItAvailable()) {
                        oos.writeObject("ERROR" + "#" + Server.bookList.get(i).getBookName());
                        while(!Server.bookList.get(i).getIsItAvailable()) {
                            try {
                                sleep(500);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }                           
                        }
                        oos.writeObject("AVAILABLE" + "#" + Server.bookList.get(i).getBookName());
                        
                    }
//                    else {
//                        oos.writeObject("AVAILABLE" + "#" + Server.bookList.get(i).getBookName());
//                        oos.flush();
//                                }
                }
            }
            
            if(request.startsWith("SEARCH")) {
                String[] s = request.split("#");
                for(Knjiga k : Server.listaKnjiga) { //for petljom kroz listu knjiga
                    if(s[1].equals(k.getAuthorName()) || k.getAuthorName().startsWith(s[1]) || //provejra da li u listi ima knjige
                        k.getAuthorName().endsWith(s[1]) || s[1].equals(k.getBookName()) ||//od unesenog autora
                         k.getBookName().startsWith(s[1]) || s[1].equals(k.getGenre()) || // zanra, ili naziva...
                                k.getGenre().startsWith(s[1]) || k.getGenre().endsWith(s[1])) { //&&!s[2].equals(null) || s[2].startsWith(k.getGenre()) || s[2].endsWith(k.getGenre())) {
                        lista.add(k); 
                    }
                }
            if(!lista.isEmpty()) {
             oos.writeObject(lista);
             oos.flush();
            }
            else {
                oos.writeObject(null);
                oos.flush();
            }
            }
//            if(request.startsWith("DELETE")) {
//                String strings[] = request.split("#");
//                for(int i = 0; i < Server.bookList.size(); i++) {
//                    if(Server.bookList.get(i).getBookName().equals(strings[1])) {
//                        Server.bookList.remove(Server.bookList.get(i));
//                    }
//                }
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
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    private Socket sock;
    //redni broj klijenta
    private int value;

}