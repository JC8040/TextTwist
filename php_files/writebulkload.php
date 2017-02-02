<?php
/*
@File: writebulkload.php
@Purpose: Save the correct bulk load puzzles to the puzzle.txt file
@Author: Christina
*/

	$bulkload = $_POST['bulkload'];
	$puzzlefile = "puzzle.txt";
	// Save puzzle to file
	$file = fopen($bulkload, "r");
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
