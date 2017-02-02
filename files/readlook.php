<?php
/*
@File: readlook.php
@Purpose: Get the look saved
@Author: Jackie
//Not used anymore
*/
	$myFile = "lookAndFeel.txt";
	$fh = fopen($myFile, 'r');
	$theData = fgets($fh);
	fclose($fh);
	echo $theData;	
?>
