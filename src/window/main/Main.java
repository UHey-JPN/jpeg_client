package window.main;

import communication.console.TcpSocket;

public class Main {

	public static void main(String[] args) {
		LogToSystemIO log = new LogToSystemIO();

		TcpSocket tcp = new TcpSocket(log);
		tcp.send("DB/source/松村.png");
	}

}
