package com.POSD.util;

import android.util.Log;

public class bs {
    static int[][] Arbic_Position = new int[][]{new int[]{65152, 65152, 65152, 65152}, new int[]{65154, 65153, 65154, 65153}, new int[]{65156, 65155, 65156, 65155}, new int[]{65158, 65157, 65158, 65157}, new int[]{65160, 65159, 65160, 65159}, new int[]{65162, 65163, 65164, 65161}, new int[]{65166, 65165, 65166, 65165}, new int[]{65168, 65169, 65170, 65167}, new int[]{65172, 65171, 65171, 65171}, new int[]{65174, 65175, 65176, 65173}, new int[]{65178, 65179, 65180, 65177}, new int[]{65182, 65183, 65184, 65181}, new int[]{65186, 65187, 65188, 65185}, new int[]{65190, 65191, 65192, 65189}, new int[]{65194, 65193, 65194, 65193}, new int[]{65196, 65195, 65196, 65195}, new int[]{65198, 65197, 65198, 65197}, new int[]{65200, 65199, 65200, 65199}, new int[]{65202, 65203, 65204, 65201}, new int[]{65206, 65207, 65208, 65205}, new int[]{65210, 65211, 65212, 65209}, new int[]{65214, 65215, 65216, 65213}, new int[]{65218, 65219, 65220, 65217}, new int[]{65222, 65223, 65224, 65221}, new int[]{65226, 65227, 65228, 65225}, new int[]{65230, 65231, 65232, 65229}, new int[]{1595, 1595, 1595, 1595}, new int[]{1596, 1596, 1596, 1596}, new int[]{1597, 1597, 1597, 1597}, new int[]{1598, 1598, 1598, 1598}, new int[]{1599, 1599, 1599, 1599}, new int[]{1600, 1600, 1600, 1600}, new int[]{65234, 65235, 65236, 65233}, new int[]{65238, 65239, 65240, 65237}, new int[]{65242, 65243, 65244, 65241}, new int[]{65246, 65247, 65248, 65245}, new int[]{65250, 65251, 65252, 65249}, new int[]{65254, 65255, 65256, 65253}, new int[]{65258, 65259, 65260, 65257}, new int[]{65262, 65261, 65262, 65261}, new int[]{65264, 65263, 65264, 65263}, new int[]{65266, 65267, 65268, 65265}};
    static byte MAX_CNT_SET1 = (byte) 29;
    static byte MAX_CNT_SET2 = (byte) 42;
    static byte MAX_CNT_SET3 = (byte) 4;
    static byte PERSIAN_AlPHABET_CNT = (byte) 6;
    static int POS_ALONE = 3;
    static int POS_FIRST = 1;
    static int POS_LAST = 0;
    static int POS_MIDILE = 2;
    static int[][] arabic_specs = new int[][]{new int[]{65269, 65270}, new int[]{65271, 65272}, new int[]{65273, 65274}, new int[]{65275, 65276}};
    static int[] theSet1 = new int[]{1574, 1576, 1578, 1579, 1580, 1581, 1582, 1587, 1588, 1589, 1590, 1591, 1592, 1593, 1594, 1600, 1601, 1602, 1603, 1604, 1605, 1606, 1607, 1610, 1662, 1670, 1705, 1711, 1740};
    static int[] theSet2 = new int[]{1570, 1571, 1572, 1573, 1574, 1575, 1576, 1577, 1578, 1579, 1580, 1581, 1582, 1583, 1584, 1585, 1586, 1587, 1588, 1589, 1590, 1591, 1592, 1593, 1594, 1600, 1601, 1602, 1603, 1604, 1605, 1606, 1607, 1608, 1609, 1610, 1662, 1670, 1688, 1705, 1711, 1740};
    int[][] Persian_AlphabetTab = new int[][]{new int[]{64343, 64344, 64345, 64342}, new int[]{64379, 64380, 64381, 64378}, new int[]{64395, 64394, 64395, 64394}, new int[]{64399, 64400, 64401, 64398}, new int[]{64403, 64404, 64405, 64402}, new int[]{64509, 64510, 64511, 64508}};
    int[] pUniStr;

    public bs() {
        int[] iArr = new int[8];
        iArr[0] = 1585;
        iArr[1] = 1586;
        iArr[2] = 1608;
        iArr[3] = 1604;
        iArr[4] = 1608;
        iArr[5] = 1588;
        iArr[6] = 1606;
        this.pUniStr = iArr;
    }

