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


	public synchronized void send(String name) {
		try {
			out.flush();
			Image img = new Image(new File(name), log_mes);
			img.upload(soc.getOutputStream());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
