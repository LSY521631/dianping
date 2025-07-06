package TCP.Pratice02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class TcpServer {
    public static void main(String[] args) throws IOException {
        // 创建socket对象，监听客户端发送的数据，
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务端启动成功...");
        while (true) {
            // 接收客户端数据，该方法当没有接收到客户端的数据，会一直阻塞
            Socket accept = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 接收客户端数据
                        InputStream inputStream = accept.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = inputStream.read(bytes);
                        // 服务器端接受客户端数据包:userName=lsy&userPwd=123
                        String reqtext = new String(bytes, 0, len);
                        String userName = reqtext.split("&")[0].split("=")[1];// 重要
                        String userPwd = reqtext.split("&")[1].split("=")[1];// 重要

                        // 服务端响应数据给客户端 如果验证正确则返回ok,否则返回fail
                        OutputStream outputStream = accept.getOutputStream();
                        if ("lsy".equals(userName) && "123".equals(userPwd)) {
                            outputStream.write("ok".getBytes());
                        } else {
                            outputStream.write("fail".getBytes());
                        }

                        // 关闭资源
                        inputStream.close();
                        outputStream.close();
                        accept.close();
                    } catch (Exception e) {
                    }
                }
            }).start();


        }

    }
}
