package com.POSD.controllers;

import android_serialport_api.SerialPort;
import com.POSD.interfaces.RS232ReadCallback;
import com.POSD.util.LogUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RS232Controller {
    private static final String TAG = RS232Controller.class.getSimpleName();
    private static RS232Controller rs232Controller = null;
    private String IO_CS0 = "/proc/jbcommon/gpio_control/UART3_SEL0";
    private String IO_CS1 = "/proc/jbcommon/gpio_control/UART3_SEL1";
    private String IO_OE = "/proc/jbcommon/gpio_control/UART3_EN";
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
    private String power = "/proc/jbcommon/gpio_control/Finger_CTL";
    private RS232ReadCallback rs232ReadCallback;

    private class ReadThread extends Thread {
        private ReadThread() {
        }

        public void run() {
            while (RS232Controller.this.begin) {
                try {
                    Thread.sleep(100);
                    int couth = 0;
                    if (RS232Controller.this.mInputStream != null) {
                        int coutq = RS232Controller.this.mInputStream.available();
                        while (coutq != couth) {
                            Thread.sleep(10);
                            couth = RS232Controller.this.mInputStream.available();
                            Thread.sleep(10);
                            coutq = RS232Controller.this.mInputStream.available();
                        }
                        byte[] buffer1 = new byte[couth];
                        if (RS232Controller.this.mInputStream.read(buffer1) > 0 && RS232Controller.this.rs232ReadCallback != null) {
                            RS232Controller.this.rs232ReadCallback.RS232_Read(buffer1);
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

    public static RS232Controller getInstance() {
        LogUtil.m12d(TAG, "getInstance");
        if (rs232Controller == null) {
            rs232Controller = new RS232Controller();
        }
        return rs232Controller;
    }

    public int Rs232_Open(String file, int baud, int bits, char event, int stopBits, RS232ReadCallback l) {
        LogUtil.m12d(TAG, "Rs232_Open");
        this.mFile = file;
        this.mBaud = baud;
        this.mBits = bits;
        this.mEvent = event;
        this.mStopBits = stopBits;
        this.rs232ReadCallback = l;
        try {
            this.mSerialPort = new SerialPort(new File(file), this.mBaud, this.mBits, this.mEvent, this.mStopBits, 0, 1);
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

    public void Rs232_Close() {
        LogUtil.m12d(TAG, "Rs232_Close");
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

    public void Rs232_Write(final byte[] command) {
        //LogUtil.m12d(TAG, "Rs232_Write::" + Tools.bytesToHexString(command));
        if (command != null) {
            for (int i = 0; i < command.length; i++) {
                LogUtil.m12d(TAG, "[" + i + "]=" + command[i]);
            }
        }
        if (this.mOutputStream != null && this.mSerialPort != null) {
            new Thread() {
                public void run() {
                    try {
                        if (command == null) {
                            RS232Controller.this.mOutputStream.write("".getBytes());
                            RS232Controller.this.mOutputStream.flush();
                        } else {
                            RS232Controller.this.mOutputStream.write(command);
                            RS232Controller.this.mOutputStream.flush();
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void writeFile(File file, String value) {
        IOException e1;
        try {
            FileWriter writer = new FileWriter(file);
            FileWriter fileWriter;
            try {
                writer.write(value);
                writer.flush();
                writer.close();
                fileWriter = writer;
            } catch (IOException e) {
                e1 = e;
                fileWriter = writer;
                e1.printStackTrace();
            }
        } catch (IOException e2) {
            e1 = e2;
            e1.printStackTrace();
        }
    }

    private void power_up() {
        writeFile(new File(this.power), "1");
        writeFile(new File(this.IO_OE), "0");
        writeFile(new File(this.IO_CS0), "0");
        writeFile(new File(this.IO_CS1), "0");
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
}
