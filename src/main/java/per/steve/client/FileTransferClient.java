package per.steve.client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileTransferClient {
	
	private String hostAddress;
	private int port;
	private String targetPath;

	public FileTransferClient(String hostAddress, int port, String targetPath) {
		this.hostAddress = hostAddress;
		this.port = port;
		this.targetPath = targetPath;
	}
	
	public void downloadFile(String string) {
		int fileSize = 1022386;
		int bytesRead;
		int currBytesIndex = 0;
		Socket s = null;
		DataOutputStream dos = null;
		BufferedOutputStream bos = null;
		try {
			s = new Socket(hostAddress, port);
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(string);
			dos.flush();
			
			byte[] byteArray = new byte[fileSize];
			InputStream is = s.getInputStream();
			File fp = new File(targetPath);
			if(!fp.exists()) {
				fp.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(fp);
			bos = new BufferedOutputStream(fos);
			bytesRead = is.read(byteArray, 0, byteArray.length);
			currBytesIndex = bytesRead;
			do {
				bytesRead = is.read(byteArray, currBytesIndex, (byteArray.length - currBytesIndex));
				if(bytesRead >= 0) {
					currBytesIndex += bytesRead;
				}
			} while (bytesRead > -1);
			bos.write(byteArray, 0, currBytesIndex);
			bos.flush();
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			if(dos != null) {
				try {
					dos.close();
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
			if(bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
