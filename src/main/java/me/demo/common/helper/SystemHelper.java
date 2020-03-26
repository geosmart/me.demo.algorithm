package me.demo.common.helper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class SystemHelper {
    static String localhost = null;

    public synchronized static String getLocalHostAddress() throws SocketException {
        if (localhost == null)
            localhost = inGetLocalHostAddress();
        return localhost;
    }

    static String inGetLocalHostAddress() throws SocketException {
        String localip = "127.0.0.1";// 本地IP，如果没有配置外网IP则返回它
        String netip = "";// 外网IP
        String ipprefix = System.getenv("IP_PREFIX");
        Enumeration<NetworkInterface> netInterfaces =
                NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (ipprefix != null && !ipprefix.isEmpty()) {
                    if (ip.getHostAddress().startsWith(ipprefix))
                        return ip.getHostAddress();
                } else {
                    if (!ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netip = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public static void main(String[] args){
        try {
            String addr = getLocalHostAddress();
            System.out.println(addr);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
