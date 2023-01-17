import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class FahrkartenAutomat {
    private static final Scanner tastatur = new Scanner(System.in);
    private static double zuZahlenderBetrag;
    private static double eingezahlterGesamtbetrag = 0.0;
    private static double eingeworfeneMuenze;
    private static double preis = 0.0;
    private static int[] waehrungen = new int[10];
    private static double gewinn;
    private static double kasse;
    private static final String EURO = "Euro";
    private static final String CENT = "Cent";

    public static void main(String[] args) {
        initKasse();
        fahrkartenbestellungErfassen();
    }


    public static void fahrkartenbestellungErfassen() {
        class Fahrkarten {
            final double[] preis;
            final String[] bezeichnung;
            int counter;


            public Fahrkarten(int n) {
                preis = new double[n];
                bezeichnung = new String[n];
                counter = 0;
            }

            public void add(double preis, String bezeichnung) {
                this.preis[counter] = preis;
                this.bezeichnung[counter] = bezeichnung;
                counter++;
            }

            public void add(double[] preise, String[] bezeichnungen) {
                if (this.preis.length - counter < preise.length) {
                    throw new IllegalArgumentException(" zu groß für das array");
                }
                for (int i = 0; i < preise.length; i++) {
                    add(preise[i], bezeichnungen[i]);
                }
            }

            public void get(int index) {
                System.out.println("Ticket :" + bezeichnung[index] + "\nPreis : [ " + preis[index] + " ]");
            }

            public void printOut() {
                LocalDate now = LocalDate.now();
                System.out.printf("Herzlich Willkommen, \t\t\t\t\t\t\t%s%n%n%nFahkartenbestellvorgang:%n========================%n",
                        now.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                System.out.println("Wählen Sie ihre Wunschfahrkarte für Berlin AB aus:\n");
                for (int i = 0; i < counter; i++) {
                    System.out.printf("Ticket :%40s  Preis : [ %02.2f € ] bitte eingabe: (%d)%n", bezeichnung[i], preis[i], i);
                }
            }
        }

        Fahrkarten fahrkarten = new Fahrkarten(100);
        double[] zahlen = {2.90, 3.30, 3.60, 1.90, 8.60, 9.00, 9.60, 23.50, 24.30, 24.90};
        String[] bezeichnungen = {
                "Einzelfahrschein Berlin AB ",
                "Einzelfahrschein Berlin BC",
                "Einzelfahrschein Berlin ABC",
                "Kurzstrecke",
                "Tageskarte Berlin AB",
                "Tageskarte Berlin BC",
                "Tageskarte Berlin ABC",
                "Kleingruppen-Tageskarte Berlin AB",
                "Kleingruppen-Tageskarte Berlin BC",
                "Kleingruppen-Tageskarte Berlin ABC"
        };
        fahrkarten.add(zahlen, bezeichnungen);
        fahrkarten.printOut();
        boolean aliveTicketWahl = true;
        while (aliveTicketWahl) {

            int tarifWahl = tastatur.nextInt();
            if (tarifWahl >= fahrkarten.counter) {
                System.err.println("\nUngültige Fahrkarten Auswahl\n");
            }
            System.out.println("Ihre Wahl: " + tarifWahl);
            preis += fahrkarten.preis[tarifWahl];

            boolean aliveTicketAnzahl = true;
            while (aliveTicketAnzahl) {
                System.out.print("Anzahl der Tickets (Maximal 10): ");
                int anzahlTickets = tastatur.nextInt();
                if (anzahlTickets > 0 && anzahlTickets <= 10) {
                    zuZahlenderBetrag = anzahlTickets * preis;
                    aliveTicketAnzahl = false;
                } else {
                    System.err.println("\nUngültige Angabe von Tickets, bitte erneut eingeben!\n");
                }
            }
            System.out.println("Zwischensumme:          " + preis + " €");
            System.out.println("Weitere Karten bestellen (Y/N)?");
            if (tastatur.next().equals("N")) {
                aliveTicketWahl = false;
            }
            System.out.println("Wählen Sie ihre Wunschfahrkarte für Berlin AB aus:\n");
        }

        geldeinwurf();

    }

    private static void initKasse() {
        for (int i = 0; i < waehrungen.length; i++) {
            waehrungen[0] = 100;
        }
    }

    public static void geldeinwurf() {
        while (eingezahlterGesamtbetrag < zuZahlenderBetrag) {
            System.out.format("Noch zu zahlen: %4.2f €%n", (zuZahlenderBetrag - eingezahlterGesamtbetrag));
            System.out.print("Eingabe (mind. 5Ct, höchstens 50 Euro): ");
            String eingabe = tastatur.next();
            if (eingabe.equals("adminmenü")) {
                adminKonsole();
            }
            eingeworfeneMuenze = Double.parseDouble(eingabe);
            if (!(eingeworfeneMuenze == 50.0 || eingeworfeneMuenze == 20.0 || eingeworfeneMuenze == 10.0 || eingeworfeneMuenze == 5.0 || eingeworfeneMuenze == 2.0 || eingeworfeneMuenze == 1.0 || eingeworfeneMuenze == 0.5 || eingeworfeneMuenze == 0.2 || eingeworfeneMuenze == 0.1 || eingeworfeneMuenze == 0.05)) {
                System.err.println("\nKann nicht angenommen werden, bitte in Währungen bis 50 Euro\n");
            }
            if (eingeworfeneMuenze <= 0.0) {
                System.err.println("\nBitte eine Münze einwerfen, nicht probieren sie herauszufischen!\n");
            } else {
                eingezahlterGesamtbetrag += eingeworfeneMuenze;
                kasse += eingeworfeneMuenze;
            }
        }
        fahrscheinausgabe();
    }

    private static void adminKonsole() {
        System.out.println("");
        int command = tastatur.nextInt();
        switch (command) {
            case 1: {
                initKasse();
                break;
            }
            case 2: {
                gewinn += kasse;
                kasse = 0;
                break;
            }
            case 3: {

                break;
            }
            case 4: {

                break;
            }
        }
    }

    private static void fahrscheinausgabe() {
        System.out.println("\nFahrschein wird ausgegeben");
        warte(250);

        System.out.println("\n");
        rueckgabeBerechnung();
    }

    private static void rueckgabeBerechnung() {
        double rueckgabebetrag = eingezahlterGesamtbetrag - zuZahlenderBetrag;
        System.out.println("\nVergessen Sie nicht, den Fahrschein\n" +
                "vor Fahrtantritt entwerten zu lassen!\n" +
                "Wir wünschen Ihnen eine gute Fahrt.\n\n");
        warte(250);
        if (rueckgabebetrag > 0.0) {
            System.out.format("Der Rückgabebetrag in Höhe von %4.2f € %n", rueckgabebetrag);
            System.out.println("wird wie folgendend ausgezahlt:");
            warte(200);
            while (rueckgabebetrag >= 50.0 && waehrungen[9] > 0) {
                scheinAusgeben(50.0);
                rueckgabebetrag -= 50.0;
                waehrungen[9]--;
            }
            while (rueckgabebetrag >= 20.0 && waehrungen[8] > 0) {
                scheinAusgeben(20.0);
                rueckgabebetrag -= 20.0;
                waehrungen[8]--;
            }
            while (rueckgabebetrag >= 10.0 && waehrungen[7] > 0) {
                scheinAusgeben(10.0);
                rueckgabebetrag -= 10.0;
                waehrungen[7]--;
            }
            while (rueckgabebetrag >= 5.0 && waehrungen[6] > 0) {
                scheinAusgeben(5.0);
                rueckgabebetrag -= 5.0;
                waehrungen[6]--;
            }
            while (rueckgabebetrag >= 2.0 && waehrungen[5] > 0) {
                muenzeAusgeben(2, EURO);
                rueckgabebetrag -= 2.0;
                waehrungen[5]--;
            }
            while (rueckgabebetrag >= 1.0 && waehrungen[4] > 0) {
                muenzeAusgeben(1, EURO);
                rueckgabebetrag -= 1.0;
                waehrungen[4]--;
            }
            while (rueckgabebetrag >= 0.5 && waehrungen[3] > 0) {
                muenzeAusgeben(50, CENT);
                rueckgabebetrag -= 0.5;
                waehrungen[3]--;
            }
            while (rueckgabebetrag >= 0.2 && waehrungen[2] > 0) {
                muenzeAusgeben(20, CENT);
                rueckgabebetrag -= 0.2;
                waehrungen[2]--;
            }
            while (rueckgabebetrag >= 0.1 && waehrungen[1] > 0) {
                muenzeAusgeben(10, CENT);
                rueckgabebetrag -= 0.1;
                waehrungen[1]--;
            }
            while (rueckgabebetrag >= 0.05 && waehrungen[0] > 0) {
                muenzeAusgeben(5, CENT);
                rueckgabebetrag -= 0.05;
                waehrungen[0]--;
            }
            if (rueckgabebetrag > 0.0) {
                System.err.println("\n\nbitte admin rufen, keine Münzen mehr vorhanden\n\n");
                String eingabe = tastatur.next();
                if (eingabe.equals("admin")) {
                    adminKonsole();
                }
            }
        }
        eingeworfeneMuenze = 0.0;
        zuZahlenderBetrag = 0.0;
        eingezahlterGesamtbetrag = 0.0;
        fahrkartenbestellungErfassen();
    }

    private static void scheinAusgeben(double rueckgabebetrag) {
        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *");
        System.out.println("*                                *");
        System.out.println("*             " + (int) rueckgabebetrag + "  \t             *");
        System.out.println("*            Euro                *");
        System.out.println("*                                *");
        System.out.println("*  *  *  *  *  *  *  *  *  *  *  *");
    }

    private static void muenzeAusgeben(double rueckgabebetrag, String waehrung) {
        System.out.println("     *  *  *");
        System.out.println("   *         *");
        System.out.println(" *     " + (int) rueckgabebetrag + "\t   *");
        System.out.println(" *    " + waehrung + "     *");
        System.out.println("   *         *");
        System.out.println("     *  *  *");
    }


    public static void warte(int millis) {
        for (int i = 0; i < 8; i++) {
            System.out.print("=");
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("\n");

    }


}
