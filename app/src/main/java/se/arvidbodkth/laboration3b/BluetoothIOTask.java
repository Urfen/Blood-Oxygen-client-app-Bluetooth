package se.arvidbodkth.laboration3b;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

/**
 * Created by arvidbodin on 07/12/15.
 *
 */
public class BluetoothIOTask extends AsyncTask<Void,Void,Void>{

    private BluetoothSocket socket;

    public BluetoothIOTask(BluetoothSocket socket){
        this.socket = socket;
    }

    @Override
    protected Void doInBackground(Void... params) {


        return null;
    }
}
