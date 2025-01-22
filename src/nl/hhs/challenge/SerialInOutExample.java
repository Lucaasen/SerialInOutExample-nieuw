package nl.hhs.challenge;

import com.serialpundit.serial.SerialComManager;
import com.serialpundit.serial.SerialComManager.BAUDRATE;
import com.serialpundit.serial.SerialComManager.DATABITS;
import com.serialpundit.serial.SerialComManager.FLOWCONTROL;
import com.serialpundit.serial.SerialComManager.PARITY;
import com.serialpundit.serial.SerialComManager.STOPBITS;
import java.sql.*;
import java.awt.*; // alleen voor het geluidje
import java.text.SimpleDateFormat; // omzetten voor datum en tijd naar iets leesbaar
import java.util.Date; // voor bepalen datum en tijd
import javax.swing.*; // voor de GUI
//import java.time.Clock; // voor de millis()

public class SerialInOutExample extends JFrame {
    public static SerialInOutExample serialInstance;
    private JPanel mainPanel;
    private JTextField inkomend;
    private JTextField uitgaand;
    private JButton verstuurButton;
    private String teVerzenden;
    public static GUI gui; // GUI-referentie om updates door te geven.
    public static void main(String[] args) {
        test();
//        new GUI1();
        JFrame frame = new SerialInOutExample("SerialInOutExample");
        frame.setVisible(true);
    }

