package InetAddressTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class test01 {

    public static void main(String[] args) throws UnknownHostException {

        // 获取给定主机名的的IP地址，host参数表示指定主机
        // 以太网适配器 以太网的ipv4
        InetAddress inetAddress1 = InetAddress.getByName("127.0.0.1");
        InetAddress inetAddress = InetAddress.getByName("localhost");
        String hostName1 = inetAddress1.getHostName();
        System.out.println(hostName1);
        // 获取ip地址
        String address1 = inetAddress1.getHostAddress();
        System.out.println(address1);

        // PPP 适配器 ZUCCVPN的ipv4
        InetAddress inetAddress2 = InetAddress.getByName("10.171.192.35");
        String hostName2 = inetAddress1.getHostName();
        System.out.println(hostName2);
        String address2 = inetAddress1.getHostAddress();
        System.out.println(address2);

    }
}
