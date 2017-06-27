package data.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import window.main.LogMessageAdapter;

public class Image {
	public static final String CRLF = "\r\n";
	
	private File file;
	
	private LogMessageAdapter log_mes;
	
	public Image(File file, LogMessageAdapter log_mes) throws FileNotFoundException{
		if( !file.exists() ){
			throw new FileNotFoundException();
		}
		this.log_mes = log_mes;
		this.file = file;
	}
	
	public boolean upload(OutputStream out) throws FileNotFoundException{
		// ファイルサイズの出力
		try {
			String size_info = "size=" + Long.toString(file.length()) + CRLF;
			out.write(size_info.getBytes());
		} catch (IOException e) {
			log_mes.log_print(e);
			return false;
		}
		
		// ファイルデータの出力
		try {
			int fileLength;
			byte[] buffer = new byte[512];
			
			InputStream inputStream = new FileInputStream(file);
			
			while ( (fileLength = inputStream.read(buffer)) > 0 ) {
				out.write(buffer, 0, fileLength);
			}
			inputStream.close();
			return true;
			
		} catch (IOException e) {
			log_mes.log_print(e);
			return false;
		}
	}

}
