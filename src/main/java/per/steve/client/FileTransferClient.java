package per.steve.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
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
	
	public void downloadFile(String downloadingFileName) {
		int bytesRead;
		int currBytesIndex = 0;
		Socket s = null;
		InputStream is = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			s = new Socket(hostAddress, port);
			
			//1. request for getting file size
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(downloadingFileName);
			dos.flush();
			
			is = s.getInputStream();
			
			dis = new DataInputStream(is);
			int recievedFileSize = 0;
			boolean ack = true;
			//2. waiting and recieving file size
			while(ack) {
				recievedFileSize = dis.readInt();
				if(recievedFileSize > 0) {
					ack = dis.readBoolean();
				}
			}
			
			//3. sending ack
			dos.writeBoolean(false);
			dos.flush();
			
			//4. recieving file
			byte[] byteArray = new byte[recievedFileSize + 1];
			bis = new BufferedInputStream(is);
			bytesRead = bis.read(byteArray, 0, byteArray.length);
			currBytesIndex = bytesRead;
			do {
				bytesRead = bis.read(byteArray, currBytesIndex, (byteArray.length - currBytesIndex));
				if(bytesRead >= 0) {
					currBytesIndex += bytesRead;
				}
			} while (bytesRead > -1);

			//5. saving file
			File fp = new File(targetPath);
			if(!fp.exists()) {
				fp.createNewFile();
			}
			fos = new FileOutputStream(fp);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray, 0, currBytesIndex);
			bos.flush();
			System.out.println("Success Recieving Files : " + downloadingFileName);
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
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
			if(s != null) {
				try {
					s.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if(is != null) {
				try {
					is.close();
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
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

}
