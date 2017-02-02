<?php

	// Directory used to write the winners' names
	//define("DIRECTORY", "winnerLists")
	
	// Get the new password
	$newlook = $_POST['newlook'];

	// Save the password to file
	file_put_contents("lookAndFeel.txt", $newlook);

	
	
?>
