import org.apache.mina.core.session.IoSession;


public class Msg {
	
	public String msgbody;
	public IoSession iosession;
	
	
	public Msg(String msg, IoSession session){
		
		msgbody=msg;
		iosession=session;
		
	}

}
