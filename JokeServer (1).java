import java.io.*;
import java.net.*;
import java.util.*;

public class JokeServer {
        
        String jokefile = "witze.txt";
        LinkedList<String> jokes = null;
        int port = 8765;
        String motd = "I can haz ProtocolServer?!"; // Hier kann der Spruch des Tages hin.

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
                ServerSocket srvsocket = new ServerSocket(port);

                while (true) {
                    Socket cltsocket = srvsocket.accept();

                    // Rufen Sie hier die Methode "workOnClient()" auf, um einen Client
                    // abzuarbeiten
                    // ...
                    workOnClient(cltsocket);
                }
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
            PrintStream writer = new PrintStream(socket.getOutputStream());
            Scanner reader = new Scanner(socket.getInputStream());

            String clientname = "";
            boolean session = true;

            while (session) {
                String command = reader.nextLine();
                if (command.startsWith("SERVUS ")) {
                    clientname = command.substring(7);
                    System.out.println("Client verbunden: " + clientname);
                    writer.println("200 SERVUS");
                    writer.println();
                    writer.println(";)");
                } else if (command.equals("GET SPRUCH")) {
                    writer.println("200 SPRUCH");
                    writer.println(this.motd);
                    writer.println(";)");
                } else if (command.startsWith("GET WITZ ")) {
                    int nummer = Integer.parseInt(command.substring(9));
                    String witz = this.jokes.get(nummer);
                    writer.println("200 WITZ");
                    writer.println(witz);
                    writer.println(";)");
                } else if (command.equals("HABEDEHRE")) {
                    writer.println("200 HABEDEHRE");
                    writer.println();
                    writer.println(";)");
                    session = false;
                } else {
                    writer.println("400 SYNTAX ERROR");
                    writer.println("Did not understand: " + command);
                    writer.println(";)");
                }
            }
            writer.close();
            reader.close();
            socket.close();
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
