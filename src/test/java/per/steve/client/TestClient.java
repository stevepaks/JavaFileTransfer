package per.steve.client;

import java.io.IOException;

import org.junit.Test;

public class TestClient {
	
	@Test
	public void testClient() throws IOException {
		FileTransferClient fileTransferClient = new FileTransferClient("127.0.0.1", 15124, "src/test/resources/client/test.txt");
		fileTransferClient.downloadFile("src/test/resources/server/test.txt");
//		FileUtils.deleteQuietly(new File("src/test/resources/client/test.txt"));
		
	}

}
