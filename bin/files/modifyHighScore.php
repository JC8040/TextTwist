<html>
<head>
<title>Modifying 100word Stats</title>
</head>
<body>
<?php

$name= $_POST['name'];
$score  = $_POST['score'];

$dir = getcwd() . "/100WordStats/";

$source = $dir . "highscore.txt";

$filecontent = $score . "||" . $name;


// overwrite file
$file=fopen($source, 'w');


fwrite($file, $filecontent);

fclose($file);


?>
</body>
</html>