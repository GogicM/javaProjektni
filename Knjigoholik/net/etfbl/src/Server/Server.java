package Server;

import Knjiga.ElektronskaKnjiga;
import Knjiga.Knjiga;
import Knjiga.ElektronskaKnjiga;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Milan
 */
public class Server {
    //podesavamo port na kom ce server osluskivati zahtjeve od klijenata
    public static final int TCP_PORT = 9000;
    public static final String path = "src/Knjiga/Opis/";
    public static Knjiga[] listaKnjiga;
    public static ArrayList<Knjiga> bookList = new ArrayList<Knjiga> ();
    public static ArrayList<Knjiga> songOfIceAndFire = new ArrayList<Knjiga>();
    public static ArrayList<Knjiga> lotr = new ArrayList<Knjiga>();
    public static ArrayList<Knjiga> serijali = new ArrayList<Knjiga>();

    public static void main(String[] args) {
        init();
        //serijalizacija kolekcija knjiga
        try{
            FileOutputStream fos = new FileOutputStream("F:/Java/Gogic Milan 63-08/Knjigoholik/net/etfbl/src/Server/Knjiga/bookList.dat");
            ObjectOutputStream oos1 = new ObjectOutputStream(fos);
            oos1.writeObject(bookList);
            oos1.close();
            fos.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            //ovaj server jednostavno ispisuje koliko je klijenata trenutno povezano na njega...
            int clientCounter = 0;
            // slusaj zahteve na datom portu
            ServerSocket ss = new ServerSocket(TCP_PORT);
            System.out.println("Server running...");
            while (true) {
                //prihvataj klijente
                Socket sock = ss.accept();
                System.out.println("Client accepted: "
                        + (++clientCounter));
                //startuj nit za svakog klijenta
                ServerThread st = new ServerThread(sock, clientCounter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
        //pomocna metoda u kojoj cu kreirati 20 knjiga, dodati u arraylist u
    //eventualno ih rasporediti u serijale (imam Pesme leda i vatre i Gospodar prstenova
    public static void init() {
        ArrayList<Knjiga> serijali = new ArrayList<>();
        ArrayList<String> txtSummary = new ArrayList<>();
        String summary;
        txtSummary.add("basara.txt");
        txtSummary.add("buka.txt");
        txtSummary.add("cuvari.txt");
        txtSummary.add("demijan.txt");
        txtSummary.add("dorijan.txt");
        txtSummary.add("druzina_prstena.txt");
        txtSummary.add("dve_kule.txt");
        txtSummary.add("gozba_za_vrane.txt");
        txtSummary.add("igraPrestola.txt");
        txtSummary.add("lament.txt");
        txtSummary.add("lelejskaGora.txt");
        txtSummary.add("narcis.txt");
        txtSummary.add("nastasijevic_pesme.txt");
        txtSummary.add("oluja_maceva.txt");
        txtSummary.add("ples1.txt");
        txtSummary.add("po.txt");
        txtSummary.add("povratak_kralja.txt");
        txtSummary.add("psihoanaliza.txt");
        txtSummary.add("sudar_kraljeva.txt");
        txtSummary.add("tvrdjava.txt");

        try {
        ElektronskaKnjiga eBasara = new ElektronskaKnjiga(false, true, "Fama o biciklistima", "978-86-521-1090-2", "Svetislav Basara",
        334," ","Dereta", "src/GUI/Images/basara.jpg", 16.50, 20, "filozofsko - religijski roman, istorijska");
        //pomocu setera iz klase Knjiga, postavljam opis za odredjenu knjigu citajuci ga iz fajla i konvertujuci u string
        //moje "elegantno" rjesenje se malo zakomplikovalo :)
        eBasara.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(0))));

        ElektronskaKnjiga eIgraPrestola = new ElektronskaKnjiga(false, true, "Igra prestola", "978-86-743-6099-6", "Dzordz R.R. Martin",
        599,"","Laguna", "src/GUI/Images/igraPrestola.jpg", 20 , 2, "epska fantastika, politicka strategija");
        eIgraPrestola.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(7))));

