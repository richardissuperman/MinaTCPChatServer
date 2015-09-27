import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoConnectionPool extends BasePooledObjectFactory<DB>{
	
	private  String host="ds041643.mongolab.com";
	private char[] password={'1','2','3'};
	private String dbName="nusnosql";
	private String username="root";
	private int port=41643;

	@Override
	public DB create() throws Exception {
		// TODO Auto-generated method stub
		MongoClient  client=new MongoClient(host, port);
		
		DB mongoDb=client.getDB(dbName);
		
		boolean auth=mongoDb.authenticate(username, password);
		if(auth){
			return mongoDb;
		}
		else {
			return null;
		}
	}

	@Override
	public PooledObject<DB> wrap(DB arg0) {
		// TODO Auto-generated method stub
		 return new DefaultPooledObject<DB>(arg0);
	}
	
	
	@Override
	public void passivateObject(PooledObject<DB> p) throws Exception {
		// TODO Auto-generated method stub
		//super.passivateObject(p);
		DB db=p.getObject();
		db.cleanCursors(true);
	}
	
	

}