    private static int waterPercentage = 0; // Houdt het huidige waterpercentage bij.
    public static void test () {



        Connection connection = null;
        try {
            // below two lines are used for connectivity.
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/vb1",
                    "root", "0020Ferron0500");

            // mydb is database
            // mydbuser is name of database
            // mydbuser is password of database

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(
                    "select * from micro_bit_v2");
            Float code;
            String title;
            while (resultSet.next()) {
                code = resultSet.getFloat("temperatuur");
//                title = resultSet.getString("tijdstip").trim();
                System.out.println("Code : " + code);
//                        + " Title : " + title);
            }
            resultSet.close();
            statement.close();
            connection.close();
            gui = new GUI(); // Maak een nieuwe GUI aan.
            gui.setWaterPercentage(waterPercentage); // Initieer met de startwaarde.
            gui.setVisible(false); // Laat de GUI pas zien na succesvol inloggen.

        }
        catch (Exception exception) {
            System.out.println(exception);
        }
    }
    public SerialInOutExample(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        verstuurButton.addActionListener(e -> {
            teVerzenden = uitgaand.getText();
            uitgaand.setText("");
        });
        // Stukje code om elke 2 seconden iets via seriële poort te verzenden (om te testen ofzo)
//        Clock clock = Clock.systemDefaultZone();
//        long millis = 0;
//        long vorigeMillis = 0;
//        int interval = 2000; // interval van verzenden

        // Begin van het "hoofdprogramma"
        InsertIntoSQL database = new InsertIntoSQL();   //Deze regel uitcommenten als SQL nog niet werkt.
        String port = "";

        try {
            SerialComManager scm = new SerialComManager();

            // Blok hieronder: automatisch de poort met de Microbit kiezen (werkt alleen voor Microbit).
            String[] poorten = scm.listAvailableComPorts();
            for (String poort : poorten) {
                port = poort;
                System.out.println("Poort " + port + " gekozen..."); // beschikbare poorten afdrukken
            }
            if (port.isEmpty()) {
                System.out.print("Geen Microbit gevonden!");
                System.exit(1); // Programma afbreken
            }

            // COM poort kun je ook hard invullen, zoek via Arduino of Device Manager uit welke COM poort je gebruikt:
            // long handle = scm.openComPort("COM3", true, true, true);

            long handle = scm.openComPort("COM6", true, true, true);
            scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B9600, 9600);
            scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
            scm.writeString(handle, "test", 0);
            this.setVisible(true); // de gui

            while (true) { // gewoon altijd doorgaan, vergelijkbaar met de Arduino loop()
                this.mainPanel.updateUI();

                // tijdstip = nu, dit moment.
                String tijdstip = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                tijdstip = tijdstip.replaceAll("[\\n\\r]", ""); // tijdstip om af te drukken, handig...

                // Stukje code om elke 2 seconden iets via seriële poort te verzenden (om te testen ofzo)
//                millis = clock.millis(); // tijdafhandeling op dezelfde manier als op Arduino/Microbit
//                if (millis > vorigeMillis + interval) {
//                    String dataVerzenden = "\n";
//                    scm.writeString(handle, dataVerzenden, 0);
////                    System.out.println(tijdstip + " Data verzonden: " + dataVerzenden);
//                    vorigeMillis = milli s;
//                }

                // Data verzenden via serieel
                if (teVerzenden != null) {
//                    scm.writeString(handle, teVerzenden, 0);
                    System.out.println(tijdstip + " Data verzonden: " + teVerzenden);
                    teVerzenden = null;
                }

                // Data ontvangen via serieel
                String dataOntvangen = scm.readString(handle);
                if (dataOntvangen != null) { // Er is data ontvangen
                    // verwijder alle newlines '\n' en carriage_returns '\r':
                    dataOntvangen = dataOntvangen.replaceAll("\\R.*", "");
                    System.out.println(tijdstip + " Ontvangen data: " + dataOntvangen);

                    this.inkomend.setText("Ontvangen op " + tijdstip + ": " + dataOntvangen);

                    // String naar float omzetten
                    Float afstand = Float.parseFloat(dataOntvangen);

                    // afronden op 1 cijfer achter de komma
//                    temperatuur = (float) (Math.round(temperatuur * 10.0) / 10.0);

                    //System.out.println("Float: " + temperatuur); // Kun je mee testen of er correct verstuurd wordt.
//                    database.insert(tijdstip, temperatuur);  //Deze regel uitcommenten als SQL nog niet werkt.

//                    if (dataOntvangen.contains("1")) { // Piepje als er een 1 gelezen wordt vanaf de seriële poort
//                        System.out.println("\"1\" ontvangen, dus: Windows default beep");
//                        Toolkit.getDefaultToolkit().beep(); // Piep


                    String url = "jdbc:mysql://localhost:3306/vb1";
                    String user = "root";
                    String password = "0020Ferron0500";
                    try {

                        double straal = 11;


                        int tankLiter = 12;
                        String Microbitnr = "M001";
                        float waterVolumeP = (float) ((straal * straal) * Math.PI);
                        float foutMarge = (float) 0.5;
                        String gebruiker = "G001";

                        Connection conn = DriverManager.getConnection(url, user, password);
                        Statement mystmt = conn.createStatement();
                        System.out.println(dataOntvangen);
//                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(Micro_Bit_V2_ID)" + "values('" + Microbitnr + "')");
//                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(Afstand_tot_water)" + "values(" + dataOntvangen + ")");
//                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(Liter_Tank)" + "values(" + tankLiter + ")");
//                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(Watervolume_Percentage)" + "values(" + waterVolumeP + ")");
//                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(Foutmarge)" + "values(" + foutMarge + ")");
//                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(gebruiker)" + "values('" + gebruiker + "')");

                        mystmt.executeUpdate("insert into vb1.micro_bit_v2" + "(Micro_Bit_V2_ID, Afstand_tot_water, Liter_Tank, Watervolume_Percentage, Foutmarge, gebruiker)" + "values('" + Microbitnr + "', " + dataOntvangen + ", " + tankLiter + ", " + waterVolumeP + ", " + foutMarge + ", '" + gebruiker + "')");


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
//                    }
                }
//            }
            // Ontvang waterpercentage via seriële data.
//            String dataOntvangen = scm.readString(handle);
//            String tijdstip = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            if (dataOntvangen != null) {
                dataOntvangen = dataOntvangen.replaceAll("\\R.*", ""); // Clean data.
                System.out.println(tijdstip + " Ontvangen data: " + dataOntvangen);

                try {
                    int nieuwPercentage = Integer.parseInt(dataOntvangen);
                    if (nieuwPercentage >= 0 && nieuwPercentage <= 100) {
                        waterPercentage = nieuwPercentage;
                        gui.setWaterPercentage(waterPercentage); // Update de GUI.
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ongeldige data ontvangen: " + dataOntvangen);
                }
            }
            boolean isRunning = true;

            try {
                while (isRunning) {

                    tijdstip = tijdstip.replaceAll("[\\n\\r]", "");
                    if (dataOntvangen != null) {
                        dataOntvangen = dataOntvangen.replaceAll("\\R.*", "");
                        System.out.println(tijdstip + " Ontvangen data: " + dataOntvangen);
                    }

                    // Stop de loop als een bepaald signaal wordt ontvangen
                    if (dataOntvangen.equalsIgnoreCase("STOP")) {
                        isRunning = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Seriële communicatie is gestopt.");
            }
}

        } catch (Exception e) { // Stukje foutafhandeling, wordt als het goed is nooit gebruikt
            System.out.print("\033[1;93m\033[41m"); // Dikke gele tekst in rode achtergrond (ANSI colors Java)
            System.out.print("Ai, er zit een fout in je programma. Kijk eerst naar de onderste rode foutmeldingen en werk omhoog:");
            System.out.println("\033[0m"); // Tekstkleuren weer resetten naar standaard.
            e.printStackTrace(); // Dit drukt de foutmeldingen af.
            System.exit(2); // Programma afbreken
        }
    }
}

