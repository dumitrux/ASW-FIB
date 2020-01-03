package wallOfTweets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WoTServlet
 */
@WebServlet("/")
public class WoTServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Locale currentLocale = new Locale("en");
	String ENCODING = "ISO-8859-1";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WoTServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Vector<Tweet> tweets = Database.getTweets();
			if (request.getHeader("Accept").equals("text/plain")) 
				printPLAINresult (tweets, request, response);
			else 
				printHTMLresult(tweets, request, response);

		}

		catch (SQLException ex ) {
			throw new ServletException(ex);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String author = request.getParameter("author");
		String tweet_text = request.getParameter("tweet_text");
		
		Long idt = null;
		
		String id = request.getParameter("id");
		Cookie[] cookies = request.getCookies();
		if (id != null) {
			if (cookies.length != 0) {
				for (Cookie c : cookies) {
					if (c.getValue().equals(convertirMD5(id))) 
						Database.deleteTweet(Long.parseLong(id));
				}
			}
			
		} else {
			try {
				idt = Database.insertTweet(author, tweet_text);
				response.addCookie(new Cookie("id" + idt.toString(), convertirMD5(idt.toString())));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (! request.getHeader("Accept").equals("text/plain")) 
			response.sendRedirect(request.getContextPath());
		else {
			PrintWriter  out = response.getWriter ( );
			out.print(String.valueOf(idt));
		}
	}
	

	private void printHTMLresult (Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, currentLocale);
		DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.DEFAULT, currentLocale);
		res.setContentType ("text/html");
		res.setCharacterEncoding(ENCODING);
		PrintWriter  out = res.getWriter ( );
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head><title>Wall of Tweets</title>");
		out.println("<link href=\"wallstyle.css\" rel=\"stylesheet\" type=\"text/css\" />");
		out.println("</head>");
		out.println("<body class=\"wallbody\">");
		out.println("<h1>Wall of Tweets</h1>");
		out.println("<div class=\"walltweet\">"); 
		out.println("<form method=\"post\">");
		out.println("<table border=0 cellpadding=2>");
		out.println("<tr><td>Your name:</td><td><input name=\"author\" type=\"text\" size=70></td><td></td></tr>");
		out.println("<tr><td>Your tweet:</td><td><textarea name=\"tweet_text\" rows=\"2\" cols=\"70\" wrap></textarea></td>"); 
		out.println("<td><input type=\"submit\" name=\"action\" value=\"Tweet!\"></td></tr>"); 
		out.println("</table></form></div>");
		String currentDate = "None";
		for (Tweet tweet: tweets) {
			String messDate = dateFormatter.format(tweet.getDate());
			if (!currentDate.equals(messDate)) {
				out.println("<br><h3>...... " + messDate + "</h3>");
				currentDate = messDate;
			}
			out.println("<div class=\"wallitem\">");
			out.println("<h4><em>" + tweet.getAuthor() + "</em> @ "+ timeFormatter.format(tweet.getDate()) +"</h4>");
			out.println("<p>" + tweet.getText() + "</p>");
			
			Cookie[] cookies = req.getCookies();
			
			if (cookies.length != 0) {
				for (Cookie c : cookies) {
					if (c.getValue().equals(convertirMD5(Long.toString(tweet.getTwid())))) {
						out.println("<form method=\"post\">");
						out.println("<table border=0 cellpadding=2>");
						out.println("<input type=\"submit\" name=\"action\" value=\"delete\" style = \"color: black\">");
						out.println("<tr><td><input type=\"hidden\" name=\"id\" value="+tweet.getTwid()+"></td></tr>");
						out.println("</table></form>");
					}
			}
				
			out.println("</div>");
			}
		}
		out.println ( "</body></html>" );
	}
	
	private void printPLAINresult (Vector<Tweet> tweets, HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		PrintWriter  out = res.getWriter ( );
		out.println(tweets.firstElement().getTwid());
		for (Tweet tweet: tweets) {
			out.println("tweet #" + tweet.getTwid() + ": " + tweet.getAuthor() + ": " +
					tweet.getText() + " [" + tweet.getDate() + ']');
			
		}
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
