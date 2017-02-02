<?php

/*
@File: writeskin.php
@Purpose: Save the skin
@Author: Christina
*/

	// Get the skin
	$skin = $_POST['skin'];
	$skinfile = "skin.txt";
	// Save to file
	file_put_contents($skinfile, $skin);
	
	
?>
