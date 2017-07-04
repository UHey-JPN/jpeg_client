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
	
	/**
	 * プロトコルに従ってファイルを送信。
	 * 送信するデータは次のような感じ
	 * <p style="padding-left:2em">
	 *   image add {@literal <name><CR><LF>}<br>
	 *   size=123456{@literal <CR><LF>}<br>
	 *   DATA本体<br>
	 * </p>
	 * 
	 * @param out - ファイルを送信する先を指定。
	 * @return
	 * {@code true} : 送信処理が正常に終了。<br>
	 * {@code false} : 送信処理が正常に終了。
	 * 
	 * @throws FileNotFoundException
	 */
	public boolean upload(OutputStream out) throws FileNotFoundException{
		// コマンドとファイルサイズの出力
		try {
			String size_info = "";
			size_info += "image add " + file.getName() + CRLF;
			size_info += "size=" + Long.toString(file.length()) + CRLF;
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
	
	/**
	 * MD5を文字列形式で取得
	 * @return
	 * @throws FileNotFoundException
	 */
	public String get_md5_str() throws FileNotFoundException{
		byte[] md = get_md5();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < md.length; i++){
			int b = md[i] & 0xFF;
			if( b < 15 ) sb.append("0");
			sb.append(Integer.toHexString(b));
		}
		return sb.toString();
	}
	
	/**
	 * ハッシュ値をMD5で計算する。
	 *
	 * @return 基本はハッシュ値を返す。何かしらのエラーで{@code null}を返す。
	 * @throws FileNotFoundException
	 */
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
