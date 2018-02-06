package com.POSD.controllers;

import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LaserlightController {
    private static final String TAG = "LaserlightController";
    private static LaserlightController laserlightController = null;
    private String power_j = "/proc/jbcommon/gpio_control/PosLight_CTL";
    private String power_n = "/proc/jbcommon/gpio_control/Led_CTL";

    public static LaserlightController getInstance() {
        Log.i(TAG, "getInstance");
        if (laserlightController == null) {
            laserlightController = new LaserlightController();
        }
        return laserlightController;
    }

    public int LaserlightController_J_Open() {
        Log.i(TAG, "LaserlightController_Open");
        int flag = writeFile(new File(this.power_j), "1");
        if (1 == flag) {
            return 1;
        }
        if (flag == 0) {
            return 0;
        }
        return 0;
    }

    public int LaserlightController_N_Open() {
        Log.i(TAG, "LaserlightController_Open");
        int flag = writeFile(new File(this.power_n), "1");
        if (1 == flag) {
            return 1;
        }
        if (flag == 0) {
            return 0;
        }
        return 0;
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

    public int LaserlightController_Close() {
        Log.i(TAG, "LaserlightController_Close");
        int flag1 = writeFile(new File(this.power_j), "0");
        int flag2 = writeFile(new File(this.power_n), "0");
        if (1 == flag1 && 1 == flag2) {
            return 1;
        }
        return 0;
    }

    public int LaserlightController_J_Close() {
        Log.i(TAG, "LaserlightController_J_Close");
        if (1 == writeFile(new File(this.power_j), "0")) {
            return 1;
        }
        return 0;
    }

    public int LaserlightController_N_Close() {
        Log.i(TAG, "LaserlightController_N_Close");
        if (1 == writeFile(new File(this.power_n), "0")) {
            return 1;
        }
        return 0;
    }
}
