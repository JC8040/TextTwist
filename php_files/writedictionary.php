<?php
/*
@File: writedictionary.php
@Purpose: Save the correct dictionary to the dictionary.txt file
@Author: Christina
*/
	
	$dictionary = $_POST['dictionary'];
	$dictionaryfile = "dictionary.txt";
	// Save to file
	$file = fopen($dictionary, "r");
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
