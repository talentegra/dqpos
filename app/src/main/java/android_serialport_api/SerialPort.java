package android_serialport_api;

import com.POSD.util.LogUtil;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = SerialPort.class.getSimpleName();
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private static native FileDescriptor open(String str, int i, int i2);

    private static native FileDescriptor open(String str, int i, int i2, char c, int i3, int i4);

    public native void close();

    public SerialPort(File device, int baudrate, int bits, char event, int stop, int flags) throws SecurityException, IOException {
        System.loadLibrary("serialport");
        //LogUtil.trace("device.canRead()=" + device.canRead() + "; device.canWrite()=" + device.canWrite());
        if (!(device.canRead() && device.canWrite())) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                su.getOutputStream().write(("chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n").getBytes());
                if (!(su.waitFor() == 0 && device.canRead() && device.canWrite())) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        this.mFd = open(device.getAbsolutePath(), baudrate, bits, event, stop, flags);
        //LogUtil.m12d(TAG, "open device success!!");
        if (this.mFd == null) {
            //LogUtil.m12d(TAG, "native open returns null");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public SerialPort(File device, int baudrate, int bits, char event, int stop, int flags, int mode) throws SecurityException, IOException {
        System.loadLibrary("serialport");
        //LogUtil.trace("device.canRead()=" + device.canRead() + "; device.canWrite()=" + device.canWrite());
        if (!(device.canRead() && device.canWrite())) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                su.getOutputStream().write(("chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n").getBytes());
                if (!(su.waitFor() == 0 && device.canRead() && device.canWrite())) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        this.mFd = open(device.getAbsolutePath(), baudrate, bits, event, stop, flags);
        //LogUtil.m12d(TAG, "open device success!!");
        if (this.mFd == null) {
            //LogUtil.m12d(TAG, "native open returns null");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        System.loadLibrary("serial_port");
        //LogUtil.trace("device.canRead()=" + device.canRead() + "; device.canWrite()=" + device.canWrite());
        if (!(device.canRead() && device.canWrite())) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                //LogUtil.trace("test for runtime exception...");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                //LogUtil.trace("device.getAbsolutePath()::" + device.getAbsolutePath());
                su.getOutputStream().write(cmd.getBytes());
                if (!(su.waitFor() == 0 && device.canRead() && device.canWrite())) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                //LogUtil.trace("runtime exception...");
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        //LogUtil.trace(device.getAbsolutePath() + "; " + baudrate + "; " + flags);
        this.mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (this.mFd == null) {
            //LogUtil.m12d(TAG, "native open returns null");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public SerialPort(boolean isSu, String path, File device, int baudrate, int flags) throws SecurityException, IOException {
        System.loadLibrary("serial_port");
        //LogUtil.trace("device.canRead()=" + device.canRead() + "; device.canWrite()=" + device.canWrite());
        if (!(device.canRead() && device.canWrite())) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                su.getOutputStream().write(("chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n").getBytes());
                if (!(su.waitFor() == 0 && device.canRead() && device.canWrite())) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        this.mFd = open(device.getAbsolutePath(), baudrate, flags);
        //LogUtil.m12d(TAG, "open device!!");
        if (this.mFd == null) {
            //LogUtil.m12d(TAG, "native open returns null");
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }
}
