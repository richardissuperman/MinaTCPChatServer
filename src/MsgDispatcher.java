import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.apache.mina.core.session.IoSession;


public class MsgDispatcher extends Thread {
	
	private HashMap<String,IoSession> map;
	private OfflineMsgDispatcher offline;
	
	private volatile Queue<Msg> msgQueue=new LinkedList<Msg>();
	
	
	public MsgDispatcher(HashMap<String,IoSession> map){
		
		this.map=map;
	
		
	}
	
	public void setOfflineMessenger(OfflineMsgDispatcher dis){
		this.offline=dis;
	}
	
	
	private void handleMsg(Msg message) throws Exception{
		if(message==null){
			return;
		}
		System.out.println("handling one msg");
		String msg=message.msgbody;
		IoSession session=message.iosession;
		String[] msgs=msg.split(";");
		switch (msgs[0]) {
		case "login":
			Login(msgs[1],session);
			break;
		case "talk":
			sendMsg(message);
			break;
		}
		
	}
	
	
	private void Login(String email, IoSession session) throws Exception{
		map.put(email, session);
		session.setAttribute("email", email);
		//retrieve offline msg
		this.offline.dispatchOfflineMSg(session);
		
	}
	
	
	private void sendMsg(Msg message) throws Exception{
		if(message.iosession.getAttribute("email")==null||message.iosession.getAttribute("email").equals("")){
			message.iosession.write("please login first");
			return;
		}
		String msg=message.msgbody;
		IoSession sender_session=message.iosession;
		String[] msgs=msg.split(";");
		String sender=(String)sender_session.getAttribute("email");
		String receiver=msgs[1];
		String text=msgs[2];
		IoSession receiver_session=map.get(receiver);
		if(receiver_session!=null){
			receiver_session.write(text);
		}
		else {
			//offline msg handling
			sender_session.write("user is not online,writing msg to db ");
			this.offline.sendOffileMsg(message, receiver);
		}
		
	}
	
	
	
	
	
	public void dispatchMsg(String msg,IoSession session){
		
		Msg message=new Msg(msg, session);
		this.msgQueue.add(message);
		System.out.println("queue hash in dispatchMsg is "+this.msgQueue.hashCode());
		

		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		System.out.println("queue hash in run is "+this.msgQueue.hashCode());
		while(true){
			
			//System.out.println("queue is empyt "+msgQueue.isEmpty());
			
			while(!msgQueue.isEmpty()){
				synchronized (this) {
				    //System.out.println("dispatcher running");
					try {
						handleMsg(msgQueue.poll());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		}
	}

}
