<?php
require_once("DbHandler.php");

setlocale(LC_TIME,"en_US");

$dbhandler = new DbHandler();

$rss = new simpleXMLElement("<rss></rss>");
$rss->addAttribute('version', '2.0');


$ch = $rss->addChild('channel');
$ch->title = "Wall of Tweets 2 - RSS Version";
$ch->link = "wall.php";
$ch->description = "RSS 2.0 feed that retrieves the tweets posted to the web app \"Wall of Tweets 2\"";

$tws = $dbhandler->getTweets();
foreach ($tws as $tw) {
	$item = $rss->channel->addChild('item');
	$item->title = $tw['text'];
	$item->link = "http://localhost:8080/waslab02/wall.php#" . $tw['id'];
	$item->description = "This is WoT tweet #" . $tw['id']  . " posted by " . $tw['author'] . ". It has been liked by "
	. $tw['likes'] . " people";
	$item->pubDate = date(DATE_W3C, $tw['time']);

	
}

header('Content-type: text/xml');
echo $rss->asXML();




	
?>
