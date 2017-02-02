<?php
/*

	$dictionary = $_POST['dictionary'];
	$dictionaryfile = "dictionary.txt";

	file_put_contents($dictionaryfile, $dictionary);
	
	*/
	
	$dictionary = $_POST['dictionary'];
	$dictionaryfile = "dictionary.txt";
	// Save puzzle to file
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
