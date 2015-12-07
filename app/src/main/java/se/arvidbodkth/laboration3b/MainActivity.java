package se.arvidbodkth.laboration3b;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart, buttonStop;
    private ScrollView scrollView;
    private TextView textView;
    private EditText editText;
    private DataSender model;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //BluetoothTask bluetoothTask = new BluetoothTask();
                //bluetoothTask.execute();
                textView.append(" " + i++ + "\n");

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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


    private class BluetoothTask extends AsyncTask<Void, Void, String> {

        private String NAME;
        private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private ArrayList<BluetoothDevice> arrayAdapter = new ArrayList<>();
        private BluetoothSocket socket;
        private InputStream in;
        private OutputStream out;


        @Override
        protected String doInBackground(Void... params) {

            //Anslut
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter != null) {
            }

            Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    // Show name and MAC address in a ListView
                    arrayAdapter.add(device);
                    NAME = device.getName();
                    publishProgress();
                }
            }

            try {

                socket = arrayAdapter.get(0).createRfcommSocketToServiceRecord(MY_UUID);

                //socket.connect();

                //BluetoothIOTask bluetoothIOTask = new BluetoothIOTask()

            }catch (IOException e){
                e.printStackTrace();
            }


            //Hämta BL data

            //Spara data

            return "Testtes123";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TcpTask tcpTask = new TcpTask();
            tcpTask.execute(s);

        }
    }
}

