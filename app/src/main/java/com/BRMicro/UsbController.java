package com.BRMicro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.widget.Toast;
import java.util.Arrays;

@SuppressLint({"NewApi"})
public class UsbController {
    protected static final String ACTION_USB_PERMISSION = "ch.serverbox.android.USB";
    public static final String TAG = "USBController";
    private final int PID;
    private final int VID;
    private final Context mApplicationContext;
    private final IUsbConnState mConnectionHandler;
    private BroadcastReceiver mPermissionReceiver = new PermissionReceiver(new C01741());
    private final UsbManager mUsbManager;
    private byte[] m_abyTransferBuf;
    private boolean m_bInit = false;
    private UsbEndpoint m_epIN = null;
    private UsbEndpoint m_epOUT = null;
    private int m_nEPInSize;
    private int m_nEPOutSize;
    private UsbDeviceConnection m_usbConn = null;
    private UsbDevice m_usbDevice;
    private UsbInterface m_usbIf = null;

    private interface IPermissionListener {
        void onPermissionDenied(UsbDevice usbDevice);
    }

    private class PermissionReceiver extends BroadcastReceiver {
        private final IPermissionListener mPermissionListener;

        public PermissionReceiver(IPermissionListener permissionListener) {
            this.mPermissionListener = permissionListener;
        }

        public void onReceive(Context context, Intent intent) {
            UsbController.this.mApplicationContext.unregisterReceiver(this);
            if (!intent.getAction().equals(UsbController.ACTION_USB_PERMISSION)) {
                return;
            }
            if (intent.getBooleanExtra("permission", false)) {
                UsbController.this.m1l("Permission granted");
                UsbDevice dev = (UsbDevice) intent.getParcelableExtra("device");
                if (dev == null) {
                    UsbController.this.mConnectionHandler.onDeviceNotFound();
                    return;
                } else if (dev.getVendorId() == UsbController.this.VID && dev.getProductId() == UsbController.this.PID) {
                    UsbController.this.GetConnInerface(dev);
                    return;
                } else {
                    return;
                }
            }
            this.mPermissionListener.onPermissionDenied((UsbDevice) intent.getParcelableExtra("device"));
            UsbController.this.mConnectionHandler.onUsbPermissionDenied();
        }
    }

    class C01741 implements IPermissionListener {
        C01741() {
        }

        public void onPermissionDenied(UsbDevice d) {
            UsbController.this.m1l("Permission denied on " + d.getDeviceId());
        }
    }

    class C01752 implements IPermissionListener {
        C01752() {
        }

        public void onPermissionDenied(UsbDevice d) {
            UsbManager usbman = (UsbManager) UsbController.this.mApplicationContext.getSystemService("usb");
            PendingIntent pi = PendingIntent.getBroadcast(UsbController.this.mApplicationContext, 0, new Intent(UsbController.ACTION_USB_PERMISSION), 0);
            UsbController.this.mApplicationContext.registerReceiver(UsbController.this.mPermissionReceiver, new IntentFilter(UsbController.ACTION_USB_PERMISSION));
            usbman.requestPermission(d, pi);
        }
    }

    public UsbController(Activity parentActivity, IUsbConnState connectionHandler, int vid, int pid) {
        this.mConnectionHandler = connectionHandler;
        this.mApplicationContext = parentActivity.getApplicationContext();
        this.mUsbManager = (UsbManager) this.mApplicationContext.getSystemService("usb");
        this.VID = vid;
        this.PID = pid;
        this.m_abyTransferBuf = new byte[512];
    }

    public void init() {
        enumerate(new C01752());
    }

    public void uninit() {
        if (this.m_usbConn != null) {
            this.m_usbConn.releaseInterface(this.m_usbIf);
            this.m_usbConn.close();
            this.m_usbConn = null;
            this.m_bInit = false;
        }
    }

    public void stop() {
        try {
            this.mApplicationContext.unregisterReceiver(this.mPermissionReceiver);
        } catch (IllegalArgumentException e) {
        }
    }

    public boolean IsInit() {
        return this.m_bInit;
    }

