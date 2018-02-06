package com.POSD.controllers;

import android.util.Log;
import android_serialport_api.SerialPort;
import com.POSD.interfaces.RS232ReadCallback;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RJ11SerialportController {
    private static final String TAG = "RS232Controller";
    private static RJ11SerialportController rs232Controller = null;
    private boolean begin;
    private int mBaud;
    private int mBits;
    private char mEvent;
    private String mFile;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private ReadThread mReadThread = null;
    private SerialPort mSerialPort = null;
    private int mStopBits;
    private RS232ReadCallback rs232ReadCallback;

    private class ReadThread extends Thread {
        private ReadThread() {
        }

        public void run() {
            while (RJ11SerialportController.this.begin) {
                try {
                    Thread.sleep(100);
                    int couth = 0;
                    if (RJ11SerialportController.this.mInputStream != null) {
                        int coutq = RJ11SerialportController.this.mInputStream.available();
                        while (coutq != couth) {
                            Thread.sleep(10);
                            couth = RJ11SerialportController.this.mInputStream.available();
                            Thread.sleep(10);
                            coutq = RJ11SerialportController.this.mInputStream.available();
                        }
                        byte[] buffer1 = new byte[couth];
                        if (RJ11SerialportController.this.mInputStream.read(buffer1) > 0 && RJ11SerialportController.this.rs232ReadCallback != null) {
                            RJ11SerialportController.this.rs232ReadCallback.RS232_Read(buffer1);
                        }
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static RJ11SerialportController getInstance() {
        Log.i(TAG, "getInstance");
        if (rs232Controller == null) {
            rs232Controller = new RJ11SerialportController();
        }
        return rs232Controller;
    }

    public int Rs232_Open(String file, int baud, int bits, char event, int stopBits, RS232ReadCallback l) {
        Log.i(TAG, "Rs232_Open");
        this.mFile = file;
        this.mBaud = baud;
        this.mBits = bits;
        this.mEvent = event;
        this.mStopBits = stopBits;
        this.rs232ReadCallback = l;
        try {
            this.mSerialPort = new SerialPort(new File(this.mFile), this.mBaud, this.mBits, this.mEvent, this.mStopBits, 0, 1);
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.mInputStream = this.mSerialPort.getInputStream();
            this.begin = true;
            this.mReadThread = new ReadThread();
            this.mReadThread.start();
            return 0;
        } catch (SecurityException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e2) {
            e2.printStackTrace();
            return -1;
        }
    }

    private void doRead() {
        try {
            if (this.mSerialPort != null && this.mInputStream != null) {
                byte[] buffer = new byte[this.mInputStream.available()];
                int size = this.mInputStream.read(buffer);
                Thread.sleep(450);
                if (size > 0 && this.rs232ReadCallback != null) {
                    this.rs232ReadCallback.RS232_Read(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Rs232_Write(final byte[] command) {
        if (this.mOutputStream != null && this.mSerialPort != null) {
            new Thread() {
                public void run() {
                    try {
                        if (command == null) {
                            RJ11SerialportController.this.mOutputStream.write("".getBytes());
                            RJ11SerialportController.this.mOutputStream.flush();
                        } else {
                            RJ11SerialportController.this.mOutputStream.write(command);
                            RJ11SerialportController.this.mOutputStream.flush();
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public void Rs232_Close() {
        this.begin = false;
        if (this.mOutputStream != null || this.mInputStream != null) {
            try {
                this.mOutputStream.close();
                this.mInputStream.close();
                this.mOutputStream = null;
                this.mInputStream = null;
                if (this.mSerialPort != null) {
                    this.mSerialPort.close();
                    this.mSerialPort = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
