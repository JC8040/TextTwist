<?php

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
