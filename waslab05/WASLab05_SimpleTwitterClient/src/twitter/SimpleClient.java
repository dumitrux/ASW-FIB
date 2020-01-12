
package twitter;

import java.util.Date;


import twitter4j.*;

public class SimpleClient {

	public static void main(String[] args) throws Exception {
		
		final Twitter twitter = new TwitterFactory().getInstance();
		/*
		Date now = new Date();
		String latestStatus = "Hey @fib_was, we've just completed task #4 [timestamp: "+now+"]";
		Status status = twitter.updateStatus(latestStatus);
		System.out.println("Successfully updated the status to: " + status.getText());      */ 
		
		
		ResponseList<User> list = twitter.lookupUsers("fib_was");
		System.out.println(list.get(0).getStatus().getText());
		//System.out.println(list.get(0).getStatus().getId());
		Status status = twitter.retweetStatus(list.get(0).getStatus().getId());
		System.out.println("Successfully updated the status to: " + status.getText());
	}
}
