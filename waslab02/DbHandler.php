<?php

class DbHandler extends SQLite3 {

	private $db;

	function __construct()
	{
		$this->open('wotdb.db');
		$res = $this->querySingle("SELECT name FROM sqlite_master WHERE type='table' AND name='tweets';");
		if (!isset($res)) {
			$create = "create table tweets (".
						"twID INTEGER PRIMARY KEY,".
						"twAUTHOR varchar (120),".
						"twTEXT varchar (2400),".
						"twLIKES INTEGER DEFAULT 0,".
						"twTIME timestamp DEFAULT CURRENT_TIMESTAMP)";
		    $this->exec($create);
		    $this->exec("insert into tweets (twAUTHOR, twTEXT, twLIKES, twTIME) values('Sherlock','A cynic is a man who, when he smells flowers, looks around for a coffin',5,'2018-09-19 14:56:11')");
			$this->exec("insert into tweets (twAUTHOR, twTEXT, twLIKES, twTIME) values('Mycroft','No married man is genuinely happy if he has to drink worse whisky than he used to drink when he was single',5,'2018-09-20 17:23:45')");
			$this->exec("insert into tweets (twAUTHOR, twTEXT, twLIKES, twTIME) values('Sherlock','Before a man speaks it is always safe to assume that he is a fool. After he speaks, it is seldom necessary to assume it',6,'2018-09-20 19:07:11')");
			$this->exec("insert into tweets (twAUTHOR, twTEXT, twLIKES, twTIME) values('Mycroft','Adultery is the application of democracy to love',6,'2018-09-21 10:46:31')");
			$this->exec("insert into tweets (twAUTHOR, twTEXT, twLIKES, twTIME) values('Mycroft','A judge is a law student who marks his own examination papers',22,'2018-09-22 10:06:11')");
			$this->exec("insert into tweets (twAUTHOR, twTEXT, twLIKES, twTIME) values('Sherlock','For every complex problem there is an answer that is clear, simple, and wrong',11,'2018-09-22 11:16:01')");
        
		} 
	}

	function getTweets() // Returns an array of tweets. 
	                     //In turn, each tweet is represented as an array too.
	{
		$tweets = array();
		$query = "select * from tweets order by twTIME desc";
		$result = $this->query($query);
		while ($row = $result->fetchArray(SQLITE3_ASSOC))
		{
			$tweet = array ('id' => $row['twID'], 
			                'author' => $row['twAUTHOR'],
							'text' => $row['twTEXT'],
							'likes' => $row['twLIKES'],
							'time' => strtotime($row['twTIME']));
			array_push($tweets, $tweet);
		}
		return $tweets;
	}
	
	function likeTweet($tweetId) // Increments the number of "likes" of the tweet with twID = $tweetId
	                             // and returns the new value.
	{
		$update = "update tweets set twLIKES = twLIKES + 1 where twID = ".$tweetId;
		$query = "select twLIKES from tweets where twID = ".$tweetId;
		$this->exec($update);
		$result = $this->query($query);
		$row = $result->fetchArray(SQLITE3_ASSOC);
		$likes = $row['twLIKES'];
		return $likes;
	}
	
	function insertTweet($author, $text)  // Inserts a new tweet and returns its ID
	{
		$newId = NULL;
		if (isset($text) && $text != "") {
		    if (!isset($author) || $author == "") $author = "Anonymous";
			$insert = "insert into tweets(twAUTHOR, twTEXT) values('{$author}','{$text}')";
			$this->exec($insert);
			$newId = $this->lastInsertRowID();
		}
		return $newId;
	}
	
	function deleteTweet($tweetId) // Tries to delete the tweet with twID = $tweetId 
	                               // If successul, returns 1; 0 otherwise.
	{
		$this->exec("delete from tweets where twID = ".$tweetId);
		return $this->changes();
	}
}

?>