    public int Arbic_Convert(int[] srcWcs, int[] destWcs) {
        int[] sc = srcWcs;
        int len = sc.length - 1;
        Log.i("info", "len == " + len);
        if (len > 0) {
            int[] destWcss = new int[(sc.length + 1)];
            if (destWcss != null) {
                int[] pdsc = destWcss;
                int mid_len = len / 2;
                int i = 0;
                int j = 0;
                while (i < len) {
                    int i2;
                    int wPrevCode = i == 0 ? 0 : sc[i - 1];
                    int wNextCode = i == len + -1 ? 0 : sc[i + 1];
                    int nCode = ConvertRule2(wPrevCode, sc[i], wNextCode);
                    if (nCode == -1) {
                        nCode = ConvertRule1(wPrevCode, sc[i], wNextCode);
                        if (nCode == -1) {
                            nCode = ConvertRule3(wPrevCode, sc[i], wNextCode);
                        }
                    } else {
                        i++;
                    }
                    int j2 = j + 1;
                    if (nCode != -1) {
                        i2 = nCode;
                    } else {
                        i2 = sc[i];
                    }
                    destWcs[j] = i2;
                    i++;
                    j = j2;
                }
                destWcs[j] = 0;
                return 1;
            }
        }
        return 0;
    }

    static int ConvertRule1(int wPrevCode, int wCurCode, int wNextCode) {
        if (wCurCode < 1569 || wCurCode > 1610) {
            return -1;
        }
        int nPos = POS_ALONE;
        int bLinkPrev = IsLinkPrev(wPrevCode);
        boolean b = false;
        boolean c = false;
        if (IsLinkNext(wNextCode) == 1) {
            b = true;
        }
        if (bLinkPrev == 1) {
            c = true;
        }
        if (b && c) {
            nPos = POS_MIDILE;
        } else if (c) {
            nPos = POS_LAST;
        } else if (b) {
            nPos = POS_FIRST;
        }
        return Arbic_Position[wCurCode - 1569][nPos];
    }

    static int ConvertRule2(int wPrevCode, int wCurCode, int wNextCode) {
        if (wCurCode == 1604) {
            int[] theSet3 = new int[]{1570, 1571, 1573, 1575};
            byte i = (byte) 0;
            while (i < MAX_CNT_SET3 && theSet3[i] != wNextCode) {
                i = (byte) (i + 1);
            }
            if (i != MAX_CNT_SET3) {
                return arabic_specs[i][IsLinkPrev(wPrevCode)];
            }
        }
        return -1;
    }

    static int IsLinkPrev(int wCode) {
        if (wCode != 0) {
            int[] pwData = theSet1;
            long low = 0;
            long high = (long) (MAX_CNT_SET1 - 1);
            while (low <= high) {
                long mid = (high + low) / 2;
                if (pwData[(int) mid] == wCode) {
                    return 1;
                }
                if (wCode > pwData[(int) mid]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return 0;
    }

    static int IsLinkNext(int wCode) {
        if (wCode != 0) {
            int[] pwData = theSet2;
            long low = 0;
            long high = (long) (MAX_CNT_SET2 - 1);
            while (low <= high) {
                long mid = (high + low) / 2;
                if (pwData[(int) mid] == wCode) {
                    return 1;
                }
                if (wCode > pwData[(int) mid]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return 0;
    }

    int ConvertRule3(int wPrevCode, int wCurCode, int wNextCode) {
        if (wCurCode >= 1536 && wCurCode <= 1791) {
            int[] Persian_Alphabets = new int[]{1662, 1670, 1688, 1705, 1711, 1740};
            byte i = (byte) 0;
            while (i < PERSIAN_AlPHABET_CNT && wCurCode != Persian_Alphabets[i]) {
                i++;
            }
            if (i < PERSIAN_AlPHABET_CNT) {
                int nPos = POS_ALONE;
                int bLinkPrev = IsLinkPrev(wPrevCode);
                boolean b = false;
                boolean c = false;
                if (IsLinkNext(wNextCode) == 1) {
                    b = true;
                }
                if (bLinkPrev == 1) {
                    c = true;
                }
                if (b && c) {
                    nPos = POS_MIDILE;
                } else if (c) {
                    nPos = POS_LAST;
                } else if (b) {
                    nPos = POS_FIRST;
                }
                return this.Persian_AlphabetTab[i][nPos];
            }
        }
        return -1;
    }

    public int IsIncludeArbic(int[] wcs) {
        int[] sc = wcs;
        int len = sc.length;
        int i = 0;
        while (i < len) {
            if ((sc[i] >= 1536 && sc[i] <= 1791) || ((sc[i] >= 1872 && sc[i] <= 1919) || ((sc[i] >= 64336 && sc[i] <= 65023) || (sc[i] >= 65136 && sc[i] <= 65279)))) {
                return 1;
            }
            i++;
        }
        return 0;
    }
}
