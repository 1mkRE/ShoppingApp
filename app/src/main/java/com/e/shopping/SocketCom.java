package com.e.shopping;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketCom {

    private static final String TAG = SocketCom.class.getSimpleName();
    public static final int BUFFER_SIZE = 1024;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    private String host = null;
    private String macAddress = null;
    private int port = 7999;
    private boolean error = false;

    public SocketCom(String host, int port, String macAddress) {
        this.host = host;
        this.port = port;
        this.macAddress = macAddress;
    }

    boolean getError(){
        return this.error;
    }

    private void connectWithServer() {
        //Log.i(TAG,"Entry connectWithServer");
        try {
            if (socket == null) {
                socket = new Socket(this.host, this.port);
                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            //Log.i(TAG,"Fehler",e);
        }
        //Log.i(TAG,"Austritt");
    }

    private void disConnectWithServer() {
        if (socket != null) {
            if (socket.isConnected()) {
                try {
                    in.close();
                    out.close();
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendDataWithString(String message) {
        //Log.i(TAG,"Entry sendDataWithString");
        if (message != null) {
            connectWithServer();
            if(socket != null) {
                if (socket.isConnected()) {
                    out.write(message);
                    out.flush();
                }
            }
        }
        //Log.i(TAG,"Exit sendDataWithString");
    }

    public String receiveDataFromServer() {
        try {
            String message = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];
            if (socket != null) {
                if(socket.isConnected()) {
                    if ((charsRead = in.read(buffer)) != -1) {
                        message += new String(buffer).substring(0, charsRead);
                    }
                    disConnectWithServer();
                    error = false;
                    return message;
                }
            }
            error = true;
            return "No data received";
        } catch (IOException e) {
            error = true;
            disConnectWithServer();
            return "Error receiving response:  " + e.getMessage();
        }
    }
}
