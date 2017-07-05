package communication.console;

import java.net.Socket;

import data.image.Image;
import window.main.LogMessageAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

public class TcpSocket{
	public static final String CRLF = "\r\n";
	
	private Socket soc;
	private BufferedReader in;
	private PrintWriter out;
	
	private LogMessageAdapter log_mes;
	

	public TcpSocket(LogMessageAdapter log_mes) {
		this.log_mes = log_mes;
		this.connect_to_server();
	}
	
	
	public synchronized boolean connect_to_server(){
		if( soc == null ){
			String ip = "127.0.0.1";
			int port = 50000;
			InetSocketAddress addr = new InetSocketAddress(ip, port);

			// connect to server
			try {
				soc = new Socket();
				soc.connect(addr, 200);
				soc.setSoTimeout(2000);
				log_mes.log_println("connected to server("+ soc.getRemoteSocketAddress() +")");
			} catch (IOException e) {
				log_mes.log_print(e);
				this.shutdown_connection();
				return false;
			}
			
			try {
				in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				out = new PrintWriter(soc.getOutputStream(), true);
			} catch (IOException e) {
				log_mes.log_print(e);
				this.shutdown_connection();
				return false;
			}
			
			return true;

		}else{
			log_mes.log_println("already login.");
			return false;
		}
	}
	
	public synchronized void shutdown_connection(){
		if( soc != null ){
			try {
				soc.close();
				soc = null;
				log_mes.log_println("socket was closed.");
			} catch (IOException e) {
				log_mes.log_print(e);
			}
		}else{
			log_mes.log_println("socket is already closed.");
		}
	}


	public synchronized boolean send_img(String name) {
		try {
			Image img = new Image(new File(name), log_mes);
			out.println("image add " + name);				// 画像アップロードのコマンド
			String[] soc_info = in.readLine().split(",");	// アップロード先の指示待ち
			String addr = soc_info[0];
			int port = Integer.parseInt(soc_info[1]);
			img.upload(new InetSocketAddress(addr, port));
			
			// サーバーでの処理結果を確認
			if(in.readLine().matches("OK")){
				return true;
			}else{
				return false;
			}
		} catch (FileNotFoundException e) {
			log_mes.log_println("File(" + name + ") is not exist.");
			return false;
		} catch (IOException e) {
			log_mes.log_print(e);
			return false;
		}
	}


}
