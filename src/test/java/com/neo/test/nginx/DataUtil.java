package com.neo.test.nginx;

/**
 * Created by neowyp on 2016/6/8.
 * Author   : wangyunpeng
 * Date     : 2016/6/8
 * Time     : 12:40
 * Version  : V1.0
 * Desc     :
 */
public class DataUtil {

    /**
     * desc: 将hex字符串转换成byte[]
     * <p>创建人：wangyunpeng , 2014-5-15上午10:22:49</p>
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * desc: 将char转换成byet
     * <p>创建人：wangyunpeng , 2014-5-15上午10:23:46</p>
     *
     * @param c
     * @return
     */
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * desc: 将byte[]转换成hex字符串
     * <p>创建人：wangyunpeng , 2014-5-15上午10:24:04</p>
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        if (bArray == null || bArray.length == 0)
            return "";
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


}
