package TCP.Pratice02;

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
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入用户名称");
            String userName = sc.nextLine();
            System.out.println("请输入用户密码");
            String uesrPwd = sc.nextLine();

            Socket socket = new Socket("127.0.0.1", 8080);
            // 获取到OutputStream
            OutputStream outputStream = socket.getOutputStream();
            // 发送数据
            String context = "userName=" + userName + "&uesrPwd=" + uesrPwd;
            outputStream.write(context.getBytes());
            // 接收服务端的数据给客户端
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len = inputStream.read(bytes);

            // 获取到服务器端响应数据
            String resq = new String(bytes, 0, len);
            if ("ok".equals(resq)) {
                System.out.println("登陆成功！");
            } else {
                System.out.println("登陆失败！");
            }
            // 关闭资源
            outputStream.close();
            socket.close();

        }
    }
}
