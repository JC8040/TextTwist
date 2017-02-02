<?php

/*
@File: writesecurityQA.php
@Purpose: Save the security question and answer
@Author: Christina
*/

	$question = $_POST['question'].PHP_EOL;
	$answer = $_POST['answer'].PHP_EOL;
	
	$securityFile = "securityQA.txt";
	// Save to file
$fh = fopen($securityFile, 'w') or die("can't open file");
fwrite($fh, $question.$answer);
fclose($fh);
?>
