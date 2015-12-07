package se.arvidbodkth.laboration3b;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

    public BluetoothIOTask(MainActivity activity, BluetoothDevice noninDevice){
        this.activity = activity;
        this.noninDevice = noninDevice;
        this.adapter = BluetoothAdapter.getDefaultAdapter();
    }


    @Override
    protected String doInBackground(Void... v) {
        String output = "";

        // an ongoing discovery will slow down the connection
        adapter.cancelDiscovery();

        BluetoothSocket socket = null;

        try {
            socket = noninDevice
                    .createRfcommSocketToServiceRecord(STANDARD_SPP_UUID);
            socket.connect();

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

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
                output = value1 + "; " + value2 + "\r\n";
            }
        } catch (Exception e) {
            output = e.getMessage();
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (Exception e) {
            }
        }

        return output;
    }

    private int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //System.out.println("Bytes: " + values[0].toString());
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        System.out.println("woop: " + s);
       // TcpTask tcpTask = new TcpTask();
       // tcpTask.execute(s);

    }
}
