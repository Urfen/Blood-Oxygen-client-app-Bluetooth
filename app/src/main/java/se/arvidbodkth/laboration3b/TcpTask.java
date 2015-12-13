package se.arvidbodkth.laboration3b;

import android.os.AsyncTask;

/**
 * Created by Arvid Bodin och Mattias Grehnik on 2015-12-06.
 *
 */

public class TcpTask extends AsyncTask<String,Void,Void> {


    /**
     * Starts a task that sends the data to
     * the given IP.
     * @param params the data
     * @return null;
     */
    @Override
    protected Void doInBackground(String... params) {

        DataSender dataSender = new DataSender(params[0]);
        dataSender.sendData();

        return null;
    }

    /**
     * When done let the user know.
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        System.out.println("TcpTask klar.");
    }
}
