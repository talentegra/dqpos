package com.BRMicro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import java.io.DataOutputStream;
import java.util.Arrays;

@SuppressLint({"DefaultLocale", "SdCardPath"})
public class NETLH_E {
    private static final short BROADCASTADDR = (short) -1;
    private static final short CHAR_EALG_SIZE = (short) 256;
    private static final short CHAR_XALG_SIZE = (short) 810;
    private static final short CMD_DELETE_CHAR = (short) 809;
    private static final short CMD_DETECT_FINGER = (short) 801;
    private static final short CMD_DEVICE_RESET = (short) 800;
    private static final short CMD_DOWNLOAD_IMAGE = (short) 832;
    private static final short CMD_EMPTY_CHAR = (short) 810;
    private static final short CMD_ERASE_PROGRAM = (short) 833;
    private static final short CMD_GEN_CHAR = (short) 805;
    private static final short CMD_GET_CARD_ID_TYPEA = (short) 1041;
    private static final short CMD_GET_CARD_ID_TYPEB = (short) 1042;
    private static final short CMD_GET_CHAR = (short) 812;
    private static final short CMD_GET_MBINDEX = (short) 814;
    private static final short CMD_GET_RANDOM = (short) 825;
    private static final short CMD_GET_RAW_IMAGE = (short) 802;
    private static final short CMD_GET_REDRESS_IMAGE = (short) 803;
    private static final short CMD_IC_ANTICOLL = (short) 1044;
    private static final short CMD_IC_CHECK_KEY = (short) 1048;
    private static final short CMD_IC_DECREMENT_MONEY = (short) 1053;
    private static final short CMD_IC_HALT = (short) 1046;
    private static final short CMD_IC_INCREMENT_MONEY = (short) 1052;
    private static final short CMD_IC_INIT_MONEY = (short) 1051;
    private static final short CMD_IC_LOAD_KEY = (short) 1047;
    private static final short CMD_IC_READ_BIOCK = (short) 1049;
    private static final short CMD_IC_REQUEST = (short) 1043;
    private static final short CMD_IC_SELECT = (short) 1045;
    private static final short CMD_IC_TRANSFER_MONEY = (short) 1054;
    private static final short CMD_IC_WRITE_BIOCK = (short) 1050;
    private static final byte CMD_JUDGEDISK = (byte) -121;
    private static final short CMD_MATCH_CHAR = (short) 806;
    private static final short CMD_MERGE_CHAR = (short) 822;
    private static final int CMD_PACKET_LEN = 26;
    private static final short CMD_PUT_CHAR = (short) 813;
    private static final byte CMD_READ = (byte) -123;
    private static final short CMD_READ_CHAR_DIRECT = (short) 835;
    private static final short CMD_READ_ID_CARD = (short) 1037;
    private static final short CMD_READ_NOTE_BOOK = (short) 815;
    private static final short CMD_READ_PAR_TABLE = (short) 817;
    private static final short CMD_RESUME_FACTORY = (short) 821;
    private static final short CMD_RT_ADDRESS_OVERFLOW = (short) 10;
    private static final short CMD_RT_CHAR_REPEAT = (short) 18;
    private static final short CMD_RT_CLEAR_TEMPLET_LIB_ERR = (short) 15;
    private static final short CMD_RT_COM_PASSWORD_ERR = (short) 3;
    public static final int CMD_RT_CONNECTIONED = 1;
    private static final short CMD_RT_DELETE_TEMPLET_ERR = (short) 14;
    private static final short CMD_RT_DEVICE_ADDRESS_ERR = (short) 2;
    public static final int CMD_RT_DISCONNECTION = 0;
    private static final short CMD_RT_ERASE_FLAG_FAILE = (short) 22;
    private static final short CMD_RT_FINGER_MATCH_ERR = (short) 7;
    private static final short CMD_RT_FINGER_NOT_MOVE = (short) 16;
    private static final short CMD_RT_FINGER_SEARCH_FAILE = (short) 8;
    private static final short CMD_RT_GEN_CHAR_ERR = (short) 6;
    private static final short CMD_RT_GET_IMAGE_FAILE = (short) 5;
    private static final short CMD_RT_GET_MBINDEX_OVERFLOW = (short) 20;
    private static final short CMD_RT_MB_NOT_EXIST_IN_ADDRESS = (short) 19;
    private static final short CMD_RT_MERGE_TEMPLET_FAILE = (short) 9;
    private static final short CMD_RT_NOTE_BOOK_ADDRESS_OVERFLOW = (short) 24;
    private static final short CMD_RT_NO_CMD = (short) 26;
    private static final short CMD_RT_NO_FINGER = (short) 4;
    private static final short CMD_RT_NO_TEMPLET_IN_ADDRESS = (short) 17;
    private static final short CMD_RT_OK = (short) 0;
    private static final short CMD_RT_OPERATION_FLASH_ERR = (short) 24;
    private static final short CMD_RT_PACKGE_ERR = (short) 1;
    private static final short CMD_RT_PARA_ERR = (short) 25;
    private static final short CMD_RT_READ_TEMPLET_ERR = (short) 11;
    private static final short CMD_RT_SET_BAUD_RATE_FAILE = (short) 21;
    private static final short CMD_RT_SYSTEM_RESET_FAILE = (short) 23;
    private static final short CMD_RT_UP_IMAGE_FAILE = (short) 13;
    private static final short CMD_RT_UP_TEMPLET_ERR = (short) 12;
    private static final short CMD_SEARCH_CHAR = (short) 808;
    private static final short CMD_SET_ADDRESS = (short) 824;
    private static final short CMD_SET_BAUD_RATE = (short) 818;
    private static final short CMD_SET_CARD_TYPE = (short) 1040;
    private static final short CMD_SET_CMOS_PARA = (short) 820;
    private static final short CMD_SET_PSW = (short) 823;
    private static final short CMD_SET_SECURLEVEL = (short) 819;
    private static final short CMD_STORE_CHAR = (short) 807;
    private static final short CMD_STORE_CHAR_DIRECT = (short) 834;
    private static final short CMD_UPLOAD_IMAGE = (short) 804;
    private static final short CMD_VERIFY_CHAR = (short) 811;
    private static final byte CMD_WRITE = (byte) -122;
    private static final short CMD_WRITE_NOTE_BOOK = (short) 816;
    private static final int COMM_SLEEP_TIME = 40;
    public static final String DEFAULT_BMP_FILE_PATH = "/mnt/sdcard/Finger.bmp";
    private static final short DEFAULT_DEVICE_JUDGE_HEART = (short) 13141;
    public static final int DEFAULT_FINGER_IMAGE_MAX_LENG = 184320;
    private static final short DEFAULT_PACK_NUM = (short) 0;
    private static final short IC_BLOCK_SIZE = (short) 16;
    private static final short LOADKEY_LENGTH = (short) 6;
    private static final short NETLH_CRC_CHECK = (short) 2;
    private static final short NETLH_DATA_ACK = (short) 2;
    private static final short NETLH_PACKDATA = (short) 820;
    private static final short NETLH_PACKHEAD = (short) 17;
    private static final short NETLH_PACKLENGTH = (short) 839;
    private static final short NETLH_PACKLEN_POS_LH = (short) 15;
    private static final short NOTE_BOOK_PAGE_SIZE = (short) 32;
    private static final short PACK_CMD = (short) 1;
    private static final short PACK_DATA = (short) 2;
    private static final short PACK_HEAD = (short) 751;
    private static final int PID = 22304;
    public static final short RAW_IMAGE_X = (short) 312;
    public static final short RAW_IMAGE_Y = (short) 232;
    private static final int RCM_DATA_OFFSET = 10;
    private static final int RCM_PACKET_LEN = 26;
    public static final short REDRESS_IMAGE_BIG_Y = (short) 360;
    public static final short REDRESS_IMAGE_X = (short) 256;
    public static final short REDRESS_IMAGE_Y = (short) 288;
    private static final int SCSI_TIMEOUT = 5000;
    public static final int USB_MAX_PACKAGE = 32768;
    private static final int VID = 1155;
    private final int SERIAL_TYPE = 2;
    private final String TAG = "=NETLH_E=";
    private final int USB_NO_ROOT_TYPE = 0;
    private final int USB_TYPE = 1;
    private final Context mApplicationContext;
    private int mConnType = 1;
    private byte[] mRxBuf = new byte[8390];
    private int mTimeOutValue = 1000;
    private byte[] mTxBuf = new byte[8390];
    private final IUsbConnState m_IConnectionHandler = new C01731();
    public byte[] m_abyPacket = new byte[8390];
    public byte[] m_abyPacket2 = new byte[8390];
    public byte m_byDstDeviceID = (byte) 1;
    public byte m_bySrcDeviceID = (byte) 1;
    public int m_nPacketSize;
    private Activity m_parentAcitivity;
    private UsbController m_usbBase;

