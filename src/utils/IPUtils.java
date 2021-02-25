package utils;


import java.net.InetAddress;


public class IPUtils {
    public static String getLocalIP() {
        try {
            String localip = "";
            InetAddress addr = (InetAddress)InetAddress.getLocalHost();
            // 获取本机IP
            localip = addr.getHostAddress().toString();

            return localip;
        } catch (Exception e) {
            return null;
        }
    }

}
