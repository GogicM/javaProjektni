/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.*;
import java.net.*;

/**
 *
 * @author Milan
 */
public class ServerThread extends Thread {

    public ServerThread(Socket sock, int value) {
        this.sock = sock;
        this.value = value;
        try {
            // inicijalizuj ulazni stream
            in = new BufferedReader(
                    new InputStreamReader(
                            sock.getInputStream()));
            // inicijalizuj izlazni stream
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    sock.getOutputStream())), true);
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
            String login = in.readLine();
            System.out.println(login);
            String[] loginElements = login.split("#");
            if (loginValidation(loginElements[0], loginElements[1])) {
                out.println("OK");
            } else {
                out.println("Korisnicko ime ili sifra pogresni");
            }    
            // zatvori konekciju
            in.close();
            out.close();
            sock.close();
            System.out.println("[Client " + value + "] se odjavio");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private synchronized boolean loginValidation(String username, String password) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("F:/Java/Gogic Milan 63-08/Knjigoholik/net/etfbl/src/Server/users.txt"));
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
    //pomocna metoda u kojoj cu kreirati 20 knjiga, dodati u arraylist u
    //eventualno ih rasporediti u serijale (imam Pesme leda i vatre i Gospodar prstenova
    public void init() {
    }
    private Socket sock;
    //redni broj klijenta
    private int value;
    private BufferedReader in;
    private PrintWriter out;
}