    class C01731 implements IUsbConnState {
        C01731() {
        }

        public void onUsbConnected() {
            String[] w_strInfo = new String[1];
        }

        public void onUsbPermissionDenied() {
            Log.d("=NETLH_E=", "Permission denied!");
        }

        public void onDeviceNotFound() {
            Log.d("=NETLH_E=", "Can not find usb device!");
        }
    }

    private native int NCmdCreatFileOption(String str, byte[] bArr, int i, int i2);

    private native int NCmdGetParaTable(PARA_TABLE para_table, byte[] bArr);

    public native int AsciiToHex(char[] cArr, int i, char[] cArr2, int[] iArr);

    public native int CmdCheckInquiry(byte[] bArr);

    public native int CmdDeviceInitGetPath(byte[] bArr);

    public native int CmdGetProtocolCheck(byte[] bArr);

    public native int CmdGetRawImageBuf(byte[] bArr);

    public native int CmdSendDemon(byte[] bArr, int[] iArr);

    public native int CmdSetPack(int i, int i2, int i3, int i4, byte[] bArr, byte[] bArr2);

    public native int GetAppDirectoryPath(char[] cArr, int i);

    public native int GetComList(char[] cArr);

    public native int GetCurrentDirectoryPath(char[] cArr, int i);

    public native int GetLastCommErr();

    public native int GetLastCommSystemErr();

    public native int NCmdDelChar(int i, int[] iArr);

    public native int NCmdDetectFinger(int[] iArr);

    public native int NCmdDeviceReset(int[] iArr);

    public native int NCmdEmptyChar(int[] iArr);

    public native int NCmdEraseProgram(int[] iArr);

    public native int NCmdGenChar(int i, int[] iArr);

    public native int NCmdGetCardIdTypeA(byte[] bArr, int[] iArr);

    public native int NCmdGetCardIdTypeB(byte[] bArr, int[] iArr);

    public native int NCmdGetChar_eAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdGetChar_xAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdGetMBIndex(byte[] bArr, int i, int i2, int[] iArr);

    public native int NCmdGetRandom(int[] iArr, int[] iArr2);

    public native int NCmdGetRawImage(int[] iArr);

    public native int NCmdGetRedressImage(int i, int[] iArr);

    public native int NCmdICAnticoll(int i, byte[] bArr, int[] iArr);

    public native int NCmdICAuthentication(int i, int i2, int i3, int[] iArr);

    public native int NCmdICDecrementMoney(int i, int i2, int i3, int[] iArr);

    public native int NCmdICHalt(int[] iArr);

    public native int NCmdICIncrementMoney(int i, int i2, int i3, int[] iArr);

    public native int NCmdICInitMoney(int i, int i2, int i3, int[] iArr);

    public native int NCmdICLoadKey(byte[] bArr, int[] iArr);

    public native int NCmdICReadBlock(int i, int i2, byte[] bArr, int[] iArr);

    public native int NCmdICRequest(int i, byte[] bArr, int[] iArr);

    public native int NCmdICSelect(byte[] bArr, int i, int[] iArr);

    public native int NCmdICTransferMoney(int i, int i2, int[] iArr);

    public native int NCmdICWriteBlock(int i, int i2, byte[] bArr, int[] iArr);

    public native int NCmdMatchChar(int[] iArr, int[] iArr2);

