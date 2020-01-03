<?php

$URI = 'http://localhost:8080/waslab02/wall.php';
$resp = file_get_contents($URI);
echo $http_response_header[0], "\n"; // Print the first HTTP response header

$xmlstr = <<<XML
$resp
XML;
$alltweets = new SimpleXMLElement($xmlstr);



foreach ($alltweets->tweet as $tweet) {
	echo "[tweet #" . $tweet["id"] . "] " . $tweet->author . 
	": " .  $tweet->text . " [" . $tweet->time . "]\n";
}


$posdata = new SimpleXMLElement("<tweet></tweet>");
$posdata->author = "Test";
$posdata->tweet_text = "Text Test";

$opts = array('http' =>
    array(
        'method'  => 'PUT',
        'header'  => 'Content-type: text/xml',
        'content' => $posdata->asXML()
    )
);

$context = stream_context_create($opts);

$result = file_get_contents($URI, false, $context);

echo $result;

$opts = array('http' =>
    array(
        'method'  => 'DELETE',
        'header'  => 'Content-type: text/xml'
    )
);

$context = stream_context_create($opts);
$result_del = file_get_contents('http://localhost:8080/waslab02/wall.php?twid=16', false, $context);
echo $result_del;


?>
