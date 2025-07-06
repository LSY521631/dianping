package UDP.First;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    public static void main(String[] args) throws IOException {
        // 1.创建接收端socket对象；
        // 建立监听端口号
        int port = 8080;
        DatagramSocket datagramSocket = new DatagramSocket(port);

        // 2.接收数据；
        byte[] bytes = new byte[1024];
        // 数据包的形式接收
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        // 开始监听客户端发送给服务器端数据，如果没有监听到数据则一直阻塞等待
        System.out.println("开始接收客户端发送的数据...");
        datagramSocket.receive(datagramPacket);
        System.out.println("接收到数据...");

        // 3.解析数据
        byte[] data = datagramPacket.getData();
        String msg = new String(data);

        // 4.输出数据(客户端发送给服务端的内容)
        System.out.println(msg);
        // 5.释放资源
        datagramSocket.close();
    }
}
