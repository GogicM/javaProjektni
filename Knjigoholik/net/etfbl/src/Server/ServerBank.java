
package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Milan
 */
public class ServerBank {
    public static final int TCP_PORT_BANK = 9500;
    public static final HashMap<String, Double> kursnaLista = new HashMap<String,Double>();
    
    public static void main(String[] args) {
        try{
            int clientCounter = 0;
            ServerSocket ss = new ServerSocket(TCP_PORT_BANK);
            System.out.println("Bank server running...");
            while(true) {
                //prihvataj korisnike
            Socket sock = ss.accept();
            System.out.println("Client accepted:" + (++clientCounter));
            ServerThreadBank stb = new ServerThreadBank(sock, clientCounter);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void populateKursnaLista() {
    kursnaLista.put("EUR", 1.95);
    kursnaLista.put("HRK", 0.26);
    kursnaLista.put("USD", 1.75);
    kursnaLista.put("NOK", 0.209);
    }
}
