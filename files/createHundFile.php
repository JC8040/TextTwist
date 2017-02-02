<?php
/*
@File: createHundFile.php
@Purpose: When user plays 100 word game, creates a file so their stats can be stored 
@Author: Christina
*/
$uid = $_POST['uid'];
$dir = getcwd() . "/100WordStats/";

$source = $dir . $uid . ".txt";


	if (!file_exists($source)) {
	$ourFileHandle = fopen($source, 'w') or die("can't open file");
	fclose($ourFileHandle);
	
	}



?>