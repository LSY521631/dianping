package UDP.Partice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClientTest01 {
    public static void main(String[] args) throws IOException {

        // 创建Socket发送者
        DatagramSocket ds = new DatagramSocket();
        // 客户端发送数据给服务端
        while (true) {
            System.out.println("客户端输入数据...");
            Scanner scanner = new Scanner(System.in);
            String context = scanner.nextLine();
            if ("88".equals(context)) {
                System.out.println("发送者退出...");
                break;
            }

            byte[] bytes = context.getBytes();

            // 封装发送的数据包
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length,
                    InetAddress.getByName("www.test.io"), 8080); // 注意api，不要填错

            // 发送数据
            ds.send(dp);
            System.out.println("数据发送成功...");
        }
        // 关闭
        ds.close();
    }
}
