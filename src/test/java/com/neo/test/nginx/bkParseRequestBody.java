package com.neo.test.nginx;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by neowyp on 2016/6/7.
 * Author   : wangyunpeng
 * Date     : 2016/6/7
 * Time     : 18:53
 * Version  : V1.0
 * Desc     :
 */
@Slf4j
public class bkParseRequestBody {

    // ←↑→↓↖↙↗↘↕
    public static void main(String[] args) {
        String body = "<xml>\\x0D\\x0A<appid><![CDATA[wx9f37c95514a62115]]></appid>\\x0D\\x0A<attach><![CDATA[\\xE5\\x95\\x86\\xE5\\x93\\x81\\xE6\\x9D\\xA5\\xE6\\xBA\\x90\\xE5\\x9B\\xBD\\xE7\\xBE\\x8EPlus APP]]></attach>\\x0D\\x0A<bank_type><![CDATA[CFT]]></bank_type>\\x0D\\x0A<fee_type><![CDATA[CNY]]></fee_type>\\x0D\\x0A<is_subscribe><![CDATA[N]]></is_subscribe>\\x0D\\x0A<mch_id><![CDATA[1271069601]]></mch_id>\\x0D\\x0A<nonce_str><![CDATA[a8a36e0de19d4e3eb762feee393f0c97]]></nonce_str>\\x0D\\x0A<openid><![CDATA[opoEYvzBHR10A_PDlGvk_0xuJ-3A]]></openid>\\x0D\\x0A<out_trade_no><![CDATA[201606070256091100011111100007]]></out_trade_no>\\x0D\\x0A<result_code><![CDATA[SUCCESS]]></result_code>\\x0D\\x0A<return_code><![CDATA[SUCCESS]]></return_code>\\x0D\\x0A<sign><![CDATA[C6B76111F9B41C4EA14A419971BAAE52]]></sign>\\x0D\\x0A<time_end><![CDATA[20160607025615]]></time_end>\\x0D\\x0A<total_fee>5800</total_fee>\\x0D\\x0A<cash_fee>5800</cash_fee>\\x0D\\x0A<trade_type><![CDATA[APP]]></trade_type>\\x0D\\x0A<transaction_id><![CDATA[4004562001201606076885193832]]></transaction_id>\\x0D\\x0A</xml>\\x0D\\x0A";
        System.out.println(body);

        String[] xt = body.split("(\\\\x\\w{2})+");
        for (int i = 0; i < xt.length; i++) {
//            System.out.println(xt[i]);
        }
        Pattern p = Pattern.compile("(\\\\x\\w{2})+");
        Matcher m = p.matcher(body);
        int lastPos = 0;
        StringBuffer xmlOut = new StringBuffer("");
        while (m.find()) {
//            count++;
//            System.out.println("Match number " + count);
//            System.out.println("start(): " + m.start());
//            System.out.println("end(): " + m.end());
            //log.info("{}-{}-{}:{}", count, m.start(), m.end(), body.substring(m.start(), m.end()));
//            log.info("{}{}", body.substring(lastPos, m.start()), convertHextoString(body.substring(m.start(), m.end())));
            xmlOut.append(body.substring(lastPos, m.start()));
            xmlOut.append(convertHextoString(body.substring(m.start(), m.end())));
            lastPos = m.end();
        }
        System.out.println(xmlOut.toString());
        System.out.println(body.replaceAll("\\\\x\\w{2}", "**"));

//        byte[] tmp = new byte[]{0x0D, 0x0A};
//        System.out.println("[==" + new String(tmp) + "]");

        StringBuffer buf = new StringBuffer();
        StringBuffer hex = new StringBuffer();
        int postion = 0, index = 0;
        int loop = 0;
        for (; ; ) {
            loop++;
            if (loop > body.length())
                break;
//            log.info("index:{},postion:{}", index, postion);
            index = body.indexOf("\\x", postion);
            if (index == -1) {
                break;
            }

            String sxml = body.substring(postion, index);
            if ((index - postion) > 4) {

                String hs = convertHextoString(hex.toString());
                buf.append(hs);

                hex.setLength(0);

                buf.append(sxml);
                postion = index;

//                System.out.println("【" + sxml + "】【" + hex.toString() +"】");
            }

            String shex = body.substring(index, index + 4);
            hex.append(shex);
            postion = index + 4;


        }
//        System.out.println(hex.toString());
        System.out.println(buf.toString());


    }

    private static String convertHextoString(String input) {
        try {
            String hexstr = input.toString().replaceAll("\\\\x", "");
            byte[] hexbyte = DataUtil.hexStringToByte(hexstr);
            return new String(hexbyte, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
