<?php
/*
@File: writepq.php
@Purpose: Save the password
@Author: J
*/

	// Get the new password
	$newpw = $_POST['newpw'];

	// Save the password to file
	file_put_contents("password.txt", $newpw);

	
	
?>
