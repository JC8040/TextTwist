<?php


	$newpuzzle = $_POST['newpuzzle'].PHP_EOL;
	$puzzlefile = "puzzle.txt";
	// Save puzzle to file
$fh = fopen($puzzlefile, 'a') or die("can't open file");
fwrite($fh, $newpuzzle);
fclose($fh);
?>
