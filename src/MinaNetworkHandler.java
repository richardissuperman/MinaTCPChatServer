import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


public class MinaNetworkHandler implements IoHandler{
	
	private MsgDispatcher msgDispatcher;
	
	private HashMap<Long, IoSession> sessions=new HashMap<Long, IoSession>(2);
	
   public void setDispatcher(MsgDispatcher dispatcher){
	   this.msgDispatcher=dispatcher;	  
	   this.msgDispatcher.start();
   }

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Exception!");
		System.out.println(arg1.toString());
		
	}


	@Override
	public void messageReceived(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
//		System.out.println("msg has received in session "+arg0.getId());
//		if(arg0.getId()==new Long(1)){
//			sessions.get(new Long(2)).write("session 1 talk to you: "+arg1.toString());
//		}
//		else{
//			sessions.get(new Long(1)).write("session 2 talk to you "+arg1.toString());
//		}
		
		//arg0.write("we have "+sessions.size()+" sessions");
		System.out.println("msg has received in session "+arg0.getId()+" he says "+arg1.toString());
		this.msgDispatcher.dispatchMsg(arg1.toString(),arg0);
		
		
		
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("msg has been sent to session "+arg0.getId());

		
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("session "+arg0.getId()+" has been closed ");
		
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("session "+arg0.getId()+" has been created");

		
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("session is Idle");

		
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("session "+arg0.getId()+" opened!");
		sessions.put(arg0.getId(), arg0);


	}

}
