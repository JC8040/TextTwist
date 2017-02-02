<?php
/*
@File: savedictionaryurl.php
@Purpose: Save the dictionary from the url passed in to the server
@Author: Christina
*/
	$dictionaryURL = $_POST['dictionaryURL'];
	$dictionaryfile = "dictionaryFile.txt";
	// Save dictionary to file
	$file = fopen($dictionaryURL, "r");
	$dictionaryArray = array();
	
	while (!feof($file)) {
		$dictionaryArray[] = fgets($file);
	}
	fclose($file);
		
	$fh = fopen($dictionaryfile, 'w+') or die("can't open file");
	foreach($dictionaryArray as $key => $value){
	$value=strtolower($value);
	fwrite($fh,$value);
	}
	fclose($fh);

?>
