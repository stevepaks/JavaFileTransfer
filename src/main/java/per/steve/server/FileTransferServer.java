package per.steve.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
		DataInputStream dis = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;
		FileInputStream fis = null;
		while(true) {
			try {
				System.out.println("Waiting connection...");
				s = ss.accept();
				System.out.println("Accepted connection : " + s);
				
				//1. extracting the file name from the request
				dis = new DataInputStream(s.getInputStream());
				String filePath = dis.readUTF();
				
				File fp = new File(filePath);
				if(!fp.exists()) {
					throw new IOException("not exists file");
				}
				
				os = s.getOutputStream();
				
				//2. sending size of file
				dos = new DataOutputStream(os);
				dos.writeInt((int)fp.length());
				dos.flush();
				
				//3. sending ack
				dos.writeBoolean(false);
				dos.flush();
				
				//4. waiting reply from the client
				boolean ack = true;
				while(ack) {
					ack = dis.readBoolean();
				}
				
				//5. reading file
				byte[] byteArray = new byte[(int)fp.length()];
				fis = new FileInputStream(fp);
				bis = new BufferedInputStream(fis);
				bis.read(byteArray, 0, byteArray.length);
				
				//6. sending file
				bos = new BufferedOutputStream(os);
				System.out.println("Sending Files from " + filePath);
				bos.write(byteArray, 0, byteArray.length);
				bos.flush();
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
				if(dis != null) {
					try {
						dis.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				if(dos != null) {
					try {
						dos.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				if(bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				if(bos != null) {
					try {
						bos.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				if(fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
 			}
		}
		
	}

}
