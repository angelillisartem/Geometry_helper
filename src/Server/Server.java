package Server;

import java.io.*;
import java.net.*;

public class Server {

    private final ServerSocket serverSocket;

    public Server() throws IOException {
        serverSocket = new ServerSocket(14345);
    }

    public void start() throws IOException {
        System.out.print("\nServer avviato.");
        System.out.print("\nIn attesa di connessioni...");

        while (true) {
            // Accetta una nuova connessione
            Socket socket = serverSocket.accept();
            System.out.print("\nConnessione accettata da: " + socket.getInetAddress() + " sulla porta " + socket.getPort());

            // Crea un thread per gestire la connessione
            Thread thread = new Thread(new ClientHandler(socket));
            thread.start();
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Legge dal client la richiesta
                InputStream inputStream = socket.getInputStream();
                byte[] data = new byte[1024];
                int bytesRead = inputStream.read(data);

                // Decodifica la richiesta
                String richiesta = new String(data, 0, bytesRead);
                System.out.print("\nRichiesta ricevuta dal client: " + richiesta);

                // Calcola l'area della figura geometrica richiesta
                float area = 0;
                if (richiesta.equals("quadrato")) {
                    // Leggi il lato dal client
                    inputStream.read(data);
                    int lato = Integer.parseInt(new String(data, 0, bytesRead));
                    area = lato * lato;
                } else if (richiesta.equals("rettangolo")) {
                    // Leggi la base dal client
                    inputStream.read(data);
                    int base = Integer.parseInt(new String(data, 0, bytesRead));
                    // Leggi l'altezza dal client
                    inputStream.read(data);
                    int altezza = Integer.parseInt(new String(data, 0, bytesRead));
                    area = base * altezza;
                } else if (richiesta.equals("cerchio")) {
                    // Leggi il raggio dal client
                    inputStream.read(data);
                    float raggio = Float.parseFloat(new String(data, 0, bytesRead));
                    area = (float) Math.PI * raggio * raggio;
                }

                // Invia al client il valore dell'area calcolata
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(Float.toString(area).getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace(); // Gestione dell'errore: stampa la traccia dell'errore
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
