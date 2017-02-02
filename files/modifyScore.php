</body>
</html>
modifyscore:
<html>
<head>
<title>Modifying Score</title>
</head>
<body>
<?php
/*
@File: modifyScore.php
@Purpose: For preset game, modify the score (each time the player finds a new word)
@Author: Mark
*/
$uid = $_POST['uid'];
$puzzle = $_POST['puzzle'];
$score = $_POST['score'];

$dir = getcwd() . "/players/";

$source = $dir . $uid . ".txt";

$filecontent = "";
// copy operation
$sh=fopen($source, 'r');

while (!feof($sh)) {
$line=fgets($sh);
if (strpos($line, $puzzle)!==false) {
$count=0;
$newline = $puzzle . "||" . $score . "||";
$index = strlen($line);
// find last occurence of '|'
while ($line[$index] != "|") {
$index--;
}

$index++;
while ($index <= strlen($line)) {
$newline = $newline . $line[$index];
$index++;
}

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