<html>
<head>
<title>Modifying 100word Stats</title>
</head>
<body>
<?php

$uid = $_POST['uid'];
$frequency = $_POST['frequency'];
$score  = $_POST['score'];

$dir = getcwd() . "/100WordStats/";

$source = $dir . $uid . ".txt";

$filecontent = $frequency . "||" . $score;


// overwrite file
$file=fopen($source, 'w');


fwrite($file, $filecontent);

fclose($file);


?>
</body>
</html>