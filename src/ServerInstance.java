import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;




public class ServerInstance {
	
	public static void main(String[] args) throws IOException{
		
		
		//1.retieve list
		
		
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
		  .url("http://192.168.1.4:8080/RestCrud/api/user/list")
		  .get()
		  .addHeader("content-type", "application/json")
		  .addHeader("cache-control", "no-store")
		  .addHeader("username", "richard_johnson@sina.com")
		  .addHeader("hashcode", "adsad")
		  .build();

		Response response = null;

		try {
			response = client.newCall(request).execute();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String jsonString=response.body().string();
		
		JSONArray array=null;
		
		try {
			array=new JSONArray(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String,IoSession> map=new HashMap<String, IoSession>();
		
		for(int i=0;i<array.length();i++){
			JSONObject obj;
			try {
				obj = array.getJSONObject(i);
				String email=obj.getString("email");
				System.out.println(email);
				map.put(email, null);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		
		
		
		//2.open TCP server
				InetSocketAddress address=new InetSocketAddress(8085);
				IoAcceptor ioAcceptor=new NioSocketAcceptor();
				MinaNetworkHandler handler=new MinaNetworkHandler();
				handler.setDispatcher(new MsgDispatcher(map));
				ioAcceptor.setHandler(handler);
		        ioAcceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
				try{
					ioAcceptor.bind(address);
				}		
				catch(Exception e){
					e.printStackTrace();
				}
		
		
		
		
		
	}

}
