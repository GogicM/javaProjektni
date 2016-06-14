/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import GUI.Knjigoholik;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Milan
 */
public class ServerThreadBank extends Thread {

    public static final HashMap<String, Double> kursnaLista = new HashMap<String,Double>();
    public ServerThreadBank(Socket socket, int value) {
        this.socket = socket;
        this.value = value;
        try{
              // inicijalizuj izlazni stream
            out = new ObjectOutputStream(socket.getOutputStream());
            // inicijalizuj ulazni stream
            in = new ObjectInputStream(socket.getInputStream());
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
           // BufferedWriter bw = new BufferedWriter(new FileWriter(new File("src/Server/moneyBalance.txt")));
            
            String request = (String)in.readObject();
            if(userNameValidation(request)) {
                System.out.println(request);
                out.writeObject(getAccountName(request));
                out.writeObject(getAccountBalance(request));
                out.writeObject(getAccountCurrency(request));
                System.out.println(getAccountName(request));
                Knjigoholik.user.setAccountBalance(Double.parseDouble(getAccountBalance(request)));
                Knjigoholik.user.setCurrency(getAccountCurrency(request));
                                Knjigoholik.user.setUserName(getAccountName(request));

            }
//            if(request.startsWith("QUANTITY")) {
//                String strings[] = request.split("#");
//                bw.write(strings[1]);
//            }
            else {
                 out.writeObject("Greska");
            }
            if(request.startsWith("courseList")) {
                Random r = new Random();
                System.out.println(request);

//                new Thread() {
//                    public void run() {
//                        try{
                            double rangeMin = 1.2;
                            double rangeMax = 8.2;
                            double randNumber = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
                            ServerBank.kursnaLista.put("EUR", randNumber);
                            ServerBank.kursnaLista.put("HRK", randNumber);
                            ServerBank.kursnaLista.put("USD", randNumber);
                            ServerBank.kursnaLista.put("NOK", randNumber);
                            out.writeObject(ServerBank.kursnaLista);

//                            Thread.sleep(2000);
//                        } catch(Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//            }.start();
                
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
            //input.close();
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
            input.close();
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
            input.close();
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
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    private Socket socket;
    //redni broj klijenta
    private int value;
    private ObjectOutputStream out;
    private ObjectInputStream in;

}
