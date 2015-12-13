package se.arvidbodkth.laboration3b;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView dataView;

    private ArrayList<String> dataArray;

    public static final int REQUEST_ENABLE_BT = 42;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothDevice noninDevice = null;
    private BluetoothIOTask bluetoothIOTask;

    private File file;
    FileWriter fileWriter;
    BufferedWriter bufferedWriter;

    private  int i = 0;


    /**
     * Initialize UI and components, set
     * @param savedInstanceState state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dataView = (TextView) findViewById(R.id.textView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        dataView.addTextChangedListener(new TextWatcher() {
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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            showToast("This device do not support Bluetooth");
            this.finish();
        }

        //The data array.
        dataArray = new ArrayList<>();

        //Create a public file.
        file = new File(Environment.getExternalStorageDirectory(),
                "data.txt");

        openFileWriter();

    }

    /**
     * Initialize the bluetooth
     */
    @Override
    protected void onStart() {
        super.onStart();
        dataView.setText(R.string.data);
        initBluetooth();
    }

    /**
     * If there is a task runnig, stop it.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Turns off the bluetooth
        if(bluetoothIOTask != null) {
            bluetoothIOTask.cancel(true);
        }
    }

    /**
     * If there is a task runnig, stop it.
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Turns off the bluetooth
        if(bluetoothIOTask != null) {
            bluetoothIOTask.cancel(true);
        }
        // TODO: stop ongoing BT communication
    }


    /**
     * Sends the data to the server if the send button is pressed
     * @param view the view for the button.
     */
    public void onSendButtonClicked(View view){
        //Init the tcp task
        TcpTask tcpTask = new TcpTask();

        StringBuilder dataString = new StringBuilder();
        for (int i = 0; i < dataArray.size(); i++) {
            dataString.append(dataArray.get(i));
        }

        tcpTask.execute(dataString.toString());
    }

    /**
     * Execute background task to get data from bluetooth device, on button pressed
     * @param view the view for the button
     */
    public void onPollButtonClicked(View view) {
        if (noninDevice != null) {
            bluetoothIOTask = new BluetoothIOTask(this, noninDevice);
            bluetoothIOTask.execute();
        } else {
            showToast("No Nonin sensor found");

            //dataArray.add(String.valueOf(i++));
            //displayData(String.valueOf(i));
        }
    }

    /**
     * Stops the datatransfer from bluetooth, turns bluetooth off
     * @param view the view for the button
     */
    public void onStopButtonClicked(View view) {
        if(bluetoothIOTask != null) {
            bluetoothIOTask.cancel(true);
        }

        closeFileWriter();
    }

    /**
     * Adds the new data to the display and saves the pleth.
     * @param data new data.
     */
    protected void displayData(CharSequence data) {
        String splitData = data.toString();
        String[] splitedData = splitData.split(";");

        dataView.append(splitedData[0] + "\n");
        dataArray.add(splitedData[1] + ";");
        writeToFile(splitedData[1] + "\n");
    }

    /**
     * Creates the fileWriter.
     */
    public void openFileWriter() {
        try {
            //open output stream
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(new Date().toString() + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Colses the fileWriter.
     */
    public void closeFileWriter() {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes data to the file.
     * @param data the data to write.
     */
    public void writeToFile(String data) {

            if(bufferedWriter != null) {
            try {
                bufferedWriter.write(data);
            } catch (IOException e) {
                openFileWriter();
                try {
                    bufferedWriter.write(data);
                }catch (IOException io){
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Creates the BL adapter.
     */
    private void initBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            getNoninDevice();
        }
    }

    /**
     * Checks if the bluetooth is turned on or not, if it is
     * it gets the noine device.
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param result result
     */
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (bluetoothAdapter.isEnabled()) {
                getNoninDevice();
            } else {
                showToast("Bluetooth is turned off.");
            }
        }
    }

    /**
     * Gets the paired the devices. If one of the
     * devices has the name "nonon" it connects to it.
     */
    private void getNoninDevice() {
        noninDevice = null;
        Set<BluetoothDevice> pairedBTDevices = bluetoothAdapter
                .getBondedDevices();
        if (pairedBTDevices.size() > 0) {
            // the last Nonin device, if any, will be selected...
            for (BluetoothDevice device : pairedBTDevices) {
                String name = device.getName();
                if (name.contains("Nonin")) {
                    noninDevice = device;
                    showToast("Paired device: " + name);
                    return;
                }
            }
        }
        if (noninDevice == null) {
            showToast("No paired Nonin devices found!\r\n"
                    + "Please pair a Nonin BT device with this device.");
        }
    }

    /**
     * Show a toast to the user with a given message.
     * @param msg the message to show the user.
     */
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Creates the options menu. "Not used"
     * @param menu menu XML
     * @return true;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * When the item in the settings menu is hit.
     * @param item the item ID.
     * @return ?
     */
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


}

