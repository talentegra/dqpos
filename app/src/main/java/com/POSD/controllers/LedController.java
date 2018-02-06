package com.POSD.controllers;

import android_serialport_api.SerialPort;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class LedController {
    private static LedController ledController;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private SerialPort mSerialPort;

    public static LedController getInstance() {
        if (ledController == null) {
            ledController = new LedController();
        }
        return ledController;
    }

    private int ledCont(boolean cont) {
        try {
            String ctrl_gpio_path = "/dev/ctrl_gpio";
            byte[] buff = "00LED_CTL ".getBytes();
            buff[buff.length - 1] = (byte) 0;
            if (cont) {
                buff[1] = (byte) 49;
            } else {
                buff[1] = (byte) 48;
            }
            FileInputStream mCalfdIn = new FileInputStream(new File(ctrl_gpio_path));
            mCalfdIn.read(buff);
            mCalfdIn.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int LedController_Open() {
        try {
            this.mSerialPort = new SerialPort(new File("/dev/ttyS3"), 9600, 8, '0', 1, 0, 0);
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.mInputStream = this.mSerialPort.getInputStream();
            if (ledCont(true) == 0) {
                return 0;
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int LedController_Close() {
        try {
            if (this.mOutputStream != null) {
                this.mOutputStream.close();
                this.mOutputStream = null;
            }
            if (this.mInputStream != null) {
                this.mInputStream.close();
                this.mInputStream = null;
            }
            if (this.mSerialPort != null) {
                this.mSerialPort.close();
                this.mSerialPort = null;
            }
            if (ledCont(false) == 0) {
                return 0;
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int LedController_Close_ShowNums(String nums) {
        try {
            int i;
            byte[] showHead = new byte[]{(byte) 27, (byte) 81, (byte) 65};
            byte[] showContent = nums.getBytes();
            byte[] writeByte = new byte[(showContent.length + 4)];
            for (i = 0; i < showHead.length; i++) {
                writeByte[i] = showHead[i];
            }
            for (i = 0; i < showContent.length; i++) {
                writeByte[i + 3] = showContent[i];
            }
            writeByte[writeByte.length - 1] = (byte) 13;
            this.mOutputStream.write(writeByte);
            this.mOutputStream.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