    private void enumerate(IPermissionListener listener) {
        boolean bFound = false;
        m1l("enumerating");
        for (UsbDevice d : this.mUsbManager.getDeviceList().values()) {
            m1l("Found device: " + String.format("%04X:%04X", new Object[]{Integer.valueOf(d.getVendorId()), Integer.valueOf(d.getProductId())}));
            if (d.getVendorId() == this.VID && d.getProductId() == this.PID) {
                bFound = true;
                m1l("Device under: " + d.getDeviceName());
                if (this.mUsbManager.hasPermission(d)) {
                    GetConnInerface(d);
                    return;
                }
                listener.onPermissionDenied(d);
                if (!bFound) {
                    Toast.makeText(this.mApplicationContext, "no more devices found", 0).show();
                    this.mConnectionHandler.onDeviceNotFound();
                }
            }
        }
        if (!bFound) {
            Toast.makeText(this.mApplicationContext, "no more devices found", 0).show();
            this.mConnectionHandler.onDeviceNotFound();
        }
    }

    private void GetConnInerface(UsbDevice dev) {
        this.m_usbDevice = dev;
        this.m_usbConn = this.mUsbManager.openDevice(dev);
        if (dev.getInterfaceCount() > 0 && this.m_usbConn.claimInterface(dev.getInterface(0), true)) {
            this.m_usbIf = dev.getInterface(0);
            int n = this.m_usbIf.getEndpointCount();
            if (n >= 2) {
                for (int i = 0; i < n; i++) {
                    if (this.m_usbIf.getEndpoint(i).getType() == 2) {
                        if (this.m_usbIf.getEndpoint(i).getDirection() == 128) {
                            this.m_epIN = this.m_usbIf.getEndpoint(i);
                        } else {
                            this.m_epOUT = this.m_usbIf.getEndpoint(i);
                        }
                    }
                }
                this.m_nEPInSize = this.m_epIN.getMaxPacketSize();
                this.m_nEPOutSize = this.m_epOUT.getMaxPacketSize();
                this.m_bInit = true;
                this.mConnectionHandler.onUsbConnected();
            }
        }
    }

    public boolean OperationInternal(byte[] pData, int nDataLen, int nTimeOut, boolean bRead) {
        byte[] w_abyTmp = new byte[31];
        byte[] w_abyCSW = new byte[13];
        Arrays.fill(w_abyTmp, (byte) 0);
        w_abyTmp[0] = (byte) 85;
        w_abyTmp[1] = (byte) 83;
        w_abyTmp[2] = (byte) 66;
        w_abyTmp[3] = (byte) 67;
        w_abyTmp[4] = (byte) 40;
        w_abyTmp[5] = (byte) 43;
        w_abyTmp[6] = (byte) 24;
        w_abyTmp[7] = (byte) -119;
        w_abyTmp[8] = (byte) (nDataLen & MotionEventCompat.ACTION_MASK);
        w_abyTmp[9] = (byte) ((nDataLen >> 8) & MotionEventCompat.ACTION_MASK);
        w_abyTmp[10] = (byte) ((nDataLen >> 16) & MotionEventCompat.ACTION_MASK);
        w_abyTmp[11] = (byte) ((nDataLen >> 24) & MotionEventCompat.ACTION_MASK);
        if (bRead) {
            w_abyTmp[12] = Byte.MIN_VALUE;
        } else {
            w_abyTmp[12] = (byte) 0;
        }
        w_abyTmp[13] = (byte) 0;
        w_abyTmp[14] = (byte) 10;
        w_abyTmp[15] = (byte) -17;
        if (bRead) {
            w_abyTmp[16] = (byte) -1;
        } else {
            w_abyTmp[16] = (byte) -2;
        }
        if (!UsbBulkSend(w_abyTmp, 31, nTimeOut)) {
            return false;
        }
        boolean w_bRet;
        if (bRead) {
            w_bRet = UsbBulkReceive(pData, nDataLen, nTimeOut);
        } else {
            w_bRet = UsbBulkSend(pData, nDataLen, nTimeOut);
        }
        if (w_bRet) {
            return UsbBulkReceive(w_abyCSW, 13, nTimeOut);
        }
        return false;
    }

