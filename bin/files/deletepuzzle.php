<?php

/*
	$deletepuzzle = $_POST['deletepuzzle'];
	$puzzlefile = "puzzle.txt";
	*/

	//http://stackoverflow.com/questions/5712878/how-to-delete-a-line-from-the-file-with-php
$dir = "puzzle.txt";
$line = $_POST['deletepuzzle'];
$contents = file_get_contents($dir);
$contents = str_replace($line.PHP_EOL, '', $contents);
file_put_contents($dir, $contents);


	
	
	/*
	//http://www.codingforums.com/archive/index.php/t-180503.html
	
function removeLine ($url, $lineToRemove)
{
$data = file_get_contents($url);
$lines = explode(PHP_EOL, $data);
$lineNo = 1;
foreach($lines as $line)
{
$linesArray[$lineNo] = $line;
$lineNo++;
}
unset($linesArray[$lineToRemove]);
return implode("\n", $linesArray);
}

$data = removeLine ($puzzlefile, $deletepuzzle);

echo $data
	
	
	
	
	*/
	
	
	
	
	/*// delete puzzle from file
	$contents = file_get_contents($puzzlefile);
	$contents = str_replace($deletepuzzle , '', $contents);
	file_put_contents($dir, $contents);
*/

/*
// Working, but for line num
// The file
$filePath = "puzzle.txt";

// Grab file into an array, by lines
$fileArr = file( $filePath );

// Remove desired line
unset( $fileArr[$deletepuzzle] ); 

// Put back with PHP4
$success = FALSE;
if ( ( $fp = fopen( $filePath, 'w' ) ) !== FALSE )
{
    if ( flock( $fp, 2 ) ) // LOCK_EX
    {
        fwrite( $fp, implode( '', $fileArr ) );
        flock( $fp, 3 ); // LOCK_UN
        $success = TRUE;
    }
    fclose( $fp );
}
*/
/*
function deleteLineInFile($file,$string)
{
$i=0;$array=array();

$read = fopen($file, "r") or die("can't open the file");
while(!feof($read)) {
echo $array[$i];
$array[$i] = fgets($read);	
++$i;
}
fclose($read);

$write = fopen($file, "w") or die("can't open the file");
foreach($array as $a) {
if(!strstr($a,$string)) fwrite($write,$a);
}
fclose($write);
}

$filePath = "puzzle.txt";
deleteLineInFile("puzzle.txt",$deletepuzzle);
*/
?>