    public native int NCmdMergeChar(int[] iArr, int[] iArr2);

    public native int NCmdPutChar_eAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdPutChar_xAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdReadCharDirect_eAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdReadCharDirect_xAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdReadNoteBook(int i, byte[] bArr, int[] iArr);

    public native int NCmdReadParaTable(PARA_TABLE para_table, int[] iArr);

    public native int NCmdResumeFactory(int[] iArr);

    public native int NCmdSearchChar(int i, int[] iArr, int[] iArr2, int[] iArr3);

    public native int NCmdSetBaudRate(int i, int[] iArr);

    public native int NCmdSetCardType(int i, int[] iArr);

    public native int NCmdSetCmosPara(int i, int i2, int[] iArr);

    public native int NCmdSetDeviceAddress(int i, int[] iArr);

    public native int NCmdSetPsw(int i, int[] iArr);

    public native int NCmdSetSecurLevel(int i, int[] iArr);

    public native int NCmdStoreChar(int i, int[] iArr, int[] iArr2, int[] iArr3);

    public native int NCmdStoreCharDirect_eAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdStoreCharDirect_xAlg(int i, byte[] bArr, int[] iArr);

    public native int NCmdUpLoadRawImage(byte[] bArr);

    public native int NCmdUpLoadRedressImage(byte[] bArr);

    public native int NCmdUpLoadRedressImage256x360(byte[] bArr);

    public native int NCmdVerifyChar(int i, int i2, int[] iArr, int[] iArr2);

    public native int NCmdWriteNoteBook(int i, byte[] bArr, int[] iArr);

    public native void NCommClose();

    public native int NConfigCommParameter(int i, int i2);

    public native int NConfigCommParameterCom(String str, int i, int i2, int i3, int i4, int i5, int i6);

    public native int NConfigCommParameterUDisk(int i, int i2);

    public native int NGetTimeOutValue();

    public native void NSetTimeOutValue(int i);

    public native int SetAppDirectoryPath(char[] cArr, int i);

    public NETLH_E(Activity parentActivity, IUsbConnState usbConnState) {
        this.m_parentAcitivity = parentActivity;
        this.mApplicationContext = parentActivity.getApplicationContext();
        this.m_usbBase = new UsbController(parentActivity, usbConnState, VID, PID);
    }

    public boolean IsInit() {
        return this.m_usbBase.IsInit();
    }

    public boolean OpenComm() {
        this.m_usbBase.init();
        return true;
    }

    public boolean CloseComm() {
        this.m_usbBase.uninit();
        return true;
    }