        ElektronskaKnjiga eSudarKraljeva = new ElektronskaKnjiga(false, true,"Sudar Kraljeva", "978-86-743-6140-5", "Dzordz R.R. Martin",
        672,"","Laguna", "src/GUI/Images/sudar_kraljeva.jpg", 26.20 , 2, "epska fantastika, politicka strategija");
        eSudarKraljeva.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(18))));

        ElektronskaKnjiga eOlujaMaceva = new ElektronskaKnjiga(false, true, "Oluja maceva -deo prvi: Celik i sneg", "978-86-743-6185-6", "Dzordz R.R. Martin",
        384,"","Laguna", "src/GUI/Images/oluja_maceva.jpg", 20.50 , 2, "epska fantastika, politicka strategija");
        eOlujaMaceva.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(12))));

        ElektronskaKnjiga eGozbaZaVrane = new ElektronskaKnjiga(false, true,"Gozba za vrane -deo prvi", "978-86-743-6449-9", "Dzordz R.R. Martin",
        362,"","Laguna", "src/GUI/Images/gozba_za_vrane.jpg", 19.50 , 2, "epska fantastika, politicka strategija");
        eGozbaZaVrane.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(6))));

        ElektronskaKnjiga ePlesSaZmajevima = new ElektronskaKnjiga(false, true,"Ples sa zmajevima -deo prvi", "978-86-521-0914-2", "Dzordz R.R. Martin",
        592,"","Laguna", "src/GUI/Images/ples1.jpg", 22 , 2, "epska fantastika, politicka strategija");
        ePlesSaZmajevima.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(14))));

        ElektronskaKnjiga eDruzinaPrstena = new ElektronskaKnjiga(false, true,"Gospodar prstenova - Druzina prstena", "978-86-747-3506-0", "J.R.R. Tolkin",
        429,"","Moc knjige", "src/GUI/Images/druzina_prstena.jpg", 16.70 , 2, "epska fantastika");
        eDruzinaPrstena.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(5))));

        ElektronskaKnjiga eTvrdjava = new ElektronskaKnjiga(false, true,"Tvrdjava", "978-86-10-00973-6", "Mesa Selimovic",
        335,"","Vulkan", "src/GUI/Images/tvrdjava.jpg", 14.90 , 1, "klasicna knjizevnost");
        eTvrdjava.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(19))));

        ElektronskaKnjiga ePesme = new ElektronskaKnjiga(false, true,"Sedam lirskih krugova", "978-86-712-8003-7", "Momcilo Nastasijevic",
        101,"","Kairos", "src/GUI/Images/nastasijevic_pesme.jpg", 17 , 1, "poezija - simbolizam");
        ePesme.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(12))));

        ElektronskaKnjiga eSlikaDorijanaGreja = new ElektronskaKnjiga(false, true,"Slika Dorijana Greja", "978-86-10-00558-5", "Oskar Vajld",
        292,"","Vulkan", "src/GUI/Images/dorijan.jpg", 17 , 4, "filozofska fikcija, klasicna knjizevnost");
        eSlikaDorijanaGreja.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(4))));

        ElektronskaKnjiga eCuvari = new ElektronskaKnjiga(false, true,"Cuvari", "978-09-302-8923-2", "Alan Mur",
        464,"","Fibra", "src/GUI/Images/cuvari.jpg", 85 , 2, "graficke novele");
        eCuvari.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(2))));

        ElektronskaKnjiga eBukaIBes = new ElektronskaKnjiga(false, true,"Buka i Bes", "978-09-302-8923-2", "Vilijam Fokner",
        311,"","Vulkan", "src/GUI/Images/buka.jpg", 15 , 3, "moderna knjizevnost, knjizevnost 20. vijeka");
        eBukaIBes.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(1))));

        ElektronskaKnjiga eDemijan = new ElektronskaKnjiga(false, true,"Demijan", "978-99-402-5067-6", "Herman Hese",
        160,"","Vulkan", "src/GUI/Images/demijan.jpg", 9 , 7, "Novele, fikcija");
        eDemijan.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(3))));


        ElektronskaKnjiga eDvijeKule = new ElektronskaKnjiga(false, true,"Gospodar prstenova - Dvije Kule", "978-953-6166-12-1", "J.R.R. Tolkin",
        399,"","Moc knjige", "src/GUI/Images/dve_kule.jpg", 17.70 , 2, "epska fantastika");
        eDvijeKule.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(6))));

        ElektronskaKnjiga ePovratakKralja = new ElektronskaKnjiga(false, true,"Gospodar prstenova - Povratak kralja", "978-953-6166-13-8", "J.R.R. Tolkin",
        505,"","Moc knjige", "src/GUI/Images/povratak_kralja.jpg", 17.70 , 2, "epska fantastika");
        ePovratakKralja.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(16))));

        ElektronskaKnjiga eNarcisIZlatousti = new ElektronskaKnjiga(false, true, "Narcis i zlatousti", "978-99-402-5065-2", "Herman Hese",
        288,"","Narodna knjiga Podgorica", "src/GUI/Images/narcis.jpg", 9 , 7, "Novele, fikcija");
        eNarcisIZlatousti.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(11))));

        ElektronskaKnjiga ePrice = new ElektronskaKnjiga(false, true,"Ukradeno pismo i druge price", "978-86-867-6104-0", "Edgar Alan Po",
        273,"","Adresa", "src/GUI/Images/po.jpg", 13.50 , 7, "horor");
        ePrice.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(15))));

        ElektronskaKnjiga eLamentNadBeogradom = new ElektronskaKnjiga(false, true,"Lament nad Beogradom", "978-86-815-6719-7", "Milos Crnjanski",
        192,"","Tanesi", "src/GUI/Images/lament.jpg", 42 , 2, "poezija");
        eLamentNadBeogradom.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(9))));

        ElektronskaKnjiga eLelejskaGora = new ElektronskaKnjiga(false, true,"Lelejska gora", "978-86-010-1734-4", "Mihajlo Lalic",
        586,"","Rad", "src/GUI/Images/lelejskaGora.jpg", 20 , 3, "Ratni roman, filozofski roman");
        eLelejskaGora.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(10))));

        ElektronskaKnjiga ePsihoanaliza = new ElektronskaKnjiga(false, true,"Kompletan uvod u psihoanalizu", "978-86-747-0044-0", "Sigmund Frojd",
        524,"","Nova knjiga", "src/GUI/Images/psihoanaliza.jpg", 54 , 2, "psihoanaliza");
        ePsihoanaliza.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(17))));

//        ElektronskaKnjiga eSerijalPesmeLedaIVatre =  = new ElektronskaKnjiga(false, true,"Pesme leda i vatre", "", "Dzordz.R.R. Martin",
//        3000,"","Laguna", "src/GUI/Images/psihoanaliza.jpg", 54 , 2, "epska fantastika, politicka strategija");
//        ePsihoanaliza.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(17))));
        //dodavanje u listu koja ce sluziti za pretragu po naslovu
        listaKnjiga = new Knjiga[]{ eBasara, eIgraPrestola, eSudarKraljeva, eOlujaMaceva, eGozbaZaVrane, ePlesSaZmajevima,
        eDruzinaPrstena, eTvrdjava, ePesme, eSlikaDorijanaGreja, eCuvari, eBukaIBes, eDemijan, eDvijeKule, ePovratakKralja,
        eNarcisIZlatousti, ePrice, eLamentNadBeogradom, eLelejskaGora, ePsihoanaliza };
        Collections.addAll(bookList, listaKnjiga);


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
