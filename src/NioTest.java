
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;



public class NioTest {
	
	
	public static void main(String[] args) throws IOException{
		
		scatterRead();
		
	}
	
	public static void scatterRead() throws IOException{
		
		SocketChannel channel=SocketChannel.open();
		channel.connect(new InetSocketAddress("192.168.1.2",8085));
		channel.configureBlocking(true);
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.clear();
		buf.flip();
		while(true){
			if(channel.read(buf)!=-1){
				while(buf.hasRemaining()){
					System.out.println((char)buf.get());
				}
			}
			
		}
	}

}
