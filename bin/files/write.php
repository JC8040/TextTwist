<?php

	// Directory used to write the winners' names
	//define("DIRECTORY", "winnerLists")
	
	// Get the new password
	$newpw = $_POST['newpw'];
	$pwfile = "password.txt";
	// Save the password to file
	file_put_contents($pwfile, $newpw);
	
	
	$newpuzzle = $_POST['newpuzzle'].PHP_EOL;
	$puzzlefile = "puzzle.txt";
	// Save puzzle to file
$fh = fopen($puzzlefile, 'a') or die("can't open file");
fwrite($fh, $newpuzzle);
fclose($fh);
?>
