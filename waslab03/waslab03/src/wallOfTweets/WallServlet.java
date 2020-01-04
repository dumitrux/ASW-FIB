package wallOfTweets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;



@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/tweets", "/tweets/*"})
public class WallServlet extends HttpServlet {

	private String TWEETS_URI = "/waslab03/tweets/";

	@Override
	// Implements GET http://localhost:8080/waslab03/tweets
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.setHeader("Cache-control", "no-cache");
		List<Tweet> tweets= Database.getTweets();
		JSONArray job = new JSONArray();
		for (Tweet t: tweets) {
			JSONObject jt = new JSONObject(t);
			jt.remove("class");
			job.put(jt);
		}
		resp.getWriter().println(job.toString());
	}

	@Override
	// Implements POST http://localhost:8080/waslab03/tweets/:id/likes
	//        and POST http://localhost:8080/waslab03/tweets
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String uri = req.getRequestURI();
		int lastIndex = uri.lastIndexOf("/likes");
		long id = 0;
		if (lastIndex > -1) {  // uri ends with "/likes"
			// Implements POST http://localhost:8080/waslab03/tweets/:id/likes
			 id = Long.valueOf(uri.substring(TWEETS_URI.length(),lastIndex));		
			resp.setContentType("text/plain");
			resp.getWriter().println(Database.likeTweet(id));
		}
		else { 
			// Implements POST http://localhost:8080/waslab03/tweets
			int max_length_of_data = req.getContentLength();
			byte[] httpInData = new byte[max_length_of_data];
			ServletInputStream  httpIn  = req.getInputStream();
			httpIn.readLine(httpInData, 0, max_length_of_data);
			String body = new String(httpInData);
			/*      ^
		      The String variable body contains the sent (JSON) Data. 
		      Complete the implementation below.*/
			JSONObject nj = null;
			try {
				 nj = new JSONObject(body);
			
				
			Tweet t = null;
			
			
				t = Database.insertTweet(nj.getString("author"), nj.getString("text"));
			
			JSONObject j;
			j = new JSONObject(t);
			
				j.put("token", convertirMD5(t.getId().toString()));
				resp.getWriter().println(j.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			
		}
	
	
	@Override
	// Implements DELETE http://localhost:8080/waslab03/tweets/:id
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String uri =req.getRequestURI();
		long id = Long.valueOf(uri.substring(TWEETS_URI.length()));
		
		String token = req.getQueryString();
		//System.out.println(token);
		token = token.substring(6, token.length());
		
		String tok_id = convertirMD5(String.valueOf(id));
		
		boolean deleted = false;
		//System.out.println(tok_id);
		//System.out.println(token);

		if (!token.isEmpty() && token.equals(tok_id)) {
			deleted = Database.deleteTweet(id);
			//System.out.println("db");

		}
		
		if (!deleted || uri.isEmpty()) throw new ServletException("DELETE not yet implemented");
	}
	
	private String convertirMD5(String contra) {
		MessageDigest mdigest = null;
		try {
			mdigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] hash = mdigest.digest(contra.getBytes());
		StringBuffer s = new StringBuffer();
		
		for (byte b: hash) {
			s.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));		
		}
		return s.toString();
	}

}
