package clientserverudp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Giuseppe Genovese
 */

public class UDPServer {
    DatagramSocket socket = null;

    public UDPServer() {
        try {
        socket = new DatagramSocket(1789);
        } catch (SocketException e) {
            System.err.println("Errore nel socket!");
    }
}

public void sendAndReceive() {
    try {
        byte[] incomingData = new byte[1024];

        while (true) {
            
            //Ricezione dello stream di byte
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            socket.receive(incomingPacket);
            
            byte[] data = incomingPacket.getData();
            
            //Deserializzazione: dallo stream di byte all'oggetto
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            
            try {
                Studente studente = (Studente) is.readObject();
                System.out.println("Ricevuto dal client oggetto Studente: \n" + studente + "\n");
            } catch (ClassNotFoundException e) {
                System.err.println("Classe Studente non definita");
            }


            // Ricezione della risposta
            DatagramPacket incomingPacket2 = new DatagramPacket(incomingData, incomingData.length);
            // datagramSOcket non socket
            socket.receive(incomingPacket2);
            String risposta = new String(incomingPacket2.getData(), 0, incomingPacket2.getLength());
            System.out.println("Messaggio dal Client: " + risposta);

        
            //Recupero dei dati del mittente
            InetAddress IPAddress = incomingPacket.getAddress();
            int port = incomingPacket.getPort();
            
            //Invio della risposta
            String risposta2 = "Grazie del messaggio!";
            byte[] replyByte = risposta2.getBytes();
            DatagramPacket replyPacket = new DatagramPacket(replyByte, replyByte.length, IPAddress, port);
            socket.send(replyPacket);
            
            Thread.sleep(1000);
            
            //Terminazione del processo Server
            //System.exit(0);
    }

    } catch (SocketException e) {
        System.err.println("Errore nel socket!");
    } catch (IOException i) {
        System.err.println("Errore di I/O!");
    } catch (InterruptedException e) {
        System.err.println("Errore nel thread sleeping!");
    }
}

public static void main(String[] args) {
        UDPServer server = new UDPServer();
        server.sendAndReceive();
    }
}