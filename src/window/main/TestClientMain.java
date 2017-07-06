package window.main;

import java.io.File;
import java.io.IOException;

import communication.console.TcpSocket;
import data.image.Image;

public class TestClientMain {

	public static void main(String[] args) {
		LogToSystemIO log = new LogToSystemIO();

		TcpSocket tcp = new TcpSocket(log);
		
		try {
			Image img = new Image(new File("DB/source/松村.png"), log);
			tcp.send_img(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
