package window.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import communication.console.TcpSocket;
import communication.dataGetter.ImageDataGetterListener;
import communication.dataGetter.TcpImageDataGetter;
import data.image.Image;

public class TestClientMain implements ImageDataGetterListener {
	public TestClientMain(){
		LogToSystemIO log = new LogToSystemIO();

		TcpSocket tcp = new TcpSocket(log);
		
		try {
			Image img = new Image(new File("DB/source/松村.png"), log);
			tcp.send_img(img);
			System.out.print(tcp.get_md5_list());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 画像をダウンロードして表示
		Executor ex = Executors.newCachedThreadPool();
		ex.execute(new TcpImageDataGetter("松村.png", this, null, log));

		
	}

	public static void main(String[] args) {
		new TestClientMain();
	}

	@Override
	public void set_new_database(byte[] data) {
		try{
		  BufferedImage image = ImageIO.read( new ByteArrayInputStream( data ) );
		  new MyFrame(image);
		}catch( Exception e ){
			
		}		
	}
	
	public class MyFrame extends JFrame{
		private static final long serialVersionUID = 1L;
		private BufferedImage image;
		
		public MyFrame(BufferedImage image){
			this.image = image;
			this.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){System.exit(0);}
				});		
			this.setBounds( 0, 0, 200, 200);
			this.setVisible(true);
		}
		
		public void paint(Graphics g){
			Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(image, 0, 0, this);
		}
	}

}
