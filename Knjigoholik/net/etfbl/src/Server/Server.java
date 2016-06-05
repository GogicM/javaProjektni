package Server;

import Knjiga.ElektronskaKnjiga;
import Knjiga.Knjiga;
import Knjiga.PisanaKnjiga;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;

/**
 *
 * @author Milan
 */
public class Server {
    //podesavamo port na kom ce server osluskivati zahtjeve od klijenata
    public static final int TCP_PORT = 9000;
    public static final String path = "src/Knjiga/Opis/";
    public static Knjiga[] listaKnjiga;
    public static void main(String[] args) {
        init();
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
        HashMap bookHash = new HashMap();
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
        PisanaKnjiga pBasara = new PisanaKnjiga("Fama o biciklistima", "978-86-521-1090-2", "Svetislav Basara",
        334," ","Dereta", "src/GUI/Images/basara.jpg", 16.50, 3, "filozofsko - religijski roman, istorijska");
        //pomocu setera iz klase Knjiga, postavljam opis za odredjenu knjigu citajuci ga iz fajla i konvertujuci u string
        //moje "elegantno" rjesenje se malo zakomplikovalo :)
        pBasara.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(0))));

        PisanaKnjiga pIgraPrestola = new PisanaKnjiga("Igra prestola", "978-86-743-6099-6", "Dzordz R.R. Martin",
        599,"","Laguna", "src/GUI/Images/igra_prestola.jpg", 20 , 2, "epska fantastika, politicka strategija");
        pIgraPrestola.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(7))));

        PisanaKnjiga pSudarKraljeva = new PisanaKnjiga("Sudar Kraljeva", "978-86-743-6140-5", "Dzordz R.R. Martin",
        672,"","Laguna", "src/GUI/Images/sudar_kraljeva.jpg", 26.20 , 2, "epska fantastika, politicka strategija");
        pSudarKraljeva.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(18))));

        PisanaKnjiga pOlujaMaceva = new PisanaKnjiga("Oluja maceva -deo prvi: Celik i sneg", "978-86-743-6185-6", "Dzordz R.R. Martin",
        384,"","Laguna", "src/GUI/Images/oluja_maceva.jpg", 20.50 , 2, "epska fantastika, politicka strategija");
        pOlujaMaceva.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(12))));

        PisanaKnjiga pGozbaZaVrane = new PisanaKnjiga("Gozba za vrane -deo prvi", "978-86-743-6449-9", "Dzordz R.R. Martin",
        362,"","Laguna", "src/GUI/Images/gozba_za_vrane.jpg", 19.50 , 2, "epska fantastika, politicka strategija");
        pGozbaZaVrane.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(6))));

        PisanaKnjiga pPlesSaZmajevima = new PisanaKnjiga("Ples sa zmajevima -deo prvi", "978-86-521-0914-2", "Dzordz R.R. Martin",
        592,"","Laguna", "src/GUI/Images/ples1.jpg", 22 , 2, "epska fantastika, politicka strategija");
        pPlesSaZmajevima.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(14))));

        PisanaKnjiga pDruzinaPrstena = new PisanaKnjiga("Gospodar prstenova - Druzina prstena", "978-86-747-3506-0", "J.R.R. Tolkin",
        429,"","Moc knjige", "src/GUI/Images/druzina_prstena.jpg", 16.70 , 2, "epska fantastika");
        pDruzinaPrstena.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(5))));

        PisanaKnjiga pTvrdjava = new PisanaKnjiga("Tvrdjava", "978-86-10-00973-6", "Mesa Selimovic",
        335,"","Vulkan", "src/GUI/Images/tvrdjava.jpg", 14.90 , 1, "klasicna knjizevnost");
        pTvrdjava.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(19))));

        PisanaKnjiga pPesme = new PisanaKnjiga("Sedam lirskih krugova", "978-86-712-8003-7", "Momcilo Nastasijevic",
        101,"","Kairos", "src/GUI/Images/nastasijevic_pesme.jpg", 17 , 1, "poezijija - simbolizam");
        pPesme.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(12))));

        PisanaKnjiga pSlikaDorijanaGreja = new PisanaKnjiga("Slika Dorijana Greja", "978-86-10-00558-5", "Oskar Vajld",
        292,"","Vulkan", "src/GUI/Images/dorijan.jpg", 17 , 4, "filozofska fikcija, klasicna knjizevnost");
        pSlikaDorijanaGreja.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(4))));

        ElektronskaKnjiga eCuvari = new ElektronskaKnjiga("Cuvari", "978-09-302-8923-2", "Alan Mur",
        464,"","Fibra", "src/GUI/Images/basara.jpg", 85 , 2, "graficke novele");
        eCuvari.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(2))));

        ElektronskaKnjiga eBukaIBes = new ElektronskaKnjiga("Buka i Bes", "978-09-302-8923-2", "Vilijam Fokner",
        311,"","Vulkan", "src/GUI/Images/buka.jpg", 15 , 3, "moderna knjizevnost, knjizevnost 20. vijeka");
        eBukaIBes.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(1))));

        ElektronskaKnjiga eDemijan = new ElektronskaKnjiga("Demijan", "978-99-402-5067-6", "Herman Hese",
        160,"","Vulkan", "src/GUI/Images/demijan.jpg", 9 , 7, "Novele, fikcija");
        eDemijan.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(3))));


        ElektronskaKnjiga eDvijeKule = new ElektronskaKnjiga("Gospodar prstenova - Dvije Kule", "978-953-6166-12-1", "J.R.R. Tolkin",
        399,"","Moc knjige", "src/GUI/Images/dve_kule.jpg", 17.70 , 2, "epska fantastika");
        eDvijeKule.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(6))));

        ElektronskaKnjiga ePovratakKralja = new ElektronskaKnjiga("Gospodar prstenova - Povratak kralja", "978-953-6166-13-8", "J.R.R. Tolkin",
        505,"","Moc knjige", "src/GUI/Images/povratak_kralja.jpg", 17.70 , 2, "epska fantastika");
        ePovratakKralja.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(16))));

        ElektronskaKnjiga eNarcisIZlatousti = new ElektronskaKnjiga("Narcis i zlatousti", "978-99-402-5065-2", "Herman Hese",
        288,"","Narodna knjiga Podgorica", "src/GUI/Images/narcis.jpg", 9 , 7, "Novele, fikcija");
        eNarcisIZlatousti.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(11))));

        ElektronskaKnjiga ePrice = new ElektronskaKnjiga("Ukradeno pismo i druge price", "978-86-867-6104-0", "Edgar Alan Po",
        273,"","Adresa", "src/GUI/Images/po.jpg", 13.50 , 7, "horor");
        ePrice.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(15))));

        ElektronskaKnjiga eLamentNadBeogradom = new ElektronskaKnjiga("Lament nad Beogradom", "978-86-815-6719-7", "Milos Crnjanski",
        192,"","Tanesi", "src/GUI/Images/lament.jpg", 42 , 1, "Poezija");
        eLamentNadBeogradom.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(9))));

        ElektronskaKnjiga eLelejskaGora = new ElektronskaKnjiga("Lelejska gora", "978-86-010-1734-4", "Mihajlo Lalic",
        586,"","Rad", "src/GUI/Images/lelejskaGora.jpg", 20 , 3, "Ratni roman, filozofski roman");
        eLelejskaGora.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(10))));

        ElektronskaKnjiga ePsihoanaliza = new ElektronskaKnjiga("Kompletan uvod u psihoanalizu", "978-86-747-0044-0", "Sigmund Frojd",
        524,"","Nova knjiga", "src/GUI/Images/psihoanaliza.jpg", 54 , 2, "psihoanaliza");
        ePsihoanaliza.setSummary(Knjiga.fromFileToString(new File(path + txtSummary.get(17))));

        //dodavanje u listu koja ce sluziti za pretragu po naslovu
        listaKnjiga = new Knjiga[]{ pBasara, pIgraPrestola, pSudarKraljeva, pOlujaMaceva, pGozbaZaVrane, pPlesSaZmajevima,
        pDruzinaPrstena, pTvrdjava, pPesme, pSlikaDorijanaGreja, eCuvari, eBukaIBes, eDemijan, eDvijeKule, ePovratakKralja,
        eNarcisIZlatousti, ePrice, eLamentNadBeogradom, eLelejskaGora, ePsihoanaliza };
        
        serijali.add(pIgraPrestola);
        serijali.add(pSudarKraljeva);
        serijali.add(pOlujaMaceva);
        serijali.add(pGozbaZaVrane);
        serijali.add(pPlesSaZmajevima);
        serijali.add(pDruzinaPrstena);
        serijali.add(eDvijeKule);
        serijali.add(ePovratakKralja);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
