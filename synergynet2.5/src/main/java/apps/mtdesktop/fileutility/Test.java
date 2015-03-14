package apps.mtdesktop.fileutility;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class Test implements FileTransferListener{
	
	public Test(){
		try {
			String ftpServlet = "http://"+InetAddress.getLocalHost().getHostAddress()+":8080/ftpServlet";

			AssetRegistry.getInstance().addFileTransferListener(this);
			AssetRegistry.getInstance().registerAsset(ftpServlet, UUID.randomUUID().toString(), "C:\\123\\vvv.mpg");
			//AssetRegistry.getInstance().getAsset("http://"+InetAddress.getLocalHost().getHostAddress()+":8080/FileServer", "vvv.mpg", "C:/1");

		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){
		new Test();
	}

	@Override
	public void fileUploadStarted(File file) {
		System.out.println("upload started");
	}

	@Override
	public void fileUploadCompleted(File file) {
		System.out.println("upload complete");

	}

	@Override
	public void fileDownloadStarted(File file) {
		 
		
	}

	@Override
	public void fileDownloadCompleted(File file) {
		 
		
	}

	@Override
	public void fileUploadFailed(File file, String responseMessage) {
		System.out.println("upload failed");
		
	}

	@Override
	public void fileDownloadFailed(File file, String responseMessage) {
		 
		
	}
}