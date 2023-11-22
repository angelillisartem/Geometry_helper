package Client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.print("\nAvvio del client...");

        // Crea una socket per connettersi al server
        Socket socket = new Socket("localhost", 14345);
        System.out.println("\nConnessione al server effettuata sulla porta " + socket.getPort());

        // Crea un flusso di input dal server
        InputStream inputStream = socket.getInputStream();

        // Crea un flusso di output verso il server
        OutputStream outputStream = socket.getOutputStream();

        // Crea una variabile casuale
        Random random = new Random();

        // Seleziona la figura geometrica da calcolare
        int figura = random.nextInt(3);

        // Invia al server la richiesta di calcolo dell'area della figura selezionata
        switch (figura) {
            case 0:
                // Calcoli per il quadrato
                int lato = random.nextInt(10) + 1; // Genera un lato casuale tra 1 e 10
                outputStream.write(("quadrato " +  " lato " + lato).getBytes());
                break;
            case 1:
                // Calcoli per il rettangolo
                int base = random.nextInt(10) + 1; // Genera una base casuale tra 1 e 10
                int altezza = random.nextInt(10) + 1; // Genera un'altezza casuale tra 1 e 10
                outputStream.write(("rettangolo " +  " base " + base + ";" +  " altezza " + altezza).getBytes());
                break;
            case 2:
                // Calcoli per il cerchio
                float raggio = random.nextFloat() * 10; // Genera un raggio casuale tra 0 e 10
                outputStream.write(("cerchio:" + " raggio " + raggio).getBytes());
                break;
        }
        outputStream.flush();

        // Legge dal server il valore dell'area calcolata
        byte[] data = new byte[1024];
        int bytesRead = inputStream.read(data);

        // Converte il valore dell'area in un numero
        float area = Float.parseFloat(new String(data, 0, bytesRead));

        //Stampa l'avviso di invio dati al server
        System.out.print("\nDati inviati al server");

        // Chiude la socket
        socket.close();
    }
}
