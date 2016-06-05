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

    
    public ServerThread(Socket sock, int value) {
        this.sock = sock;
        this.value = value;
        try {
            // inicijalizuj ulazni stream
//            in = new BufferedReader(
//                    new InputStreamReader(
//                            sock.getInputStream()));
//            // inicijalizuj izlazni stream
//            out = new PrintWriter(
//                    new BufferedWriter(
//                            new OutputStreamWriter(
//                                    sock.getOutputStream())), true);
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
            String login = (String)ois.readObject();
            do {
            if(login.startsWith("LOGIN")){
                String[] loginElements = login.split("#");
                if (loginValidation(loginElements[1], loginElements[2])) {
                    oos.writeObject("OK");
                } else {
                    oos.writeObject("Korisnicko ime ili sifra pogresni");
                }
            }
            //obrada zahtjeva za pretragu knjige
            String request = (String) ois.readObject();
            System.out.println(request);
            if(request.startsWith("SEARCH")) {
            System.out.println(request);
            String[] s = request.split("#");
            for(Knjiga k : Server.listaKnjiga) {
                if(s[1].equals(k.getAuthorName()) || s[2].startsWith(k.getGenre()) || s[2].endsWith(k.getGenre())) {
                    oos.writeObject(k);
                    oos.flush();
                }
                else {
                    oos.writeObject("Nema trazene knjige");
                }
            }
            }
            else { oos.writeObject("Greska!"); }
            } while(!"KRAJ".equals(login));

            // zatvori konekciju
      //      in.close();
        //    out.close();
            oos.close();
            ois.close();
            sock.close();
            System.out.println("[Client " + value + "] se odjavio");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
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
    private BufferedReader in;
    private PrintWriter out;
}