package per.steve.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer implements Runnable {
	
	private int port;
	ServerSocket ss;
	
	public FileTransferServer(int port) throws IOException {
		this.port = port;
		ss = new ServerSocket(port);
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
	public static void main(String[] args) throws IOException {
		FileTransferServer fts = new FileTransferServer(15124);
		Thread t = new Thread(fts);
		t.start();
	}

	public void run() {
		OutputStream os = null;
		Socket s = null;
		while(true) {
			try {
				System.out.println("Waiting connection...");
				s = ss.accept();
				System.out.println("Accepted connection : " + s);
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String filePath = dis.readUTF();
				File fp = new File(filePath);
				if(!fp.exists()) {
					throw new IOException("not exists file");
				}
				byte[] byteArray = new byte[(int)fp.length()];
				FileInputStream fis = new FileInputStream(fp);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(byteArray, 0, byteArray.length);
				os = s.getOutputStream();
				System.out.println("Sending Files from " + filePath);
				os.write(byteArray, 0, byteArray.length);
				os.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				if(os != null) {
					try {
						os.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				if(s != null) {
					try {
						s.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
		
	}

}
