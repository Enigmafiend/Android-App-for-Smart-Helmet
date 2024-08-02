package com.example.bttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    static final UUID mUUID = UUID.fromString("55072829-bc9e-4c53-938a-74a6d4c78776");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
            return;
        }
        System.out.println(btAdapter.getBondedDevices());
        Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
        BluetoothDevice esp32 = btAdapter.getRemoteDevice("24:D7:EB:0F:BA:8E");
        System.out.println(esp32.getName());
        BluetoothSocket btSocket = null;
        do{
            try {
                btSocket = esp32.createRfcommSocketToServiceRecord(mUUID);
                System.out.println(btSocket);
                btSocket.connect();
                System.out.println(btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while(!btSocket.isConnected());

        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            byte b = (byte) inputStream.read();
            System.out.println((char) b);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}