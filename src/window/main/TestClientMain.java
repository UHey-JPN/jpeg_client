package window.main;

import communication.console.TcpSocket;

public class TestClientMain {

	public static void main(String[] args) {
		LogToSystemIO log = new LogToSystemIO();

		TcpSocket tcp = new TcpSocket(log);
		tcp.send_img("DB/source/松村.png");
	}

}
