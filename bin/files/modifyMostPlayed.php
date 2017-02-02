<html>
<head>
<title>Modifying 100word Stats</title>
</head>
<body>
<?php

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