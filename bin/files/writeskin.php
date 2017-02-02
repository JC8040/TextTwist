<?php


	// Get the new time
	$skin = $_POST['skin'];
	$skinfile = "skin.txt";
	// Save the new time to file
	file_put_contents($skinfile, $skin);
	
	
?>
