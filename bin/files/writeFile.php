<?php

	// Get the new password
	$content = $_POST['content'];
	$file = $_POST['file'];
	// Save the password to file
	file_put_contents($file, $content);

?>
