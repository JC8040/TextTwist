<html>
<head>
<title>Modifying Score</title>
</head>
<body>
<?php
/*
@File: modifyTime.php
@Purpose: For preset game, modify the time when the player has found the 6-letter word
@Author: Mark
*/
$uid = $_POST['uid'];
$puzzle = $_POST['puzzle'];
$time = $_POST['time'];

$dir = getcwd() . "/players/";

$source = $dir . $uid . ".txt";

$filecontent = "";
// copy operation
$sh=fopen($source, 'r');

while (!feof($sh)) {
$line=fgets($sh);
if (strpos($line, $puzzle)!==false) {
$index = strlen($line)-1;
while ($line[$index] != '|') {
$index--;
}

$index ++;
$count=0;
$newline = "";
while ($count < $index) {
$newline = $newline . $line[$count];
$count++;
}
$newline = $newline . $time;

$line = $newline;
}
if (!empty($line)) {
$filecontent = $filecontent . $line;
}
}

fclose($sh);

$sh=fopen($source, 'w');

fwrite($sh, $filecontent);

fclose($sh);


?>
</body>
</html>