package se.arvidbodkth.laboration3b;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Created by arvidbodin on 07/12/15.
 */
public class BluetoothIOTask extends AsyncTask<Void, String, String> {

    private static final UUID STANDARD_SPP_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final byte[] FORMAT = {0x44, 0x31};
    private static final byte ACK = 0x06; // ACK from Nonin sensor

    private MainActivity activity;
    private BluetoothDevice noninDevice;
    private BluetoothAdapter adapter;
    private Context context;
    private InputStream is;
    private OutputStream os;
    private BluetoothSocket socket;
    int i = 0;

    public BluetoothIOTask(MainActivity activity, BluetoothDevice noninDevice, Context context){
        this.context = context;
        this.activity = activity;
        this.noninDevice = noninDevice;
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        this.socket  = null;

    }


    @Override
    protected String doInBackground(Void... v) {
        String output = "";

        // an ongoing discovery will slow down the connection
        adapter.cancelDiscovery();




        socket = null;
        System.out.println(noninDevice.getName() + noninDevice);
        try {
            socket = noninDevice
                    .createRfcommSocketToServiceRecord(STANDARD_SPP_UUID);
            socket.connect();

            is = socket.getInputStream();
            os = socket.getOutputStream();

            while (!isCancelled() && i < 10) {
                os.write(FORMAT);
                os.flush();
                byte[] reply = new byte[1];
                is.read(reply);

                if (reply[0] == ACK) {
                    byte[] frame = new byte[4]; // this -obsolete- format specifies
                    // 4 bytes per frame
                    is.read(frame);
                    int value1 = unsignedByteToInt(frame[1]);
                    int value2 = unsignedByteToInt(frame[2]);
                    output ="Puls: " + value1 + " Syre:  " + value2 + "\r\n";


                    publishProgress(output);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                is.close();
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return output;
    }


    private int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

            try {
                socket.close();
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        activity.displayData(values[0]);

    }

    @Override
    protected void onPostExecute(String output) {
        super.onPostExecute(output);

        activity.writeToFile();
       // TcpTask tcpTask = new TcpTask();
       // tcpTask.execute(s);

    }

}
