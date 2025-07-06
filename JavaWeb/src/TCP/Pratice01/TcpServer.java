package TCP.Pratice01;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * getInputStream----按受数据
 * getOutputStream---发送数据 写数据
 */
public class TcpServer {
    public static void main(String[] args) throws IOException {
        // 创建socket对象，监听客户端发送的数据，
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务端启动成功...");
        while (true) {
            // 接收客户端数据，该方法当没有接收到客户端的数据，会一直阻塞
            Socket accept = serverSocket.accept();
            // 接收客户端数据
            InputStream inputStream = accept.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);
            System.out.println("服务端接收到数据：" + new String(bytes, 0, len));
            // 服务端响应数据给客户端
            OutputStream outputStream = accept.getOutputStream();
            String resp = "我收到啦" + UUID.randomUUID().toString();
            outputStream.write(resp.getBytes());
            // 关闭资源
            inputStream.close();
            accept.close();

        }

    }
}
