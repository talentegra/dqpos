package com.POSD.controllers;

import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CashboxController {
    private static final String TAG = "CashboxController";
    private static CashboxController cashboxController = null;
    private String power = "/proc/jbcommon/gpio_control/MBox_CTL";

    public static CashboxController getInstance() {
        Log.i(TAG, "getInstance");
        if (cashboxController == null) {
            cashboxController = new CashboxController();
        }
        return cashboxController;
    }

    public int CashboxController_Controller(String value) {
        try {
            int flag = writeFile(new File(this.power), value);
            if (1 == flag) {
                Log.i(TAG, "CashboxController_Controller_Success");
                return 1;
            } else if (flag != 0) {
                return 0;
            } else {
                Log.i(TAG, "CashboxController_Controller_Have_No_Device");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "CashboxController_Controller_Failure");
            return 0;
        }
    }

    private static int writeFile(File file, String value) {
        IOException e1;
        try {
            FileWriter writer = new FileWriter(file);
            FileWriter fileWriter;
            try {
                writer.write(value);
                writer.flush();
                writer.close();
                fileWriter = writer;
                return 1;
            } catch (IOException e) {
                e1 = e;
                fileWriter = writer;
                e1.printStackTrace();
                return 0;
            }
        } catch (IOException e2) {
            e1 = e2;
            e1.printStackTrace();
            return 0;
        }
    }
}
