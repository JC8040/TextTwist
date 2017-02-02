<?php
/*
@File: readpw.php
@Purpose: Get the password saved
@Author: Jackie
// Not used anymore
*/
	$myFile = "password.txt";
	$fh = fopen($myFile, 'r');
	$theData = fgets($fh);
	fclose($fh);
	echo $theData;	
?>
