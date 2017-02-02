<?php
	$myFile = "lookAndFeel.txt";
	$fh = fopen($myFile, 'r');
	$theData = fgets($fh);
	fclose($fh);
	echo $theData;	
?>
