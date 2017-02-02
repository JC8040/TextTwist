<?php

	// Directory used to write the winners' names
	//define("DIRECTORY", "winnerLists")
	
	// Get the new password
	$newpw = $_POST['newpw'];

	// Save the password to file
	file_put_contents("password.txt", $newpw);

	
	
?>
