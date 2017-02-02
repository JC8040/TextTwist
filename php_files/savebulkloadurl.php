<?php
/*
@File: savebulkloadurl.php
@Purpose: Save the bulk load puzzles from the url passed in to the server
@Author: Christina
*/

	$bulkloadURL = $_POST['bulkloadURL'];
	$puzzlefile = "puzzlebulk.txt";
	// Save puzzle to file
	$file = fopen($bulkloadURL, "r");
	$puzzleArray = array();
	
	while (!feof($file)) {
		$puzzleArray[] = fgets($file);
	}
	fclose($file);
	
	
	$fh = fopen($puzzlefile, 'w+') or die("can't open file");
	foreach($puzzleArray as $key => $value){
	$value=strtolower($value);
	fwrite($fh,$value);
	}
	fclose($fh);
	
?>
