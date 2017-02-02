<html>
<head>
<title>Appending to Player</title>
</head>
<body>
<?php
try {

	$uid = $_POST['uid'];

	$puzzle = $_POST['puzzle'];
	
	$score = $_POST['score'];

	$time = $_POST['time'];
	
	$newfilename = "players/" . $uid . ".txt";

	if (!file_exists($newfilename)) {
		$out = shell_exec("touch $newfilename");
		//file_put_contents($newfilename, "");
	}
	echo "before fopen";
	echo "$out";
	$file = fopen ($newfilename, 'rb');
	
	
	$file = fopen ($newfilename, 'rb');
	if ($file) {
		while(!feof($file)) {
      			$contents .= fread($file, 8192);
    		}
	}

	$append = $puzzle . "||" . $score . "||" . $time . PHP_EOL;
	file_put_contents($newfilename, $contents . $append);
	
	echo "before catch";
	} catch (Exception $e) {
		echo ($e);
	}
?>
</body>
</html>