import java.io.*;
import java.net.*;
import java.util.*;

public class JokeServer {
        
        String jokefile = "witze.txt";
        LinkedList<String> jokes = null;
        int port = 0;
        String motd = ""; // Hier kann der Spruch des Tages hin.

        /**
         * Konstruktor - erzeuge einen neuen Witzeserver.
         * Beim Start werden direkt Witze aus einer Datei eingelesen.
         */
        public JokeServer() {
            this.jokes = parseJokes();
        }

        private void runServer() {
	    // Erzeugen Sie hier die notwendigen Sockets.
            // Verwenden Sie die oben definierte Klassenvariable "port", um den Port auf
            // dem der Server lauscht zu spezifizieren.

            try {
                // Rufen Sie hier die Methode "workOnClient()" auf, um einen Client
                // abzuarbeiten
                // ...
            } catch (Exception e) {
                System.out.println("FEHLER! Unbekannter Fehler:");
                System.out.println(e.getMessage());
            }
        }

        /**
         * Bearbeitung eines einzelnen Clients
         */
        private void workOnClient(Socket socket) throws Exception {
            // Hier muss das gewählte Protokoll implementiert werden.  Es mag
            // an dieser Stelle sinnvoll sein, je nach Kommando an eine
            // spezielle Methode (die Sie erstellen müssen) weiterzudelegieren
        }


        /**
         * Lies Witze aus einer Witzedatei. Einzelne Witze werden dabei durch eine
         * Leerzeile voneinander getrennt.
         */
        private LinkedList<String> parseJokes() {
            String witz = "";
            LinkedList<String> _jokes = new LinkedList<String>();

            try {
                Scanner in = new Scanner(new FileReader(jokefile));
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.equals("")) { // Witz beendet, neuer Witz
                        _jokes.add(witz);
                        witz = ""; // Witz zurücksetzen
                    } else { // Witz wird fortgesetzt
                        witz += line;
                    }
                }
                // Letzten Witz hinzufügen
                _jokes.add(witz);
            } catch (FileNotFoundException fnf) {
                System.out.println("FEHLER! Witzdatei konnte nicht gelesen werden!");
                System.out.println(fnf.getMessage());
                System.exit(-1);
            }

            return _jokes;
        }

        /**
         * Main-Methode -- starte den Server
         */
	public static void main(String args[]) { 
            JokeServer server = new JokeServer();
	    System.out.printf("Server gestartet\n");
            server.runServer();
	}

}
