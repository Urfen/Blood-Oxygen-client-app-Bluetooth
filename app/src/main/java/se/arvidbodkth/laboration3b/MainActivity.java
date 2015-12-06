package se.arvidbodkth.laboration3b;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private DataSender model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                BluetoothTask bluetoothTask = new BluetoothTask();
                bluetoothTask.execute();

            }
        });

    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class BluetoothTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... params) {

            //Hämta BL data

            //Spara data

            return "Testtes123";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TcpTask tcpTask = new TcpTask();
            tcpTask.execute(s);

        }
    }

    private class TcpTask extends AsyncTask<String,Void,Void>{

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
}