    public boolean UsbSCSIWrite(byte[] pCDB, int nCDBLen, byte[] pData, int nDataLen, int nTimeOut) {
        if (IsInit()) {
            byte[] w_abyTmp = new byte[31];
            byte[] w_abyCSW = new byte[13];
            Arrays.fill(w_abyTmp, (byte) 0);
            w_abyTmp[0] = (byte) 85;
            w_abyTmp[1] = (byte) 83;
            w_abyTmp[2] = (byte) 66;
            w_abyTmp[3] = (byte) 67;
            w_abyTmp[4] = (byte) 87;
            w_abyTmp[5] = (byte) 0;
            w_abyTmp[6] = (byte) 0;
            w_abyTmp[7] = (byte) 0;
            w_abyTmp[8] = (byte) (nDataLen & MotionEventCompat.ACTION_MASK);
            w_abyTmp[9] = (byte) ((nDataLen >> 8) & MotionEventCompat.ACTION_MASK);
            w_abyTmp[10] = (byte) ((nDataLen >> 16) & MotionEventCompat.ACTION_MASK);
            w_abyTmp[11] = (byte) ((nDataLen >> 24) & MotionEventCompat.ACTION_MASK);
            w_abyTmp[12] = (byte) 0;
            w_abyTmp[13] = (byte) 0;
            w_abyTmp[14] = (byte) 16;
            System.arraycopy(pCDB, 0, w_abyTmp, 15, nCDBLen);
            if (!UsbBulkSend(w_abyTmp, 31, nTimeOut)) {
                return false;
            }
            if (!UsbBulkSend(pData, nDataLen, nTimeOut)) {
                return false;
            }
            boolean w_bRet = UsbBulkReceive(w_abyCSW, 13, nTimeOut);
            if (w_bRet) {
                return UsbMassStorageStatusCheck(w_abyCSW, null);
            }
            return w_bRet;
        }
        Log.d(TAG, "UsbSCSIWrite :USB not init.");
        return false;
    }