    public int CmdDeviceGetChmod(int ErrCode) {
        DataOutputStream dataOutputStream;
        byte[] path = new byte[128];
        CmdDeviceInitGetPath(path);
        String spath = new String(path);
        String command = "chmod 777 " + spath.substring(0, spath.indexOf(0));
        Log.d("=NETLH_E=", " exec " + command);
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            try {
                os.writeBytes(new StringBuilder(String.valueOf(command)).append("\n").toString());
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
                dataOutputStream = os;
                return 1;
            } catch (Exception e) {
                dataOutputStream = os;
                return 0;
            }
        } catch (Exception e2) {
            return 0;
        }
    }

    public int CmdDeviceGetChmod(String path) {
        DataOutputStream dataOutputStream;
        String command = "chmod 777 " + path;
        Log.d("=NETLH_E=", " exec " + command);
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            try {
                os.writeBytes(new StringBuilder(String.valueOf(command)).append("\n").toString());
                os.writeBytes("exit\n");
                os.flush();
                process.waitFor();
                dataOutputStream = os;
            } catch (Exception e) {
                dataOutputStream = os;
            }
        } catch (Exception e2) {
        }
        return 0;
    }

    public int ConfigCommParameterUDisk(int _DeviceAdd, int _Password) {
        this.mConnType = 0;
        if (this.mConnType != 0) {
            return NConfigCommParameterUDisk(_DeviceAdd, _Password);
        }
        NConfigCommParameter(_DeviceAdd, _Password);
        CloseComm();
        if (IsInit()) {
            if (CmdMyJudgeDisk()) {
                return 1;
            }
            return 0;
        } else if (OpenComm()) {
            return 2;
        } else {
            return 0;
        }
    }

    public int ConfigCommParameterCom(String _COM, int _BaudRate, int _DataBit, int _StopBit, int _CheckMode, int _DeviceAdd, int _Password) {
        this.mConnType = 2;
        return NConfigCommParameterCom(_COM, _BaudRate, _DataBit, _StopBit, _CheckMode, _DeviceAdd, _Password);
    }

    public int CmdDetectFinger(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdDetectFinger(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 801, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_DETECT_FINGER);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_DETECT_FINGER)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdReadParaTable(PARA_TABLE _ParaTable, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdReadParaTable(_ParaTable, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 817, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_READ_PAR_TABLE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_READ_PAR_TABLE)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        NCmdGetParaTable(_ParaTable, this.m_abyPacket);
        return 1;
    }

    public int CmdGetRawImage(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetRawImage(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 802, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_RAW_IMAGE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_RAW_IMAGE)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdGetRedressImage(int _DetectDn, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetRedressImage(_DetectDn, _ErrFlag);
        }
        byte[] detectDn = new byte[]{(byte) (_DetectDn & MotionEventCompat.ACTION_MASK)};
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 803, 1, 1, detectDn, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_REDRESS_IMAGE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_REDRESS_IMAGE)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdUpLoadRawImage(byte[] _ImageBuf) {
        if (this.mConnType != 0) {
            return NCmdUpLoadRawImage(_ImageBuf);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 804, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_UPLOAD_IMAGE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        byte[] FingerImageBuf = new byte[DEFAULT_FINGER_IMAGE_MAX_LENG];
        if (!USB_get(CMD_UPLOAD_IMAGE, FingerImageBuf, DEFAULT_FINGER_IMAGE_MAX_LENG, 32768)) {
            return 0;
        }
        if (_ImageBuf == null) {
            NCmdCreatFileOption(DEFAULT_BMP_FILE_PATH, FingerImageBuf, 312, 232);
        } else {
            System.arraycopy(FingerImageBuf, 0, _ImageBuf, 0, 72384);
        }
        return 1;
    }

    public int CmdUpLoadRedressImage256x360(byte[] _ImageBuf) {
        if (this.mConnType != 0) {
            return NCmdUpLoadRedressImage256x360(_ImageBuf);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 804, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_UPLOAD_IMAGE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        byte[] FingerImageBuf = new byte[DEFAULT_FINGER_IMAGE_MAX_LENG];
        if (!USB_get(CMD_UPLOAD_IMAGE, FingerImageBuf, DEFAULT_FINGER_IMAGE_MAX_LENG, 32768)) {
            return 0;
        }
        if (_ImageBuf == null) {
            NCmdCreatFileOption(DEFAULT_BMP_FILE_PATH, FingerImageBuf, 256, 360);
        } else {
            System.arraycopy(FingerImageBuf, 0, _ImageBuf, 0, 92160);
        }
        return 1;
    }

    public int CmdUpLoadRedressImage(byte[] _ImageBuf) {
        if (this.mConnType != 0) {
            return NCmdUpLoadRedressImage(_ImageBuf);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 804, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_UPLOAD_IMAGE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        byte[] FingerImageBuf = new byte[73728];
        if (!USB_get(CMD_UPLOAD_IMAGE, FingerImageBuf, 73728, 32768)) {
            return 0;
        }
        if (_ImageBuf == null) {
            NCmdCreatFileOption(DEFAULT_BMP_FILE_PATH, FingerImageBuf, 256, 288);
        } else {
            System.arraycopy(FingerImageBuf, 0, _ImageBuf, 0, 73728);
        }
        return 1;
    }

    public boolean CmdMyJudgeDisk() {
        if (this.mConnType != 0) {
            return true;
        }
        byte[] btCDB = new byte[16];
        byte[] w_WaitPacket = new byte[2];
        int[] w_nLen = new int[1];
        memset(btCDB, (byte) 0, 8);
        Arrays.fill(w_WaitPacket, (byte) 0);
        btCDB[0] = CMD_JUDGEDISK;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 0;
        btCDB[4] = (byte) -1;
        w_nLen[0] = 2;
        boolean w_bRet = this.m_usbBase.UsbSCSIRead(btCDB, 6, w_WaitPacket, w_nLen, SCSI_TIMEOUT, false);
        short now = (short) (((w_WaitPacket[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (w_WaitPacket[0] & MotionEventCompat.ACTION_MASK));
        if (!w_bRet) {
            Log.d("=NETLH_E=", "MyJudgeDisk: Connect failed");
        } else if (DEFAULT_DEVICE_JUDGE_HEART == now && CmdSCSIExecuteInquiry()) {
            Log.d("=NETLH_E=", "MyJudgeDisk: This is my devices.");
        } else {
            Log.d("=NETLH_E=", "MyJudgeDisk: This is not my devices:" + now);
            return false;
        }
        return w_bRet;
    }

    public void CommClose() {
        if (this.mConnType != 0) {
            NCommClose();
        }
        CloseComm();
    }

    public int CmdGenChar(int iBuffer, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGenChar(iBuffer, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 805, 1, 4, new byte[]{(byte) (iBuffer & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GEN_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GEN_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdSearchChar(int iBuffer, int[] _RetMbIndex, int[] _RetScore, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSearchChar(iBuffer, _RetMbIndex, _RetScore, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 808, 1, 4, new byte[]{(byte) (iBuffer & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_SEARCH_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_SEARCH_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        _RetMbIndex[0] = MAKEWORD(this.m_abyPacket[19], this.m_abyPacket[20]);
        _RetScore[0] = MAKEWORD(this.m_abyPacket[21], this.m_abyPacket[22]);
        return 1;
    }

    public int CmdMergeChar(int[] _RetScore, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdMergeChar(_RetScore, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 822, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_MERGE_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_MERGE_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        _RetScore[0] = MAKEWORD(this.m_abyPacket[19], this.m_abyPacket[20]);
        return 1;
    }

    public int CmdStoreChar(int m_Addr, int[] _RetMbIndex, int[] _RetScore, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdStoreChar(m_Addr, _RetMbIndex, _RetScore, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (m_Addr & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 807, 1, 2, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_STORE_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_STORE_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        _RetMbIndex[0] = MAKEWORD(this.m_abyPacket[19], this.m_abyPacket[20]);
        _RetScore[0] = MAKEWORD(this.m_abyPacket[21], this.m_abyPacket[22]);
        return 1;
    }

    public int CmdGetMBIndex(byte[] gMBIndex, int gMBIndexStart, int gMBIndexNum, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetMBIndex(gMBIndex, gMBIndexStart, gMBIndexNum, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 814, 1, 4, new byte[]{(byte) (gMBIndexStart & MotionEventCompat.ACTION_MASK), (byte) ((gMBIndexStart >> 8) & MotionEventCompat.ACTION_MASK), (byte) (gMBIndexNum & MotionEventCompat.ACTION_MASK), (byte) ((gMBIndexNum >> 8) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_MBINDEX);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_MBINDEX)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, gMBIndex, 0, this.m_nPacketSize - 12);
        return 1;
    }

    public int CmdEmptyChar(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdEmptyChar(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 810, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post((short) 810);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get((short) 810)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdVerifyChar(int iBuffer, int m_Addr, int[] _RetScor, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdVerifyChar(iBuffer, m_Addr, _RetScor, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 811, 1, 6, new byte[]{(byte) (iBuffer & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK), (byte) (m_Addr & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_VERIFY_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_VERIFY_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        _RetScor[0] = MAKEWORD(this.m_abyPacket[19], this.m_abyPacket[20]);
        return 1;
    }

    public int CmdDeviceReset(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdDeviceReset(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 800, 1, 0, new byte[1], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_DEVICE_RESET);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_DEVICE_RESET)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdMatchChar(int[] _RetScore, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdMatchChar(_RetScore, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 806, 1, 0, new byte[6], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_MATCH_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_MATCH_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        _RetScore[0] = MAKEWORD(this.m_abyPacket[19], this.m_abyPacket[20]);
        return 1;
    }

    public int CmdGetChar_eAlg(int iBuffer, byte[] CharBuf, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetChar_eAlg(iBuffer, CharBuf, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 812, 1, 4, new byte[]{(byte) (iBuffer & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, CharBuf, 0, 256);
        return 1;
    }

    public int CmdGetChar_xAlg(int iBuffer, byte[] CharBuf, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return CmdGetChar_xAlg(iBuffer, CharBuf, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 812, 1, 4, new byte[]{(byte) (iBuffer & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, CharBuf, 0, 810);
        return 1;
    }

    public int CmdPutChar_eAlg(int iBuffer, byte[] CharBuf, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdPutChar_eAlg(iBuffer, CharBuf, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[260];
        wiBuffer[0] = (byte) (iBuffer & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK);
        wiBuffer[2] = (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK);
        wiBuffer[3] = (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK);
        System.arraycopy(CharBuf, 0, wiBuffer, 4, 256);
        this.m_nPacketSize = CmdSetPack(0, 813, 1, 260, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_PUT_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_PUT_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdPutChar_xAlg(int iBuffer, byte[] CharBuf, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdPutChar_xAlg(iBuffer, CharBuf, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[814];
        wiBuffer[0] = (byte) (iBuffer & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((iBuffer >> 8) & MotionEventCompat.ACTION_MASK);
        wiBuffer[2] = (byte) ((iBuffer >> 16) & MotionEventCompat.ACTION_MASK);
        wiBuffer[3] = (byte) ((iBuffer >> 24) & MotionEventCompat.ACTION_MASK);
        System.arraycopy(CharBuf, 0, wiBuffer, 4, 810);
        this.m_nPacketSize = CmdSetPack(0, 813, 1, 814, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_PUT_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_PUT_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdDelChar(int m_Addr, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdDelChar(m_Addr, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (m_Addr & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 809, 1, 2, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_DELETE_CHAR);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_DELETE_CHAR)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdReadNoteBook(int _PageID, byte[] _NoteText, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdReadNoteBook(_PageID, _NoteText, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (_PageID & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 815, 1, 1, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_READ_NOTE_BOOK);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_READ_NOTE_BOOK)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _NoteText, 0, 32);
        return 1;
    }

    public int CmdWriteNoteBook(int _PageID, byte[] _NoteText, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdWriteNoteBook(_PageID, _NoteText, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[33];
        wiBuffer[0] = (byte) (_PageID & MotionEventCompat.ACTION_MASK);
        System.arraycopy(_NoteText, 0, wiBuffer, 1, 32);
        this.m_nPacketSize = CmdSetPack(0, 816, 1, 33, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_WRITE_NOTE_BOOK);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_WRITE_NOTE_BOOK)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdSetBaudRate(int _BaudRate, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSetBaudRate(_BaudRate, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 818, 1, 4, new byte[]{(byte) (_BaudRate & MotionEventCompat.ACTION_MASK), (byte) ((_BaudRate >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_BaudRate >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_BaudRate >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_SET_BAUD_RATE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_SET_BAUD_RATE)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdSetSecurLevel(int _SecurLevel, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSetSecurLevel(_SecurLevel, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (_SecurLevel & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 819, 1, 1, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_SET_SECURLEVEL);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_SET_SECURLEVEL)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdSetCmosPara(int _ExposeTimer, int DetectSensitive, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSetCmosPara(_ExposeTimer, DetectSensitive, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (_ExposeTimer & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((_ExposeTimer >> 8) & MotionEventCompat.ACTION_MASK);
        wiBuffer[2] = (byte) (DetectSensitive & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 820, 1, 3, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post((short) 820);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get((short) 820)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdEraseProgram(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdEraseProgram(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 833, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_ERASE_PROGRAM);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_ERASE_PROGRAM)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdResumeFactory(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdResumeFactory(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 821, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_RESUME_FACTORY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_RESUME_FACTORY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public void SetTimeOutValue(int _TimeOutValue) {
        if (this.mConnType != 0) {
            NSetTimeOutValue(_TimeOutValue);
        } else {
            this.mTimeOutValue = _TimeOutValue;
        }
    }

    public int GetTimeOutValue() {
        if (this.mConnType != 0) {
            return NGetTimeOutValue();
        }
        return this.mTimeOutValue;
    }

    public int CmdStoreCharDirect_eAlg(int m_Addr, byte[] _FingerChar, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdStoreCharDirect_eAlg(m_Addr, _FingerChar, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[260];
        wiBuffer[0] = (byte) (m_Addr & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK);
        wiBuffer[2] = (byte) ((m_Addr >> 16) & MotionEventCompat.ACTION_MASK);
        wiBuffer[3] = (byte) ((m_Addr >> 24) & MotionEventCompat.ACTION_MASK);
        System.arraycopy(_FingerChar, 0, wiBuffer, 4, 256);
        this.m_nPacketSize = CmdSetPack(0, 834, 1, 260, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_STORE_CHAR_DIRECT);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_STORE_CHAR_DIRECT)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdStoreCharDirect_xAlg(int m_Addr, byte[] _FingerChar, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdStoreCharDirect_xAlg(m_Addr, _FingerChar, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[814];
        wiBuffer[0] = (byte) (m_Addr & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK);
        wiBuffer[2] = (byte) ((m_Addr >> 16) & MotionEventCompat.ACTION_MASK);
        wiBuffer[3] = (byte) ((m_Addr >> 24) & MotionEventCompat.ACTION_MASK);
        System.arraycopy(_FingerChar, 0, wiBuffer, 4, 810);
        this.m_nPacketSize = CmdSetPack(0, 834, 1, 814, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_STORE_CHAR_DIRECT);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_STORE_CHAR_DIRECT)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdReadCharDirect_eAlg(int m_Addr, byte[] _FingerChar, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdReadCharDirect_eAlg(m_Addr, _FingerChar, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 835, 1, 4, new byte[]{(byte) (m_Addr & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_READ_CHAR_DIRECT);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_READ_CHAR_DIRECT)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _FingerChar, 0, 256);
        return 1;
    }

    public int CmdReadCharDirect_xAlg(int m_Addr, byte[] _FingerChar, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdReadCharDirect_xAlg(m_Addr, _FingerChar, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 835, 1, 4, new byte[]{(byte) (m_Addr & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((m_Addr >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_READ_CHAR_DIRECT);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_READ_CHAR_DIRECT)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _FingerChar, 0, 810);
        return 1;
    }

    public int CmdSetPsw(int _NewPsw, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSetPsw(_NewPsw, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 823, 1, 4, new byte[]{(byte) (_NewPsw & MotionEventCompat.ACTION_MASK), (byte) ((_NewPsw >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_NewPsw >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_NewPsw >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_SET_PSW);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_SET_PSW)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        if (_ErrFlag[0] == 0) {
            NConfigCommParameter(0, _NewPsw);
        }
        return 1;
    }

    public int CmdSetDeviceAddress(int _NewAddress, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSetDeviceAddress(_NewAddress, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 824, 1, 4, new byte[]{(byte) (_NewAddress & MotionEventCompat.ACTION_MASK), (byte) ((_NewAddress >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_NewAddress >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_NewAddress >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_SET_ADDRESS);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_SET_ADDRESS)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        if (_ErrFlag[0] == 0) {
            NConfigCommParameter(_NewAddress, 0);
        }
        return 1;
    }

    public int CmdGetRandom(int[] _Random, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetRandom(_Random, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 825, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_RANDOM);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_RANDOM)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _Random, 0, 4);
        return 1;
    }

    public int CmdICRequest(int _Mode, byte[] _CardType, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICRequest(_Mode, _CardType, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (_Mode & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 1043, 1, 1, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_REQUEST);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_REQUEST)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _CardType, 0, 2);
        return 1;
    }

    public int CmdICAnticoll(int _Bcnt, byte[] _CardNum, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICAnticoll(_Bcnt, _CardNum, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (_Bcnt & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 1044, 1, 1, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_ANTICOLL);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_ANTICOLL)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _CardNum, 0, 4);
        return 1;
    }

    public int CmdICSelect(byte[] _Size, int _CardNum, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICSelect(_Size, _CardNum, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1045, 1, 4, new byte[]{(byte) (_CardNum & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_SELECT);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_SELECT)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _Size, 0, 1);
        return 1;
    }

    public int CmdICHalt(int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICHalt(_ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1046, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_HALT);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_HALT)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICLoadKey(byte[] _LoadKey, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICLoadKey(_LoadKey, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1047, 1, 6, _LoadKey, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_LOAD_KEY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_LOAD_KEY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICAuthentication(int _Sector, int _AuthMode, int _CardNum, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICAuthentication(_Sector, _AuthMode, _CardNum, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1048, 1, 6, new byte[]{(byte) (_Sector & MotionEventCompat.ACTION_MASK), (byte) (_AuthMode & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 0) & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_CardNum >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_CHECK_KEY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_CHECK_KEY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICReadBlock(int _SectorIndex, int _BlockIndex, byte[] _BlockBuf, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICReadBlock(_SectorIndex, _BlockIndex, _BlockBuf, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[4];
        wiBuffer[0] = (byte) (_SectorIndex & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) (_BlockIndex & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 1049, 1, 2, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_READ_BIOCK);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_READ_BIOCK)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _BlockBuf, 0, 16);
        return 1;
    }

    public int CmdICWriteBlock(int _SectorIndex, int _BlockIndex, byte[] _BlockBuf, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICWriteBlock(_SectorIndex, _BlockIndex, _BlockBuf, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[20];
        wiBuffer[0] = (byte) (_SectorIndex & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) (_BlockIndex & MotionEventCompat.ACTION_MASK);
        System.arraycopy(_BlockBuf, 0, wiBuffer, 2, 16);
        this.m_nPacketSize = CmdSetPack(0, 1050, 1, 18, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_WRITE_BIOCK);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_WRITE_BIOCK)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICInitMoney(int _SectorIndex, int _BlockIndex, int _Value, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICInitMoney(_SectorIndex, _BlockIndex, _Value, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1051, 1, 6, new byte[]{(byte) (_SectorIndex & MotionEventCompat.ACTION_MASK), (byte) (_BlockIndex & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 0) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_INIT_MONEY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_INIT_MONEY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICIncrementMoney(int _SectorIndex, int _BlockIndex, int _Value, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICIncrementMoney(_SectorIndex, _BlockIndex, _Value, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1052, 1, 6, new byte[]{(byte) (_SectorIndex & MotionEventCompat.ACTION_MASK), (byte) (_BlockIndex & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 0) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_INCREMENT_MONEY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_INCREMENT_MONEY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICDecrementMoney(int _SectorIndex, int _BlockIndex, int _Value, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICDecrementMoney(_SectorIndex, _BlockIndex, _Value, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1053, 1, 6, new byte[]{(byte) (_SectorIndex & MotionEventCompat.ACTION_MASK), (byte) (_BlockIndex & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 0) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 8) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 16) & MotionEventCompat.ACTION_MASK), (byte) ((_Value >> 24) & MotionEventCompat.ACTION_MASK)}, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_DECREMENT_MONEY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_DECREMENT_MONEY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdICTransferMoney(int _SectorIndex, int _BlockIndex, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdICTransferMoney(_SectorIndex, _BlockIndex, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[6];
        wiBuffer[0] = (byte) (_SectorIndex & MotionEventCompat.ACTION_MASK);
        wiBuffer[1] = (byte) (_BlockIndex & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 1054, 1, 2, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_IC_TRANSFER_MONEY);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_IC_TRANSFER_MONEY)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public int CmdGetCardIdTypeB(byte[] _Card_ID, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetCardIdTypeB(_Card_ID, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1042, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_CARD_ID_TYPEB);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_CARD_ID_TYPEB)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _Card_ID, 0, 8);
        return 1;
    }

    public int CmdGetCardIdTypeA(byte[] _Card_ID, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdGetCardIdTypeA(_Card_ID, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = CmdSetPack(0, 1041, 1, 0, new byte[4], this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_GET_CARD_ID_TYPEA);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_GET_CARD_ID_TYPEA)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        System.arraycopy(this.m_abyPacket, 19, _Card_ID, 0, 4);
        return 1;
    }

    public int CmdSetCardType(int _CardType, int[] _ErrFlag) {
        if (this.mConnType != 0) {
            return NCmdSetCardType(_CardType, _ErrFlag);
        }
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] wiBuffer = new byte[6];
        wiBuffer[0] = (byte) (_CardType & MotionEventCompat.ACTION_MASK);
        this.m_nPacketSize = CmdSetPack(0, 1040, 1, 1, wiBuffer, this.m_abyPacket);
        boolean w_bRet = USB_post(CMD_SET_CARD_TYPE);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        this.m_nPacketSize = 0;
        if (!USB_get(CMD_SET_CARD_TYPE)) {
            return 0;
        }
        _ErrFlag[0] = GetRetCode();
        return 1;
    }

    public boolean CmdSCSIExecuteInquiry() {
        if (this.mConnType != 0) {
            return true;
        }
        byte[] btCDB = new byte[16];
        byte[] w_WaitPacket = new byte[600];
        int[] w_nLen = new int[1];
        memset(btCDB, (byte) 0, 8);
        Arrays.fill(w_WaitPacket, (byte) 0);
        btCDB[0] = (byte) 18;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 0;
        btCDB[4] = (byte) 36;
        w_nLen[0] = 36;
        boolean w_bRet = this.m_usbBase.UsbSCSIRead(btCDB, 6, w_WaitPacket, w_nLen, SCSI_TIMEOUT, false);
        if (!w_bRet || 1 == CmdCheckInquiry(w_WaitPacket)) {
            return w_bRet;
        }
        return false;
    }

    private boolean memcmp(byte[] p1, byte[] p2, int nLen) {
        for (int i = 0; i < nLen; i++) {
            if (p1[i] != p2[i]) {
                return false;
            }
        }
        return true;
    }

    private void memset(byte[] p1, byte nValue, int nLen) {
        Arrays.fill(p1, 0, nLen, nValue);
    }

    private void memcpy(byte[] p1, byte nValue, int nLen) {
        Arrays.fill(p1, 0, nLen, nValue);
    }

    private short MAKEWORD(byte low, byte high) {
        return (short) (((high << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (low & MotionEventCompat.ACTION_MASK));
    }

    private byte LOBYTE(short s) {
        return (byte) (s & MotionEventCompat.ACTION_MASK);
    }

    private byte HIBYTE(short s) {
        return (byte) ((s >> 8) & MotionEventCompat.ACTION_MASK);
    }

    private boolean USB_post(short wCMD) {
        byte[] btCDB = new byte[8];
        Arrays.fill(btCDB, (byte) 0);
        btCDB[0] = CMD_WRITE;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 60;
        btCDB[4] = (byte) -1;
        return this.m_usbBase.UsbSCSIWrite(btCDB, 6, this.m_abyPacket, this.m_nPacketSize, SCSI_TIMEOUT);
    }

    private boolean USB_get(short wCMD) {
        byte[] btCDB = new byte[16];
        byte[] w_WaitPacket = new byte[2];
        int[] w_nLen = new int[1];
        memset(btCDB, (byte) 0, 8);
        Arrays.fill(w_WaitPacket, (byte) 0);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        btCDB[0] = CMD_READ;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 0;
        btCDB[4] = (byte) -1;
        w_nLen[0] = 839;
        if (!this.m_usbBase.UsbSCSIRead(btCDB, 6, this.m_abyPacket, w_nLen, SCSI_TIMEOUT, false)) {
            return false;
        }
        this.m_nPacketSize = w_nLen[0];
        if (w_nLen[0] > 839 || !CheckReceive(this.m_abyPacket, this.m_nPacketSize, PACK_HEAD, wCMD)) {
            return false;
        }
        return true;
    }

    private boolean USB_get(short wCMD, byte[] rxbuff, int expetLen, int page_max) {
        int[] w_nLen = new int[1];
        int allLen = 0;
        int n = expetLen / page_max;
        int r = expetLen % page_max;
        memset(new byte[16], (byte) 0, 8);
        Arrays.fill(this.m_abyPacket, (byte) 0);
        byte[] pBuffer = new byte[page_max];
        for (int i = 0; i < n; i++) {
            w_nLen[0] = page_max;
            Arrays.fill(pBuffer, (byte) 0);
            if (USB_ReceiveRawData(pBuffer, w_nLen, false)) {
                System.arraycopy(pBuffer, 0, rxbuff, allLen, w_nLen[0]);
                allLen += w_nLen[0];
            }
        }
        Log.d("=NETLH_E=", " Get the less " + r);
        if (r > 0) {
            w_nLen[0] = r;
            Arrays.fill(pBuffer, (byte) 0);
            if (USB_ReceiveRawData(pBuffer, w_nLen, false)) {
                System.arraycopy(pBuffer, 0, rxbuff, allLen, w_nLen[0]);
                allLen += w_nLen[0];
            }
        }
        Log.d("=NETLH_E=", " Get all length is " + allLen + ",expetLen=" + expetLen);
        this.m_nPacketSize = allLen;
        return true;
    }

    private boolean USB_SendPacket(short wCMD) {
        byte[] btCDB = new byte[8];
        Arrays.fill(btCDB, (byte) 0);
        btCDB[0] = CMD_WRITE;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 60;
        btCDB[4] = (byte) -1;
        if (this.m_usbBase.UsbSCSIWrite(btCDB, 6, this.m_abyPacket, this.m_nPacketSize, SCSI_TIMEOUT)) {
            return USB_ReceiveAck(wCMD);
        }
        return false;
    }

    boolean USB_SendDataPacket(short wCMD) {
        byte[] btCDB = new byte[16];
        memset(btCDB, (byte) 0, 8);
        btCDB[0] = CMD_WRITE;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 60;
        btCDB[4] = (byte) -1;
        if (this.m_usbBase.UsbSCSIWrite(btCDB, 6, this.m_abyPacket, this.m_nPacketSize, SCSI_TIMEOUT)) {
            return USB_ReceiveDataAck(wCMD);
        }
        return false;
    }

    boolean USB_ReceiveRawData(byte[] pBuffer, int[] nDataLen, boolean must) {
        byte[] btCDB = new byte[16];
        memset(btCDB, (byte) 0, 8);
        btCDB[0] = CMD_READ;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 0;
        btCDB[4] = (byte) -1;
        if (this.m_usbBase.UsbSCSIRead(btCDB, 6, pBuffer, nDataLen, SCSI_TIMEOUT, must)) {
            return true;
        }
        return false;
    }

    boolean USB_ReceiveRawData(byte[] pBuffer, int nDataLen) {
        byte[] btCDB = new byte[16];
        memset(btCDB, (byte) 0, 8);
        btCDB[0] = CMD_READ;
        btCDB[1] = (byte) 0;
        btCDB[2] = (byte) 0;
        btCDB[3] = (byte) 0;
        btCDB[4] = (byte) -1;
        if (this.m_usbBase.UsbSCSIRead(btCDB, 6, pBuffer, nDataLen, SCSI_TIMEOUT)) {
            return true;
        }
        return false;
    }

    private boolean USB_ReceiveAck(short wCMD) {
        byte[] btCDB = new byte[8];
        byte[] w_abyWaitPacket = new byte[26];
        Arrays.fill(btCDB, (byte) 0);
        int c = 0;
        Arrays.fill(w_abyWaitPacket, (byte) -81);
        do {
            Arrays.fill(this.m_abyPacket, (byte) 0);
            btCDB[0] = CMD_READ;
            btCDB[1] = (byte) 18;
            if (!this.m_usbBase.UsbSCSIRead(btCDB, 8, this.m_abyPacket, 26, SCSI_TIMEOUT)) {
                return false;
            }
            SystemClock.sleep(40);
            c++;
        } while (memcmp(this.m_abyPacket, w_abyWaitPacket, 26));
        this.m_nPacketSize = 26;
        if (CheckReceive(this.m_abyPacket, this.m_nPacketSize, PACK_HEAD, wCMD)) {
            return true;
        }
        return false;
    }

    boolean USB_ReceiveDataAck(short wCMD) {
        byte[] btCDB = new byte[16];
        byte[] w_WaitPacket = new byte[2];
        memset(btCDB, (byte) 0, 8);
        Arrays.fill(w_WaitPacket, (byte) 0);
        w_WaitPacket[0] = (byte) -17;
        w_WaitPacket[1] = (byte) 2;
        do {
            btCDB[0] = CMD_READ;
            btCDB[1] = (byte) 0;
            btCDB[2] = (byte) 0;
            btCDB[3] = (byte) 0;
            btCDB[4] = (byte) -1;
            if (!this.m_usbBase.UsbSCSIRead(btCDB, 6, this.m_abyPacket, 17, SCSI_TIMEOUT)) {
                return false;
            }
            SystemClock.sleep(40);
        } while (memcmp(this.m_abyPacket, w_WaitPacket, 2));
        int w_nLen = ((short) (((this.m_abyPacket[16] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.m_abyPacket[15] & MotionEventCompat.ACTION_MASK))) + 2;
        if ((w_nLen + 17) + 2 > 839) {
            return false;
        }
        Arrays.fill(this.m_abyPacket2, (byte) 0);
        if (!USB_ReceiveRawData(this.m_abyPacket2, w_nLen)) {
            return false;
        }
        System.arraycopy(this.m_abyPacket2, 0, this.m_abyPacket, 17, w_nLen);
        this.m_nPacketSize = w_nLen + 17;
        if (CheckReceive(this.m_abyPacket, this.m_nPacketSize, PACK_HEAD, wCMD)) {
            return true;
        }
        return false;
    }

    private short GetRetCode() {
        return (short) (((this.m_abyPacket[18] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.m_abyPacket[17] & MotionEventCompat.ACTION_MASK));
    }

    boolean CheckReceive(byte[] pbyPacket, int nPacketLen, short wPrefix, short wCMDCode) {
        if (wPrefix != ((short) (((pbyPacket[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (pbyPacket[0] & MotionEventCompat.ACTION_MASK)))) {
            Log.d("=NETLH_E=", String.format("CheckReceive error1, wPrefix=%d, w_wTmp=%d", new Object[]{Short.valueOf(wPrefix), Short.valueOf((short) (((pbyPacket[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (pbyPacket[0] & MotionEventCompat.ACTION_MASK)))}));
            return false;
        } else if (1 != CmdGetProtocolCheck(pbyPacket)) {
            Log.d("=NETLH_E=", String.format("CheckReceiveCheck CmdGetProtocolCheck error2 ", new Object[0]));
            return false;
        } else {
            if (wCMDCode == ((short) (((pbyPacket[13] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (pbyPacket[12] & MotionEventCompat.ACTION_MASK)))) {
                return true;
            }
            Log.d("=NETLH_E=", String.format("CheckReceive Check Command Code error3, wCMDCode=%d, w_wTmp=%d", new Object[]{Short.valueOf(wCMDCode), Short.valueOf((short) (((pbyPacket[13] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (pbyPacket[12] & MotionEventCompat.ACTION_MASK)))}));
            return false;
        }
    }

    @SuppressLint({"DefaultLocale"})
    public String byteArrToHexString(byte[] b, String bk) {
        String log = "";
        int i = 0;
        while (i < b.length && i <= COMM_SLEEP_TIME) {
            String hex = Integer.toHexString(b[i] & MotionEventCompat.ACTION_MASK);
            if (hex.length() == 1) {
                hex = new StringBuilder(String.valueOf('0')).append(hex).toString();
            }
            log = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(log)).append(hex.toUpperCase()).toString())).append(bk).toString();
            i++;
        }
        Log.d("=NETLH_E=", " " + log);
        return log;
    }

    static {
        System.loadLibrary("NETLH_E");
    }
}
