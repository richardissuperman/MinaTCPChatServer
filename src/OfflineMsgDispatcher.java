import java.net.UnknownHostException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.mina.core.session.IoSession;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class OfflineMsgDispatcher {
	
	private static OfflineMsgDispatcher instance=new OfflineMsgDispatcher();
	//private MongoConnectionPool pool;
	private GenericObjectPool<DB> pool;
	private OfflineMsgDispatcher(){
		pool=new GenericObjectPool<DB>(new MongoConnectionPool());
	}
	public static OfflineMsgDispatcher getInstance(){
		return instance;
	}
	
	
	public void dispatchOfflineMSg(IoSession session) throws Exception{
		String email=(String)session.getAttribute("email");
		DB db=this.pool.borrowObject();
		DBCollection collection=db.getCollection("msg");
		//query for offline msg is 
		//1.rec is false
		//2.userid is email
		
		BasicDBObject query=new BasicDBObject();
		query.put("rec", false);
		query.put("userid", email);
		
		
		DBCursor cursor=collection.find(query);
		//if no offline msg, return directly
		if(!cursor.hasNext()){
			return;
		}
		
		while(cursor.hasNext()){
			DBObject obj=cursor.next();
			String from=(String)obj.get("fromuserid");
			String text=(String)obj.get("text");
			session.write("user "+from+" said: "+text);
		}
		
		//after dispatch offline msg, turn them to rec
		//update criteria
		//1.rec is false
	    //2.userid is email
		
		collection.update(query,new BasicDBObject("rec",true));
		pool.returnObject(db);
	}
	
	
	public void sendOffileMsg(Msg message, String receiver) throws Exception{
		//String sender=(String)senderSession.getAttribute("email");
		String sender=(String)message.iosession.getAttribute("email");
		String textbody=message.msgbody;
		DB mongoDb=this.pool.borrowObject();
		DBCollection collection=mongoDb.getCollection("msg");
		
		//update the msg collection with the new offline msg user send
		BasicDBObject object=new BasicDBObject();
		object.put("userid", receiver);
		object.put("fromuserid", sender);
		object.put("text", textbody);
		//insert into collection "msg"
		collection.insert(object);
		
		
		pool.returnObject(mongoDb);
		
	}
	
//	public static void main(String[] args){
//		try {
//			MongoClient client=new MongoClient("ds041643.mongolab.com", 41643);
//			DB mongodb=client.getDB("nusnosql");
//			//if()
//			char[] password={'1','2','3'};
//			boolean auth=mongodb.authenticate("root", password);
//			if(auth){
//				System.out.println("auth for mongo success");
//				DBCollection collection=mongodb.getCollection("msg");
//				BasicDBObject query=new BasicDBObject("userid","richard_johnson@sina.com");
//				query.put("rec",false);
//				BasicDBObject criteria=new BasicDBObject();
//				//criteria.put("fromuserid",1);
//				//criteria.put("text", 1);
//				DBCursor cursor=collection.find(query,criteria);
//				while(cursor.hasNext()){
//					System.out.println(cursor.next());
//				}
//			}
//			else {
//				System.out.println("auth for mongo failed");
//
//			}
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	

}
