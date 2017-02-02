<html>
<head>
<title>TESTING</title>
</head>
<body>

<?php
		require ("facebook.php");
       define("FACEBOOK_APP_ID", '364013840353137');
       define("FACEBOOK_SECRET_KEY", '7314312a0231c05aa38c64e3ac8a5674');
       $facebook = new Facebook(array('appId' => FACEBOOK_APP_ID, 'secret' => FACEBOOK_SECRET_KEY));		


		shell_exec('rm -r pictureFolder');
		shell_exec('mkdir -p pictureFolder');

		
		$uid = $_GET['uid'];

		$profilepic = "https://graph.facebook.com/" . $uid ."/picture&type=large";
		$headers = get_headers($profilepic,1);
		$url = $headers['Location'];

		$dirPath = getcwd();
		$newfilename = $dirPath . "/pictureFolder/" . $uid . ".jpg";
		
  		$file = fopen ($url, "rb");
  		if ($file) {
    			$newfile = fopen ($newfilename, "wb");
    			if ($newfile)
    				while(!feof($file)) {
      					fwrite($newfile, fread($file, 1024 * 8 ), 1024 * 8 );
    				}
  		}
  		if ($file) {
    			fclose($file);
  		}
  		if ($newfile) {
    			fclose($newfile);
  		}
?>
</body>
</html>