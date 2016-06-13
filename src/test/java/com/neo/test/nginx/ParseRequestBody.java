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
public class ParseRequestBody {

    public static void main(String[] args) {
        String body = "<xml>\\x0D\\x0A<appid><![CDATA[wx9f37c95514a62115]]></appid>\\x0D\\x0A<attach><![CDATA[\\xE5\\x95\\x86\\xE5\\x93\\x81\\xE6\\x9D\\xA5\\xE6\\xBA\\x90\\xE5\\x9B\\xBD\\xE7\\xBE\\x8EPlus APP]]></attach>\\x0D\\x0A<bank_type><![CDATA[CFT]]></bank_type>\\x0D\\x0A<fee_type><![CDATA[CNY]]></fee_type>\\x0D\\x0A<is_subscribe><![CDATA[N]]></is_subscribe>\\x0D\\x0A<mch_id><![CDATA[1271069601]]></mch_id>\\x0D\\x0A<nonce_str><![CDATA[a8a36e0de19d4e3eb762feee393f0c97]]></nonce_str>\\x0D\\x0A<openid><![CDATA[opoEYvzBHR10A_PDlGvk_0xuJ-3A]]></openid>\\x0D\\x0A<out_trade_no><![CDATA[201606070256091100011111100007]]></out_trade_no>\\x0D\\x0A<result_code><![CDATA[SUCCESS]]></result_code>\\x0D\\x0A<return_code><![CDATA[SUCCESS]]></return_code>\\x0D\\x0A<sign><![CDATA[C6B76111F9B41C4EA14A419971BAAE52]]></sign>\\x0D\\x0A<time_end><![CDATA[20160607025615]]></time_end>\\x0D\\x0A<total_fee>5800</total_fee>\\x0D\\x0A<cash_fee>5800</cash_fee>\\x0D\\x0A<trade_type><![CDATA[APP]]></trade_type>\\x0D\\x0A<transaction_id><![CDATA[4004562001201606076885193832]]></transaction_id>\\x0D\\x0A</xml>\\x0D\\x0A";
        System.out.println(body);

        //逐个读取解析
        String buf= parseByRead(body);
        //如果集成到生产工程的代码中，不要使用system.out
        System.out.println("--------------------------------------");
        System.out.println(buf);

        //正则读取解析
        buf = parseByRegex(body);
        System.out.println("--------------------------------------");
        System.out.println(buf);
    }

    /**
     * 使用正则表达式读取和转换微信支付结果通知的
     * @param input
     * @return
     */
    private static String parseByRegex(String input) {
        // [\\\\x] 表示 [\x]
        // [\\w] 表示 [\x]
        // {2}表示2位
        // +表示重复一次或多次
        Pattern p = Pattern.compile("(\\\\x\\w{2})+");
        Matcher m = p.matcher(input);
        /**
         *          <xml>\x0D\x0A<appid><![CDATA[wx9f37c95514a62115]]></appid>\x0D\x0A<attach>
         *          ↑   ↑     ↑
         *     lastPos m.start  m.end
         *          <xml>\x0D\x0A<appid><![CDATA[wx9f37c95514a62115]]></appid>\x0D\x0A<attach>
         *                      ↑                                            ↑     ↑
         *                    lastPos                                     m.start   m.end
         */
        int lastPos = 0;
        StringBuffer xmlOut = new StringBuffer("");
        while (m.find()) {
            //将xml追加到xmlOut中
            xmlOut.append(input.substring(lastPos, m.start()));
            //将正则表达式部分解析转换后追加到xmlOut中
            xmlOut.append(convertHextoString(input.substring(m.start(), m.end())));
            lastPos = m.end();
        }
//        System.out.println(xmlOut.toString());
        return xmlOut.toString();
    }
    /**
     * 通过读取逐个读取字符串来转换和解析微信支付异步通知
     * @param input
     * @return
     */
    private static String parseByRead(String input) {
        StringBuffer outXml = new StringBuffer();
        StringBuffer hex = new StringBuffer();
        int postion = 0, index = 0;
        for (; ; ) {
            index = input.indexOf("\\x", postion);
            //如果查询不到就跳出循环
            if (index == -1) {
                break;
            }
            String sxml = input.substring(postion, index);
            if ((index - postion) > 4) {
                //先将hex解析后追加到outXml中
                String hs = convertHextoString(hex.toString());
                outXml.append(hs);
                //清空hex
                hex.setLength(0);
                //再将新读取的字符串追加到outXml中
                outXml.append(sxml);
                postion = index;
//                log.info("hs:[{}], hex:[{}], outXml:[{}]", hs, hex, outXml);
            }
            //每次查询到\x的位置，就读取从此开始的4个字符，暂存在hex中
            String shex = input.substring(index, index + 4);
            hex.append(shex);
            //postion加4保证循环继续
            postion = index + 4;
        }

//        System.out.println(outXml.toString());
        return outXml.toString();
    }

    /**
     * 将\xEF格式的十六进制先转换成byte[]，再转换成String
     * @param input  \xEF\xBE ...
     * @return
     */
    private static String convertHextoString(String input) {
        try {
            //删除\x
            String hexstr = input.toString().replaceAll("\\\\x", "");
            //将十六进制字符串转换成byte[]   BE => 0xBE
            //DataUtil工具类用于hex与string相互转换
            byte[] hexbyte = DataUtil.hexStringToByte(hexstr);
            //在转换成字符串输出
            return new String(hexbyte, "UTF-8");
        } catch (Exception e) {
            //如果集成到生产工程的代码中，请对异常做处理
            e.printStackTrace();
        }
        return "";
    }
}
