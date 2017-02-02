<?php


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
	//copy ( $bulkload , $puzzlefile );
	/*
$fh = fopen($puzzlefile, 'a') or die("can't open file");
fwrite($fh, $newpuzzle);
fclose($fh);
*/
?>
