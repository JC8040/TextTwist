<?php
/*
@File: deletepuzzle.php
@Purpose: Gets the puzzle to be deleted, find it in the list of puzzles and delete it
@Author: Christina
*/
// Based on http://stackoverflow.com/questions/5712878/how-to-delete-a-line-from-the-file-with-php
$dir = "puzzle.txt";
$line = $_POST['deletepuzzle'];
$contents = file_get_contents($dir);
$contents = str_replace($line.PHP_EOL, '', $contents);
file_put_contents($dir, $contents);

?>
