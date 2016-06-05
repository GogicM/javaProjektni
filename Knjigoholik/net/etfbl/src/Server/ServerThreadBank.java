/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Milan
 */
public class ServerThreadBank extends Thread {
//    public enum Currency {
//        EUR(1.95),
//        BAM(1),
//        HRK(3.84),
//        USD(1.75),
//        NOK(0.20);
//        private double value;
//        private Currency(double value) {
//            this.value = value;
//        }
//    }
    public static final HashMap kursnaLista = new HashMap();
    public ServerThreadBank(Socket socket, int value) {
        this.socket = socket;
        this.value = value;
        try{
            // inicijalizuj ulazni stream
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            // inicijalizuj izlazni stream
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
        } catch(Exception e) {
            e.printStackTrace();
        }
        start();    
        }
    public void run() {
        try{//obrada klijentskih zahtjeva 
            /*klijent posalje zahtjev, u vidu neke poruke, server ocita zahtjev, u zavisnosti od 
             same poruke izvrsi odredjenu akciju, i vrati klijentu odgovor*/
            // procitaj zahtjev
            BufferedReader input = new BufferedReader(new FileReader("src/Server/moneyBalance.txt"));          
            String request = in.readLine();
            if(userNameValidation(request)) {
                System.out.println(request);
                out.println(getAccountName(request));
                out.println(getAccountBalance(request));
                out.println(getAccountCurrency(request));
            }
            else {
                 out.println("Greska");
            }     
            in.close();
            out.close();
            socket.close();
            System.out.println("[Client " + value + "] se odjavio");
            } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private synchronized String getAccountName(String username) {
        String c = "";
        try {
            BufferedReader input = new BufferedReader(new FileReader("src/Server/moneyBalance.txt"));
            String s;
            while ((s = input.readLine()) != null) {
                if(s.startsWith(username)) {
                c = s.split("#")[0];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return c;
    }
    private synchronized String getAccountBalance(String username) {
        String c = "";
        try {
            BufferedReader input = new BufferedReader(new FileReader("src/Server/moneyBalance.txt"));
            String s;
            while ((s = input.readLine()) != null) {
                if (s.startsWith(username)) {
                c = s.split("#")[1];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return c;
    }
    
    private synchronized String getAccountCurrency(String username) {
        String c = "";
        try {
            BufferedReader input = new BufferedReader(new FileReader("src/Server/moneyBalance.txt"));
            String s;
            while ((s = input.readLine()) != null) {
                if (s.startsWith(username)) {
                    c = s.split("#")[2];
                }
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return c;
    }
    private synchronized boolean userNameValidation(String username) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/Server/users.txt"));
            String s;
            while ((s = in.readLine()) != null) {
                if (s.startsWith(username)) {
                    String u = s.split("#")[0];
                    if (u.equals(username)) {
                        return true;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    private Socket socket;
    //redni broj klijenta
    private int value;
    private BufferedReader in;
    private PrintWriter out;
}