    public boolean UsbSCSIRead(byte[] pCDB, int nCDBLen, byte[] pData, int nDataLen, int nTimeOut) {
        if (IsInit()) {
            byte[] w_abyTmp = new byte[31];
            byte[] w_abyCSW = new byte[]{(byte) 85, (byte) 83, (byte) 66, (byte) 67, (byte) 87, (byte) 0, (byte) 0, (byte) 0, (byte) (nDataLen & MotionEventCompat.ACTION_MASK), (byte) ((nDataLen >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((nDataLen >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((nDataLen >> 24) & MotionEventCompat.ACTION_MASK), Byte.MIN_VALUE};
            w_abyTmp[13] = (byte) 0;
            w_abyTmp[14] = (byte) 16;
            System.arraycopy(pCDB, 0, w_abyTmp, 15, nCDBLen);
            if (!UsbBulkSend(w_abyTmp, 31, nTimeOut)) {
                return false;
            }
            long w_nTime = SystemClock.elapsedRealtime() - SystemClock.elapsedRealtime();
            if (!UsbBulkReceive(pData, nDataLen, nTimeOut)) {
                return false;
            }
            boolean w_bRet = UsbBulkReceive(w_abyCSW, 13, nTimeOut);
            if (w_bRet) {
                return UsbMassStorageStatusCheck(w_abyCSW, null);
            }
            return w_bRet;
        }
        Log.d(TAG, "UsbSCSIRead:USB not init.");
        return false;
    }

    public boolean UsbSCSIRead(byte[] pCDB, int nCDBLen, byte[] pData, int[] nDataLen, int nTimeOut, boolean must) {
        if (IsInit()) {
            byte[] w_abyTmp = new byte[31];
            byte[] w_abyCSW = new byte[]{(byte) 85, (byte) 83, (byte) 66, (byte) 67, (byte) 87, (byte) 0, (byte) 0, (byte) 0, (byte) (nDataLen[0] & MotionEventCompat.ACTION_MASK), (byte) ((nDataLen[0] >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((nDataLen[0] >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((nDataLen[0] >> 24) & MotionEventCompat.ACTION_MASK), Byte.MIN_VALUE};
            w_abyTmp[13] = (byte) 0;
            w_abyTmp[14] = (byte) 16;
            System.arraycopy(pCDB, 0, w_abyTmp, 15, nCDBLen);
            if (!UsbBulkSend(w_abyTmp, 31, nTimeOut)) {
                return false;
            }
            long w_nTime = SystemClock.elapsedRealtime() - SystemClock.elapsedRealtime();
            if (!UsbBulkReceive(pData, nDataLen, nTimeOut, must)) {
                return false;
            }
            boolean w_bRet = UsbBulkReceive(w_abyCSW, 13, nTimeOut);
            if (w_bRet) {
                return UsbMassStorageStatusCheck(w_abyCSW, null);
            }
            return w_bRet;
        }
        Log.d(TAG, "UsbSCSIRead :USB not init.");
        return false;
    }

    private boolean UsbBulkSend(byte[] pBuf, int nLen, int nTimeOut) {
        int n = nLen / this.m_nEPOutSize;
        int r = nLen % this.m_nEPOutSize;
        int i = 0;
        while (i < n) {
            System.arraycopy(pBuf, this.m_nEPOutSize * i, this.m_abyTransferBuf, 0, this.m_nEPOutSize);
            int w_nRet = this.m_usbConn.bulkTransfer(this.m_epOUT, this.m_abyTransferBuf, this.m_nEPOutSize, nTimeOut);
            if (w_nRet != this.m_nEPOutSize) {
                Log.d(TAG, "UsbBulkSend bulkTransfer err1: w_nRet = " + w_nRet);
                return false;
            }
            i++;
        }
        if (r > 0) {
            System.arraycopy(pBuf, this.m_nEPOutSize * i, this.m_abyTransferBuf, 0, r);
            int w_nRet = this.m_usbConn.bulkTransfer(this.m_epOUT, this.m_abyTransferBuf, r, nTimeOut);
            if (w_nRet != r) {
                Log.d(TAG, "UsbBulkSend bulkTransfer err2: w_nRet = " + w_nRet);
                return false;
            }
        }
        return true;
    }

    private boolean UsbBulkReceive(byte[] pBuf, int nLen, int nTimeOut) {
        int n = nLen / this.m_nEPInSize;
        int r = nLen % this.m_nEPInSize;
        int i = 0;
        while (i < n) {
            int w_nRet = this.m_usbConn.bulkTransfer(this.m_epIN, this.m_abyTransferBuf, this.m_nEPInSize, nTimeOut);
            if (w_nRet != this.m_nEPInSize) {
                Log.d(TAG, "UsbBulkReceive bulkTransfer err: w_nRet = " + w_nRet + ",m_nEPInSize=" + this.m_nEPInSize);
                return false;
            }
            System.arraycopy(this.m_abyTransferBuf, 0, pBuf, this.m_nEPInSize * i, this.m_nEPInSize);
            i++;
        }
        if (r > 0) {
            int w_nRet = this.m_usbConn.bulkTransfer(this.m_epIN, this.m_abyTransferBuf, r, nTimeOut);
            if (w_nRet != r) {
                Log.d(TAG, "UsbBulkReceive bulkTransfer err1: w_nRet = " + w_nRet + ",r=" + r);
                return false;
            }
            System.arraycopy(this.m_abyTransferBuf, 0, pBuf, this.m_nEPInSize * i, r);
        }
        return true;
    }

    private boolean UsbBulkReceive(byte[] pBuf, int[] nLen, int nTimeOut, boolean must) {
        int n = nLen[0] / this.m_nEPInSize;
        int r = nLen[0] % this.m_nEPInSize;
        nLen[0] = 0;
        int i = 0;
        while (i < n) {
            int w_nRet = this.m_usbConn.bulkTransfer(this.m_epIN, this.m_abyTransferBuf, this.m_nEPInSize, nTimeOut);
            System.arraycopy(this.m_abyTransferBuf, 0, pBuf, nLen[0], w_nRet);
            nLen[0] = nLen[0] + w_nRet;
            if (w_nRet == this.m_nEPInSize) {
                i++;
            } else if (must) {
                return false;
            } else {
                return true;
            }
        }
        if (r > 0) {
            int w_nRet = this.m_usbConn.bulkTransfer(this.m_epIN, this.m_abyTransferBuf, r, nTimeOut);
            System.arraycopy(this.m_abyTransferBuf, 0, pBuf, nLen[0], r);
            nLen[0] = nLen[0] + r;
            if (must && w_nRet != r) {
                return false;
            }
        }
        return true;
    }

    private boolean UsbMassStorageStatusCheck(byte[] CSW, byte[] tag) {
        return true;
    }

    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) ((i >> 24) & MotionEventCompat.ACTION_MASK), (byte) ((i >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((i >> 8) & MotionEventCompat.ACTION_MASK), (byte) (i & MotionEventCompat.ACTION_MASK)};
    }

    @SuppressLint({"DefaultLocale"})
    public String byteArrToHexString(byte[] b, String bk) {
        String log = "";
        int i = 0;
        while (i < b.length && i <= 60) {
            String hex = Integer.toHexString(b[i] & MotionEventCompat.ACTION_MASK);
            if (hex.length() == 1) {
                hex = new StringBuilder(String.valueOf('0')).append(hex).toString();
            }
            log = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(log)).append(hex.toUpperCase()).toString())).append(bk).toString();
            i++;
        }
        Log.d(TAG, " " + log);
        return log;
    }

    private void m1l(Object msg) {
        Log.d(TAG, ">==< " + msg.toString() + " >==<");
    }

    private void m0e(Object msg) {
        Log.e(TAG, ">==< " + msg.toString() + " >==<");
    }
}
