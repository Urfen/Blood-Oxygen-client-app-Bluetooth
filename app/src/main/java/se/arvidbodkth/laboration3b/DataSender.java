package se.arvidbodkth.laboration3b;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Arvid Bodin och Mattias Grehnik on 2015-12-06.
 *
 */
public class DataSender {

    private String ipAddress;
    private static final int port = 8008;
    private String data;

    public DataSender(String data){
        ipAddress = "192.168.0.14";
        //ipAddress = "130.229.183.203";
        this.data = data;
    }

    public void sendData(){
        try{
            Socket socket = new Socket(ipAddress, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream()
                    , true);

            out.println(data);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
