package com.e.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity2 extends AppCompatActivity{

    private final Handler mainHandler = new Handler();
    Button btnSafeSettings,btnServerConCheck, btnServerRT,btnBack;
    CheckBox cbSettings;
    EditText edtServerIP,edtServerPort;
    TextView tvMessage;
    String serverIP;
    int serverPort;
    final String SHARED_PREF = "NetworkSettings";
    final String SHARED_PREF_IP = "IP";
    final String SHARED_PREF_PORT = "Port";
    String status_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSafeSettings = (Button) findViewById(R.id.btnSafeSettings);
        btnServerConCheck = (Button) findViewById(R.id.btnConnCheck);
        btnServerRT = (Button) findViewById(R.id.btnRTCheck);
        cbSettings = (CheckBox) findViewById(R.id.cbSettings);
        edtServerIP = (EditText) findViewById(R.id.edtServerIP);
        edtServerPort = (EditText) findViewById(R.id.edtServerPort);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        status_message = "";
        edtServerIP.setEnabled(false);
        edtServerPort.setEnabled(false);
        load_settings();
        edtServerIP.setText(serverIP);
        edtServerPort.setText(String.valueOf(serverPort));
    }

    class ServerCheckThread extends Thread{

        String command = "";

        public ServerCheckThread(String command){
            this.command = command;
        }

        @Override
        public void run() {
            SocketCom sc = new SocketCom(serverIP,serverPort,null);
            sc.sendDataWithString(command);
            status_message = sc.receiveDataFromServer();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    tvMessage.setText(status_message);
                }
            });
        }
    }

    public void safe_settings(View v){
        SharedPreferences sharedNetworkSettings = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedNetworkSettings.edit();
        editor.putString(SHARED_PREF_IP,edtServerIP.getText().toString());
        editor.putInt(SHARED_PREF_PORT,Integer.parseInt(edtServerPort.getText().toString()));
        editor.commit();
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    public void load_settings(){
        SharedPreferences sharedNetworkSettings = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        serverIP = sharedNetworkSettings.getString(SHARED_PREF_IP,"89.203.156.223");
        serverPort = sharedNetworkSettings.getInt(SHARED_PREF_PORT,9089);
    }

    public void change_settings(View v){
        boolean checked = ((CheckBox) v).isChecked();
        if(checked){
            edtServerIP.setEnabled(true);
            edtServerPort.setEnabled(true);
            btnSafeSettings.setEnabled(false);
        }else{
            edtServerIP.setEnabled(false);
            edtServerPort.setEnabled(false);
            btnSafeSettings.setEnabled(true);
        }
    }

    public void server_check(View v){
        MainActivity2.ServerCheckThread sct = new ServerCheckThread("check");
        sct.start();
    }

    public void server_runtime(View v){
        MainActivity2.ServerCheckThread sct = new ServerCheckThread("pctime");
        sct.start();
    }

    public void change_menu(View v){
        Intent intent_main_activity = new Intent(this,MainActivity.class);
        startActivity(intent_main_activity);
        finish();
    }

    public void reset(View v){
        status_message = "";
        tvMessage.setText("");
    }

}