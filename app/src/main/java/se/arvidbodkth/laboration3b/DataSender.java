package se.arvidbodkth.laboration3b;

import android.nfc.FormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

/**
 * Created by Arvid Bodin och Mattias Grehnik on 2015-12-06.
 *
 */
public class DataSender {

    private String ipAddress;
    private static final int port = 8008;
    private String data;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public DataSender(String data){
        //ipAddress = "192.168.0.14";
        ipAddress = "130.229.183.203";
        this.data = data;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void sendData(){
        try{
            socket = new Socket(ipAddress,port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            out.println(data);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
