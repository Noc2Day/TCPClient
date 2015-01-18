package net.david_bauer.tcpclient;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by David on 17.01.2015.
 */
public class TCPClientClass {
    private Socket clientSocket;
    private String Hostname;
    private int Port;
    public TCPClientClass(String hostname, Integer port) throws Exception {
        clientSocket = new Socket(hostname, port);
        Port = port;
        Hostname = hostname;
    }
    public String sendToServer(String toServer) throws Exception {
        String modifiedSentence;
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(toServer + '\n');
        modifiedSentence = inFromServer.readLine();
        Log.d("TCPClientClass", "FROM SERVER: " + modifiedSentence);

        return modifiedSentence;
    }

    public void closeSocket() {
        try {
            clientSocket.close();
        } catch (Exception e) {
             // TODO
        }
    }
}
