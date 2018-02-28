
package com.ceo.fucklj.util;

import java.text.NumberFormat;

public final class StringUtil {

    /****************** CHAR ***********************/

    public static boolean isNumber(char ch) {
        if (ch >= '0' && ch <= '9') {
            return true;
        }
        return false;
    }

    public static boolean isLetter(char ch) {
        if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')) {
            return true;
        }
        return false;
    }

    public static boolean isChinese(char ch) {
        if (ch >= 0x4e00 && ch <= 0x9fa5) {
            return true;
        }
        return false;
    }

    public static boolean isChineseBiaodiao(char ch) {
        if (ch >= 0x3000 && ch <= 0x303F) {
            return true;
        }
        return false;
    }

    /********************* HEX ********************/

    private static final char[] HEX_DIGITS_UPPER = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_DIGITS_UPPER[(b & 0xf0) >> 4]);
            sb.append(HEX_DIGITS_UPPER[b & 0x0f]);
        }
        return sb.toString();
    }

    public static NumberFormat sSizeFormat;
    static {
        sSizeFormat = NumberFormat.getInstance();
        sSizeFormat.setMaximumFractionDigits(1);
        sSizeFormat.setMinimumFractionDigits(1);
    }

    /**
     * return a string in human readable format
     *
     * @param bytes
     *            单位 B
     */
    public static String getHumanReadableSize(long bytes) {
        if (bytes == 0) {
            return "0";
        } else if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1048576) {
            return sSizeFormat.format(bytes / 1024f) + "KB";
        } else {
            return sSizeFormat.format(bytes / 1048576f) + "MB";
        }
    }

    /**
     * return a string in human readable format,no fraction
     *
     * @param bytes
     *            单位 B
     */
    public static String getHumanReadableSizeNoFraction(long bytes) {
        NumberFormat sizeFormat = NumberFormat.getInstance();
        sizeFormat.setMaximumFractionDigits(0);

        if (bytes == 0) {
            return "0";
        } else if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1048576) {
            return sizeFormat.format(bytes / 1024f) + "KB";
        } else {
            return sizeFormat.format(bytes / 1048576f) + "MB";
        }
    }

    /**
     * return a string in human readable format
     *
     * @param bytes
     *            单位 B
     */
    public static String getHumanReadableSizeMore(long bytes) {
        if (bytes == 0) {
            return "0MB";
        } else if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1048576) {
            return sSizeFormat.format(bytes / 1024f) + "KB";
        } else if (bytes < 1073741824L) {
            return sSizeFormat.format(bytes / 1048576f) + "MB";
        } else if (bytes < 1099511627776L) {
            return sSizeFormat.format(bytes / 1073741824f) + "GB";
        } else {
            return sSizeFormat.format(bytes / 1099511627776f) + "T";
        }
    }

    /**
     * return a string in human readable format
     *
     * @param sizekb
     *            单位 KB
     * @return 0, KB, MB
     */
    public static String getHumanReadableSizeByKb(long sizekb) {
        if (sizekb == 0) {
            return "0";
        } else if (sizekb < 1024) {
            return sizekb + "KB";
        } else {
            return sSizeFormat.format(sizekb / 1024f) + "MB";
        }
    }

    public static String[] getFormatSize(long bytes) {
        String[] result = new String[2];
        if (bytes == 0) {
            result[0] = "0";
            result[1] = "MB";
        } else if (bytes < 1024) {
            result[0] = bytes + "";
            result[1] = "B";
        } else if (bytes < 1048576) {
            result[0] = sSizeFormat.format(bytes / 1024f) + "";
            result[1] = "KB";
        } else if (bytes < 1048576 * 1024) {
            result[0] = sSizeFormat.format(bytes / 1024f / 1024f) + "";
            result[1] = "MB";
        } else {
            result[0] = sSizeFormat.format(bytes / 1024f / 1024f / 1024f) + "";
            result[1] = "GB";
        }
        return result;
    }

    /**
     * 字符串转换成int
     *
     * @param str
     * @return
     */
    public static int str2Int(String str, int defValue) {
        int ret = defValue;
        try {
            if (!TextUtils.isEmpty(str)) {
                ret = Integer.parseInt(str.trim());
            }
        } catch (Exception ex) {
            //ignore
        }
        return ret;
    }

    /**
     * 字符串转换成long
     *
     * @param str
     * @return
     */
    public static long str2Long(String str, long defValue) {
        long ret = defValue;
        try {
            if (!TextUtils.isEmpty(str)) {
                ret = Long.parseLong(str.trim());
            }
        } catch (Exception ex) {
            //ignore
        }
        return ret;
    }

    /**
     * 字符串转换成float
     *
     * @param str
     * @return
     */
    public static float str2Float(String str, float defValue) {
        float ret = defValue;
        try {
            if (!TextUtils.isEmpty(str)) {
                ret = Float.parseFloat(str.trim());
            }
        } catch (Exception ex) {
            //ignore
        }
        return ret;
    }

    /**
     * 字符串转换成double
     *
     * @param str
     * @return
     */
    public static double str2Double(String str, double defValue) {
        double ret = defValue;
        try {
            if (!TextUtils.isEmpty(str)) {
                ret = Double.parseDouble(str.trim());
            }
        } catch (Exception ex) {
            //ignore
        }
        return ret;
    }

}
