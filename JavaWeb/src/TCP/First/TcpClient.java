package TCP.First;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) throws IOException {

        // 1.创建发送端Socket对象（创建连接）---三次握手，确保服务器在才开始传输数据
        Socket socket = new Socket(InetAddress.getByName("www.test.io"), 8080);
        // 2.获取输出流对象
        OutputStream outputStream = socket.getOutputStream(); // 抽象类不能直接new出
        // 3.发送数据(写数据就是在发送数据)
        outputStream.write("lsy".getBytes());
        // 4.释放资源
        outputStream.close();
        socket.close();

    }
}
