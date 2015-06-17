package per.steve.client;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import per.steve.server.FileTransferServer;

public class TestClient {
	
	FileTransferServer ftServer;

	@Test
	public void test() throws IOException {
		int fileSize = 1022386;
		byte[] byteArray = new byte[fileSize];
		FileInputStream fis = new FileInputStream("src/test/resources/test.txt");
		int bytesRead = fis.read(byteArray, 0, byteArray.length);

		int currBytesIndex = bytesRead;
		System.out.println(byteArray.length);
		System.out.println(bytesRead);
		do {
			bytesRead = fis.read(byteArray, currBytesIndex, (byteArray.length - currBytesIndex));
			System.out.println(currBytesIndex);
			System.out.println((byteArray.length - currBytesIndex));
			if(bytesRead >= 0) currBytesIndex += bytesRead;
		} while (bytesRead > -1);
	}
	
	@Test
	public void testClient() throws IOException {
		FileTransferClient fileTransferClient = new FileTransferClient("127.0.0.1", 15124, "src/test/resources/client/test.txt");
		fileTransferClient.downloadFile("src/test/resources/server/test.txt");
//		FileUtils.deleteQuietly(new File("src/test/resources/client/test.txt"));
		
	}

}
