import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class OfflineMsgDispatcher {
	
	private static OfflineMsgDispatcher instance=new OfflineMsgDispatcher();
	private OfflineMsgDispatcher(){
		
	}
	public static OfflineMsgDispatcher getInstance(){
		return instance;
	}
	
	public static void main(String[] args){
		try {
			MongoClient client=new MongoClient("ds041643.mongolab.com", 41643);
			DB mongodb=client.getDB("nusnosql");
			//if()
			char[] password={'1','2','3'};
			boolean auth=mongodb.authenticate("root", password);
			if(auth){
				System.out.println("auth for mongo success");
				DBCollection collection=mongodb.getCollection("msg");
				BasicDBObject query=new BasicDBObject("userid","richard_johnson@sina.com");
				query.put("rec",false);
				BasicDBObject criteria=new BasicDBObject();
				//criteria.put("fromuserid",1);
				//criteria.put("text", 1);
				DBCursor cursor=collection.find(query,criteria);
				while(cursor.hasNext()){
					System.out.println(cursor.next());
				}
			}
			else {
				System.out.println("auth for mongo failed");

			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
