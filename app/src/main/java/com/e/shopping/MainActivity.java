package com.e.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3;
    TextView tv1,tv2,tv3;
    CheckBox cb1,cb2,cb3;
    private final Handler mainHandler = new Handler();
    String message;
    String serverIP;
    int serverPort;
    final String SHARED_PREF = "NetworkSettings";
    final String SHARED_PREF_IP = "IP";
    final String SHARED_PREF_PORT = "Port";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.send_button);
        btn2 = (Button) findViewById(R.id.settings_menu);
        btn3 = (Button) findViewById(R.id.reset_button);
        tv1 = (TextView) findViewById(R.id.rec_txt_view);
        tv2 = (TextView) findViewById(R.id.messages_textview);
        tv3 = (TextView) findViewById(R.id.headline_textview);
        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        message = "";
        cb1.setEnabled(false);
        cb2.setEnabled(false);
        cb3.setEnabled(false);
        load_settings();

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DataProcessing dr = new DataProcessing();
                    String Shop[] = dr.manage(message,0);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    if(Shop != null) {
                        tv1.setText(recmessage(Shop));
                        tv3.setText(Shop[0]);
                    }
                    else{
                        tv1.setText("Data Error");
                    }
                }else{
                    tv1.setText("");
                    tv3.setText("Seznam");
                }

            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DataProcessing dr = new DataProcessing();
                    String Shop[] = dr.manage(message,1);
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    if(Shop != null) {

                        tv1.setText(recmessage(Shop));
                        tv3.setText(Shop[0]);
                    }
                    else{
                        tv1.setText("Data Error");
                    }
                } else{
                    tv1.setText("");
                    tv3.setText("Seznam");
                }
            }
        });


        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataProcessing dr = new DataProcessing();
                    String Shop[] = dr.manage(message,2);
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    if(Shop != null) {
                        tv1.setText(recmessage(Shop));
                        tv3.setText(Shop[0]);
                    }
                    else{

                        tv1.setText("Data Error");

                    }
                }else{
                    tv1.setText("");
                    tv3.setText("Seznam");
                }
            }
        });

    }

    class ComThread extends Thread{

        String command;

        public ComThread (String command){
            this.command = command;
        }

        @Override
        public void run() {
            SocketCom nc = new SocketCom(serverIP,serverPort, null);
            nc.sendDataWithString(command);
            message = nc.receiveDataFromServer();
            if(!nc.getError()) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        cb1.setEnabled(true);
                        cb2.setEnabled(true);
                        cb3.setEnabled(true);
                    }
                });
            }
        }
    }

    public void read_data(View v){
        ComThread cmt = new ComThread("read");
        cmt.start();
        }

    public void server_settings(View v){
        Intent intent_main2 = new Intent(this,MainActivity2.class);
        startActivity(intent_main2);
        finish();
    }

    public void reset(View v){
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
        tv1.setText("");
        tv2.setText("");
        tv3.setText("Seznam");
    }

    public void load_settings(){
        SharedPreferences sharedNetworkSettings = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        serverIP = sharedNetworkSettings.getString(SHARED_PREF_IP,"89.203.156.223");
        serverPort = sharedNetworkSettings.getInt(SHARED_PREF_PORT,9089);
    }

    public String recmessage(String [] array){
        int i = 0;
        int string_length = array.length;
        String received_message = "";
        for(i = 1; i < string_length;i ++ ){
            received_message += array[i]+"\n";
        }
        return received_message;
    }

    /*public void shop_select(View v){

        DataProcessing dr = new DataProcessing();
        boolean checked = ((CheckBox) v).isChecked();

        switch(v.getId()) {
            case R.id.checkBox1:
                if(checked){
                    String Shop[] = dr.manage(message,0);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    if(Shop != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv1.setText(recmessage(Shop));
                                tv3.setText(Shop[0]);
                            }
                        });
                    }
                    else{
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv1.setText("Data Error");
                            }
                        });
                    }
                }
                break;
            case R.id.checkBox2:
                if(checked){
                    String Shop[] = dr.manage(message,1);
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    if(Shop != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                tv1.setText(recmessage(Shop));
                                tv3.setText(Shop[0]);
                            }
                        });
                    }
                    else{
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv1.setText("Data Error");
                            }
                        });
                    }
                }
                break;
            case R.id.checkBox3:
                if (checked) {
                    String Shop[] = dr.manage(message,2);
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    if(Shop != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                tv1.setText(recmessage(Shop));
                                tv3.setText(Shop[0]);
                            }
                        });
                    }
                    else{
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv1.setText("Data Error");
                            }
                        });
                    }
                }
                break;
        }
    }*/
}

