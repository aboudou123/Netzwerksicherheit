import java.io.*;
import java.net.*;
import java.util.*;

public class JokeClient {

    private Socket socket = null;
    private PrintStream writer = null;
    private Scanner reader = null;
    private String name = "";

    /**
     * Innere Klasse - dient nur als Datenspeicher (wie "struct" in C)
     */
    private class Response {
        int errorcode;
        String command;
        String data;
    }

    /**
     * Jeder Client ist ein eigenes Objekt.
     * Damit hat auch jede Verbindung ein Objekt.
     * Jedes Kommando kann damit in eine eigene Methode/Funktion gekapselt werden
     */
    public JokeClient(String name) {
        this.name = name;
    }

    /**
     * Mit dem Server verbinden.
     * Hier können Fehler auftreten - darum ein "try-catch" Block
     */
    public void connect(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
            this.writer = new PrintStream(this.socket.getOutputStream());
            this.reader = new Scanner(this.socket.getInputStream());

            String gruss = "SERVUS " + this.name;
            this.writer.println(gruss);
            Response response = parseResponse();
        } catch (Exception e) {
            System.out.println("Connection failed!");
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Hole den Spruch des Tages vom Server
     */
    public String getSpruch() {
        this.writer.println("GET SPRUCH");
        Response response = parseResponse();
        return response.data;
    }

    /**
     * Hole einen Witz vom Server
     * @param num Die Nummer des Witzes
     */
    public String getWitz(int num) {
        this.writer.println("GET WITZ " + num);
        Response response = parseResponse();
        return response.data;
    }

    /**
     * Hier wird eine Antwort vom Server geparsed.
     * Anhand der Leerzeichen und Zeilenumbrüche werden die Elemente "Fehlercode", "Kommando"
     * und "Daten" voneinander unterschieden. Ein Zwinkersmiley signalisiert das Ende der
     * Nachricht.
     */
    public Response parseResponse() {
        Response response = new Response();
        StringTokenizer status = new StringTokenizer(this.reader.nextLine());
        String data = "";

        response.errorcode = Integer.parseInt(status.nextToken());
        response.command = status.nextToken();

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.equals(";)")) {
                break;
            } else {
                data += line + "\n";
            }
        }
        response.data = data;

        return response;
    }

    /**
     * Beende die Verbindung mit dem Server
     */
    public void close() {
        this.writer.println("HABEDEHRE");
        Response response = parseResponse();
        this.writer.close();
        this.reader.close();
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing!");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }


    /**
     * In der "main"-Methode steckt die Hauptschleife.
     * Hier findet die Benutzerinteraktion statt. Ein JokeClient Objekt wird zur Kommunikation
     * mit dem Server erzeugt und über vom Benutzer eingelesene Befehle gesteuert.
     */
    public static void main(String args[]) throws Exception {
        System.out.printf("Client gestartet\n");
        
        Scanner inFromUser = new Scanner(System.in);
        System.out.printf("Bitte IP-Adresse des Servers eingeben: ");
        String serverIP = inFromUser.nextLine();
        System.out.printf("Bitte Port Nummer des Servers eingeben: ");
        int serverPort = Integer.parseInt(inFromUser.nextLine());
        System.out.printf("Bitte Name eingeben: ");
        String name = inFromUser.nextLine();

        JokeClient myclient = new JokeClient(name);
        myclient.connect(serverIP, serverPort);

        System.out.printf("Client zu Server verbunden\n");

        boolean run = true;

        while (run) {
            System.out.println("Wählen Sie: [B]eenden, [S]pruch oder Witz Nummer [1-9]:");
            String cmd = inFromUser.nextLine();
            if (cmd.startsWith("S")) {
                System.out.println(myclient.getSpruch());
            } else if (cmd.startsWith("B")) {
                run = false;
            } else {
                int nummer = Integer.parseInt(cmd);
                System.out.println(myclient.getWitz(nummer));
            }
        }
        myclient.close();
        inFromUser.close();		

    }

}
