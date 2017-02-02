<?php
$uid = $_POST['uid'];
$dir = getcwd() . "/100WordStats/";

$source = $dir . $uid . ".txt";


	if (!file_exists($source)) {
	$ourFileHandle = fopen($source, 'w') or die("can't open file");
	fclose($ourFileHandle);
	
	}



?>