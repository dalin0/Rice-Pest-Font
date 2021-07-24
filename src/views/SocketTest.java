package com;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {

	
	public void startSever()
	{
		
		try {
			ServerSocket ss;
			ss = new ServerSocket(9999);
			System.out.println("服务器端以及启动正在接受数据");
			Socket s=ss.accept();
			System.out.println("检测到客户端");
			OutputStream out=s.getOutputStream();
			InputStream in=s.getInputStream();
			BufferedOutputStream bos = new BufferedOutputStream(out);
			//File file = new File("res\\image_01.png");
			//file.createNewFile();
			FileOutputStream fos=new FileOutputStream("res\\image_01.png");
			byte[] buf=new byte[1024];
			int len=0;
			while((len=in.read(buf))!=-1) {
				fos.write(buf,0,len);
			}
			//收到图片，给客户端回送信息
			
			out.write("上传成功".getBytes());
			out.flush();
			//s.shutdownOutput();
			//关闭资源
			fos.close();
			in.close();
			out.close();
			s.close();
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("服务端结束");
		
	}

	
	
}

