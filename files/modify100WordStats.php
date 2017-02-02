<html>
<head>
<title>Modifying 100word Stats</title>
</head>
<body>
<?php
/*
@File: modify100WordStats.php
@Purpose: Modify the score and frequency for the given player, for their 100 words game
@Author: Mark
*/
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