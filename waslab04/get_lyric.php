<?php
ini_set("soap.wsdl_cache_enabled","0");
header('Content-Type: application/json');

try{

  //echo $_GET['LyricId'] . ' ' . $_GET['LyricCheckSum'];

  $sClient = new SoapClient('http://api.chartlyrics.com/apiv1.asmx?WSDL');

  // Get the necessary parameters from the request
  $lyricId = $_GET['LyricId'];
  $lyricCheckSum = $_GET['LyricCheckSum'];
  //echo $lyricId. ' ' . $lyricCheckSum;
  
  $request = new stdClass();
  $request->lyricId = (int)$lyricId;
  $request->lyricCheckSum = $lyricCheckSum;
  
  
  // Use $sClient to call the operation GetLyric
  $response = $sClient->GetLyric($request);
  
  
  $lletra = $response->GetLyricResult;
  //var_dump($lletra);
  // echo the returned info as a JSON object
  $lletraJSON = json_encode($lletra);
  echo $lletraJSON;
  
  

  //header(':', true, 501); // Just remove this line to return the successful 
                          // HTTP-response status code 200.
  //echo json_encode(array('Result' => 'Not implemented'));

}
catch(SoapFault $e){
  header(':', true, 500);
  echo json_encode($e);
}

