<?php

/*
@File: writetime.php
@Purpose: Save the new time for timer
@Author: Christina
*/

	// Get the new time
	$newtime = $_POST['newtime'];
	$timefile = "time.txt";
	// Save the new time to file
	file_put_contents($timefile, $newtime);
	
	
?>
