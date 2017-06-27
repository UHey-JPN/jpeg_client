package data.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
			int len;
			byte[] buffer = new byte[512];
			
			FileInputStream file_in = new FileInputStream(file);
			
			while ( (len = file_in.read(buffer)) > 0 ) {
				out.write(buffer, 0, len);
			}
			file_in.close();
			return true;
			
		} catch (IOException e) {
			log_mes.log_print(e);
			return false;
		}
	}
	
	public byte[] get_md5() throws FileNotFoundException{
		MessageDigest md;
		FileInputStream file_in = new FileInputStream(file);
		
		try {
			md = MessageDigest.getInstance("MD5");
			
			int len;
			byte[] buffer = new byte[512];
			while ( (len = file_in.read(buffer)) > 0 ) {
				md.update(buffer, 0, len);
			}
			
			file_in.close();
			
			return md.digest();
		} catch (NoSuchAlgorithmException|IOException e) {
			log_mes.log_print(e);
			return null;
		}

	}

}
