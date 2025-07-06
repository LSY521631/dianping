package UDP.First;

import java.io.IOException;
import java.net.*;

public class UdpClient {

    public static void main(String[] args) throws IOException {
        // 1.创建发送端socket对象；
        DatagramSocket datagramSocket = new DatagramSocket();
        // 2.提供数据，并将数据封装到数据包中；
        byte[] msg = "lsy".getBytes();
        // 走dns解析，获取ip地址
        InetAddress inetAddress = InetAddress.getByName("www.test.io");
        int port = 8080;
        /**
         * new DatagramPacket(参数1,参数2,参数3,参数4)
         * 参数1:发送数据,类型是byte数组
         * 参数2:发送数据的长度
         * 参数3:发送到服务器端ip地址
         * 参数4:发送服务器端端口号
         */
        DatagramPacket datagramPacket = new DatagramPacket(msg, msg.length, inetAddress, port);
        // 3.通过socket服务的发送功能，将数据包发出去；
        datagramSocket.send(datagramPacket);
        System.out.println("发送数据成功...");
        // 4.释放资源；
        datagramSocket.close();
    }


}
