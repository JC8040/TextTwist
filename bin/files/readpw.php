<?php
	$myFile = "password.txt";
	$fh = fopen($myFile, 'r');
	$theData = fgets($fh);
	fclose($fh);
	echo $theData;	
?>
