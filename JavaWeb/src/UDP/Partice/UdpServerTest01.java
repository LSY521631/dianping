package UDP.Partice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServerTest01 {
    static int count = 0;

    public static void main(String[] args) throws IOException {
        // 创建接收Socket对象
        DatagramSocket ds = new DatagramSocket(8080);
        System.out.println("开始接收...");
        // 服务端一直接收客户端传来的信息
        while (true) {
            if (count == 100) {
                break;
                // 没有此跳出循环的语句，idea会报错
            }
            // 创建接收者数据包
            byte[] bytes = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
            ds.receive(dp);
            System.out.println("服务端接收到客户端的消息：" + new String(dp.getData()));
            count++;
        }
        ds.close();
    }
}
