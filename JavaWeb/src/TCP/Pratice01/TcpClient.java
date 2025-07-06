package TCP.Pratice01;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    public static void main(String[] args) throws IOException {
        // 客户端一直发送数据给服务端
        while (true) {
            // 获取输入
            System.out.println("输入发送给服务器的内容...");
            Scanner sc = new Scanner(System.in);
            String context = sc.nextLine();
            // 用户输入“66”退出
            if (context.equals("88")) {
                System.out.println("退出客户端！");
                break;
            }
            Socket socket = new Socket("127.0.0.1", 8080);
            // 获取到OutputStream
            OutputStream outputStream = socket.getOutputStream();
            // 发送数据
            outputStream.write(context.getBytes());
            // 接收服务端的数据给客户端
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            System.out.println("客户端接收服务端数据：" + new String(bytes, 0, len));
            // 关闭资源
            outputStream.close();
            socket.close();

        }
    }
}
