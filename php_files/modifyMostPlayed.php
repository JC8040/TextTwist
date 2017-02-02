<html>
<head>
<title>Modifying 100word Stats</title>
</head>
<body>
<?php
/*
@File: modifyMostPlayed.php
@Purpose: For the 100 word game, modify who now has played it the most
@Author: Mark
*/
$name= $_POST['name'];
$frequency  = $_POST['frequency'];

$dir = getcwd() . "/100WordStats/";

$source = $dir . "mostplayed.txt";

$filecontent = $frequency . "||" . $name;


// overwrite file
$file=fopen($source, 'w');


fwrite($file, $filecontent);

fclose($file);


?>
</body>
</html>