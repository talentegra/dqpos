package com.POSD.controllers;


import android_serialport_api.SerialPort;
import com.POSD.util.MachineVersion;
import com.POSD.util.bs;
import com.imagepay.utils.RandomUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PrinterController {
    private static final byte[] Font_Bold = new byte[]{(byte) 27, (byte) 33, (byte) 8};
    private static final byte[] Font_Double_width = new byte[]{(byte) 27, (byte) 33, (byte) 32};
    private static final byte[] Font_Italics = new byte[]{(byte) 27, (byte) 33, (byte) 2};
    private static final byte[] Font_Normal_mode;
    private static final byte[] Font_Times = new byte[]{(byte) 27, (byte) 33, (byte) 16};
    private static final byte[] Font_Underline = new byte[]{(byte) 27, (byte) 33, Byte.MIN_VALUE};
    private static final byte[] Gray = new byte[]{(byte) 27, (byte) 109, (byte) 8};
    private static final byte Line_feed = (byte) 10;
    private static final byte[] PRINTE_CUT;
    private static final byte[] PRINTE_RESET = new byte[]{(byte) 27, (byte) 64};
    private static final byte[] PRINTE_TEST = new byte[]{(byte) 29, (byte) 40, (byte) 65};
    private static final byte[] Set_Center = new byte[]{(byte) 27, (byte) 97, (byte) 1};
    private static final byte[] Set_Left;
    private static final byte[] Set_Right = new byte[]{(byte) 27, (byte) 97, (byte) 2};
    private static final byte[] Set_Type = new byte[]{(byte) 16, (byte) 4, (byte) 5};
    private static final byte[] Take_The_Paper = new byte[]{(byte) 27, (byte) 74, (byte) 64};
    //private static Context context;
    private static final byte[] hSpeed = new byte[]{(byte) 28, (byte) 115, (byte) 2};
    private static final byte[] lSpeed;
    private static final byte[] mSpeed = new byte[]{(byte) 28, (byte) 115, (byte) 1};
    private static PrinterController printerController = null;
    private String IO_CS0 = "/proc/jbcommon/gpio_control/UART3_SEL0";
    private String IO_CS1 = "/proc/jbcommon/gpio_control/UART3_SEL1";
    private String IO_OE = "/proc/jbcommon/gpio_control/UART3_EN";
    private String IR_PWR_EN = "/proc/jbcommon/gpio_control/Printer_CTL";
    private String[] binaryArray = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    private String hexStr = RandomUtils.CHAR_HEX;
    private int language = 0;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private SerialPort mSerialPort;
    int mun1;
    private boolean pintimage = true;
    String version = MachineVersion.getMachineVersion().substring(0, 7);



    static {
        byte[] bArr = new byte[4];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 86;
        bArr[2] = (byte) 66;
        PRINTE_CUT = bArr;
        bArr = new byte[3];
        bArr[0] = (byte) 27;
        bArr[1] = (byte) 33;
        Font_Normal_mode = bArr;
        bArr = new byte[3];
        bArr[0] = (byte) 27;
        bArr[1] = (byte) 97;
        Set_Left = bArr;
        bArr = new byte[3];
        bArr[0] = (byte) 28;
        bArr[1] = (byte) 115;
        lSpeed = bArr;
    }

    /*public static PrinterController getInstance(Context contexts) {
        context = contexts;
        if (printerController == null) {
            printerController = new PrinterController();
        }
        return printerController;
    }*/

    public static PrinterController getInstance() {
        if (printerController == null) {
            printerController = new PrinterController();
        }
        return printerController;
    }

    public int PrinterController_Open() {
        try {
            writeFile(new File(this.IR_PWR_EN), "1");
            writeFile(new File(this.IO_OE), "0");
            writeFile(new File(this.IO_CS0), "0");
            writeFile(new File(this.IO_CS1), "1");
            Thread.sleep(100);
            this.mSerialPort = new SerialPort(new File("/dev/ttyS3"), 115200, 8, '0', 1, 0, 0);
            this.mOutputStream = this.mSerialPort.getOutputStream();
            this.mInputStream = this.mSerialPort.getInputStream();
            //new ReadThread().start();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void writeFile(File file, String value) {
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

    public int PrinterController_Close() {
        writeFile(new File(this.IR_PWR_EN), "0");
        writeFile(new File(this.IO_CS1), "0");
        try {
            if (this.mSerialPort == null) {
                return -1;
            }
            this.mOutputStream.close();
            this.mOutputStream = null;
            this.mInputStream.close();
            this.mInputStream = null;
            this.mSerialPort.close();
            this.mSerialPort = null;
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int PrinterController_reset() {
        return Write_Command(PRINTE_RESET);
    }

    public int Write_Command(byte[] command) {
        System.out.println(command.length);
        try {
            if (this.mOutputStream == null || command == null) {
                return -1;
            }
            this.mOutputStream.write(command);
            this.mOutputStream.flush();
            //Log.v("RS232Controller", "Rs232_Write:" + bytesToHexString(command, 0, command.length));
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*private String bytesToHexString(byte[] src, int start, int size) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || size <= 0) {
            return null;
        }
        for (int i = start; i < size; i++) {
            String hv = Integer.toHexString(src[i] & MotionEventCompat.ACTION_MASK);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }*/

    public int Write_Command(byte command) {
        try {
            if (this.mOutputStream == null) {
                return -1;
            }
            this.mOutputStream.write(command);
            this.mOutputStream.flush();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int PrinterController_Linefeed() {
        return Write_Command((byte) Line_feed);
    }

    public int PrinterController_Print(byte[] bytes) {
        int i;
        for (byte println : bytes) {
            System.out.println(println);
        }
        try {
            String str = new String(bytes);
            System.out.println(str);
            int couts = 0;
            int j=0;
            if (this.language == 0) {
                String[] strs = new String[str.length()];
                for (i = 0; i < str.length(); i++) {
                    strs[i] = str.substring(i, i + 1);
                }
                for (Object equals : strs) {
                    if ("\n".equals(equals)) {
                        PrinterController_Linefeed();
                    } else {
                        couts++;
                        this.mOutputStream.write(new byte[]{strs[j].getBytes("unicode")[3], strs[j].getBytes("unicode")[2]});
                    }
                }
                return 0;
            } else if (1 == this.language) {
                //Ajis fix
                /*char[] chars = str.toCharArray();
                for (j = 0; j < chars.length; j++) {
                    this.mOutputStream.write(chars[j]);
                }*/

                ArrayList strs16 = str16(str);
                for (j = 0; j < strs16.size(); j++) {
                    int a = ((Integer) strs16.get(j)).intValue();
                    if (10 == a) {
                        PrinterController_Linefeed();
                    } else {
                        this.mOutputStream.write(a);
                    }
                }
                return 0;
            } else {
                if (2 == this.language) {
                    this.mOutputStream.write(bytes);
                }
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*public int PrinterController_Bitmap(Bitmap bmp) {
        if (bmp == null) {
            return -1;
        }
        try {
            this.mOutputStream.write(decodeBitmap(bmp));
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }*/

   /* public int PrinterController_ImageAddCode(String ICname) {
        try {
            InputStream is = context.getResources().getAssets().open(ICname);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            is.close();
            if (bmp == null) {
                return -1;
            }
            byte[] command = decodeBitmap(bmp);
            Log.v("PrinterController", "command-----:" + bytesToHexString(command));
            this.mOutputStream.write(command);
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }*/

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 15, 16);
            buffer[1] = Character.forDigit(src[i] & 15, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    public int PrinterController_PrinterStatus() {
        int i = -1;
        if (this.mInputStream != null) {
            try {
                Write_Command(Set_Type);
                Thread.sleep(50);
                byte[] buffer = new byte[this.mInputStream.available()];
                this.mInputStream.read(buffer);
                i = buffer[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public int PrinterController_PrinterLanguage(int language) {
        this.language = language;
        return 0;
    }

    public int PrinterController_PrintText() {
        return Write_Command(PRINTE_TEST);
    }

    public int PrinterController_Cut() {
        return Write_Command(PRINTE_CUT);
    }

    public int PrinterController_Take_The_Paper(int l) {
        if (l < 1) {
            return -1;
        }
        int tf = -1;
        for (int i = 0; i < l; i++) {
            tf = Write_Command(Take_The_Paper);
        }
        return tf;
    }

    public int PrinterController_Gray(int i) {
        Gray[2] = (byte) i;
        return Write_Command(Gray);
    }

    public int PrinterController_Font_Normal_mode() {
        return Write_Command(Font_Normal_mode);
    }

    public int PrinterController_Font_Bold() {
        return Write_Command(Font_Bold);
    }

    public int PrinterController_Font_Double_width() {
        return Write_Command(Font_Double_width);
    }

    public int PrinterController_Font_Times() {
        return Write_Command(Font_Times);
    }

    public int PrinterController_Font_Underline() {
        return Write_Command(Font_Underline);
    }

    public int PrinterController_Set_Right() {
        return Write_Command(Set_Right);
    }

    public int PrinterController_Set_Left() {
        return Write_Command(Set_Left);
    }

    public int PrinterController_Set_Center() {
        return Write_Command(Set_Center);
    }

    public int PrinterController_Set_lowSpeed() {
        return Write_Command(lSpeed);
    }

    public int PrinterController_Set_highSpeed() {
        return Write_Command(hSpeed);
    }

    public int PrinterController_Set_midSpeed() {
        return Write_Command(mSpeed);
    }

    private ArrayList str16(String s) {
        int i;
        int[] str16s = new int[(s.length() + 1)];
        int[] str16sb = new int[(s.length() + 1)];
        ArrayList albstr = new ArrayList();
        String str = "";
        for (i = 0; i < s.length(); i++) {
            str16s[i] = s.charAt(i);
        }
        bs bs = new bs();
        boolean zj = false;
        if (bs.IsIncludeArbic(str16s) == 1) {
            zj = true;
        }
        if (zj) {
            bs.Arbic_Convert(str16s, str16sb);
        } else {
            str16sb = str16s;
        }
        ArrayList<Integer> sgb1 = new ArrayList();
        ArrayList<Integer> sgb2 = new ArrayList();
        for (i = 0; i < str16sb.length; i++) {
            if (i == str16sb.length - 1) {
                for (int i1 = sgb1.size(); i1 > 0; i1--) {
                    sgb2.add((Integer) sgb1.get(i1 - 1));
                }
            }
            if (str16sb[i] != 10) {
                sgb1.add(Integer.valueOf(str16sb[i]));
            } else {
                for (int c1 = sgb1.size(); c1 > 0; c1--) {
                    sgb2.add((Integer) sgb1.get(c1 - 1));
                }
                sgb1.clear();
                sgb2.add(Integer.valueOf(10));
            }
        }
        for (i = 0; i < sgb2.size(); i++) {
            if (((Integer) sgb2.get(i)).intValue() == 10) {
                albstr.add(sgb2.get(i));
            } else {
                albstr.add(Integer.valueOf(((Integer) sgb2.get(i)).intValue() / 256));
                albstr.add(Integer.valueOf(((Integer) sgb2.get(i)).intValue() % 256));
            }
        }
        return albstr;
    }

    /*private byte[] decodeBitmap(Bitmap bmp) {
        int i;
        int i2;
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        List<String> list = new ArrayList();
        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;
        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = (bmpWidth / 8) + 1;
            for (i = 0; i < 8 - zeroCount; i++) {
                zeroStr = new StringBuilder(String.valueOf(zeroStr)).append("0").toString();
            }
        }
        for (i = 0; i < bmpHeight; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);
                int g = (color >> 8) & MotionEventCompat.ACTION_MASK;
                int b = color & MotionEventCompat.ACTION_MASK;
                if (((color >> 16) & MotionEventCompat.ACTION_MASK) <= 160 || g <= 160 || b <= 160) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }
        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        if (bmpWidth % 8 == 0) {
            i2 = bmpWidth / 8;
        } else {
            i2 = (bmpWidth / 8) + 1;
        }
        String widthHexString = Integer.toHexString(i2);
        if (widthHexString.length() > 2) {
            return null;
        }
        if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = new StringBuilder(String.valueOf(widthHexString)).append("00").toString();
        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            return null;
        }
        if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = new StringBuilder(String.valueOf(heightHexString)).append("00").toString();
        List<String> commandList = new ArrayList();
        commandList.add(new StringBuilder(String.valueOf(commandHexString)).append(widthHexString).append(heightHexString).toString());
        commandList.addAll(bmpHexList);
        return hexList2Byte(commandList);
    }*/

    private byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList();
        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        return sysCopy(commandList);
    }

    private byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) RandomUtils.CHAR_HEX.indexOf(c);
    }

    private static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray2 : srcArrays) {
            System.arraycopy(srcArray2, 0, destArray, destLen, srcArray2.length);
            destLen += srcArray2.length;
        }
        return destArray;
    }

    private List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                sb.append(myBinaryStrToHexString(binaryStr.substring(i, i + 8)));
            }
            hexList.add(sb.toString());
        }
        return hexList;
    }

    private String myBinaryStrToHexString(String binaryStr) {
        int i;
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (i = 0; i < this.binaryArray.length; i++) {
            if (f4.equals(this.binaryArray[i])) {
                hex = new StringBuilder(String.valueOf(hex)).append(this.hexStr.substring(i, i + 1)).toString();
            }
        }
        for (i = 0; i < this.binaryArray.length; i++) {
            if (b4.equals(this.binaryArray[i])) {
                hex = new StringBuilder(String.valueOf(hex)).append(this.hexStr.substring(i, i + 1)).toString();
            }
        }
        return hex;
    }

   /* public boolean printBitmap(Bitmap bmp) {
        if (bmp == null) {
            return false;
        }
        try {
            int e = bmp.getWidth();
            int h = bmp.getHeight();
            int w = (e + 8) - (e % 8);
            byte[] head = new byte[]{(byte) 29, (byte) 118, (byte) 48, (byte) 48, (byte) ((w / 8) >> 0), (byte) ((w / 8) >> 8), (byte) (h >> 0), (byte) (h >> 8)};
            byte[] data = decodeBitmap(scaleBitmap(bmp, w, (w * bmp.getHeight()) / e));
            byte[] command = new byte[(head.length + data.length)];
            System.arraycopy(head, 0, command, 0, head.length);
            System.arraycopy(data, 0, command, head.length, data.length);
            Thread.sleep(1000);
            this.mOutputStream.write(command);
            return true;
        } catch (Exception var10) {
            var10.printStackTrace();
            return false;
        }
    }*/

   /* private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (origin.isRecycled()) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }*/
}
