package se.arvidbodkth.laboration3b;

import android.os.AsyncTask;

/**
 * Created by arvidbodin on 07/12/15.
 *
 */

public class TcpTask extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... params) {

        DataSender dataSender = new DataSender(params[0]);
        dataSender.sendData();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        System.out.println("TcpTask klar.");
    }
}
