<?php
ini_set("soap.wsdl_cache_enabled","0");
header('Content-Type: application/json');

try{
	
	//echo $_GET['content'];
	$sClient = new SoapClient('http://api.chartlyrics.com/apiv1.asmx?WSDL');
	
	// Get the necessary parameters from the request
	$request = new stdClass();
	$request->lyricText = $_GET['content'];
	//$request = $_GET['content'];
	
	// Use $sClient to call the operation SearchLyricText
	$response = $sClient->SearchLyricText($request);

	$cancons = $response->SearchLyricTextResult;
	
	// echo the returned info as a JSON array of objects
	$canconsJSON = json_encode($cancons);
	
	echo $canconsJSON;
	


	//header(':', true, 501); // Just remove this line to return the successful 
						  // HTTP-response status code 200.
	//echo '["Not","Yet","Implemented"]';

}
catch(SoapFault $e){
	header(':', true, 500);
	echo json_encode($e);
}
?>
