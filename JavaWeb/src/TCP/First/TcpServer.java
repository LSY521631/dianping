package TCP.First;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) throws IOException {

        // 1.创建接收端Socket对象
        ServerSocket serverSocket = new ServerSocket(8080);
        // 2.监听客户端发送数据给服务端
        //   如果客户端一直没有发送数据给服务器,导致服务器端会在该方法一直阻塞
        Socket accept = serverSocket.accept();
        // 3.获取输入流对象
        InputStream inputStream = accept.getInputStream();
        byte[] bytes = new byte[1024];
        int len = inputStream.read(bytes); // read()方法从InputStream读取数据，并将其存储到提供的字节数组中
        // 4.输出数据
        System.out.println("客户端发送的数据：" + new String(bytes, 0, len));
        // 5.释放资源
        inputStream.close();
        serverSocket.close();


    }
}
