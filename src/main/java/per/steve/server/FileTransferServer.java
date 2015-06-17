package per.steve.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer {
	
	private int port;
	private String filePath;
	
	public FileTransferServer(int port, String filePath) {
		this.port = port;
		this.filePath = filePath;
	}
	
	public void runService() {
		OutputStream os = null;
		Socket s = null;
		try {
			ServerSocket ss = new ServerSocket(this.port);
			System.out.println("Waiting connection...");
			s = ss.accept();
			System.out.println("Accepted connection : " + s);
			File fp = new File(filePath);
			byte[] byteArray = new byte[(int)fp.length()];
			FileInputStream fis = new FileInputStream(fp);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(byteArray, 0, byteArray.length);
			os = s.getOutputStream();
			System.out.println("Sending Files...");
			os.write(byteArray, 0, byteArray.length);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public static void main(String[] args) {
		FileTransferServer fts = new FileTransferServer(15124, "src/test/resources/test.txt");
		fts.runService();
	}

}
