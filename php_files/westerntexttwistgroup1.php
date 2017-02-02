<html>
<head>
<title>Group 1 Text Twist - UWO CS2212</title>
</head>
<body>
<?php
/*
@File: westertexttwistgroup1.php
@Purpose: Display the applet, and pass in User information from facebook
@Author: Mark
*/
	require ("files/facebook.php");
       define("FACEBOOK_APP_ID", '364013840353137');
       define("FACEBOOK_SECRET_KEY", '7314312a0231c05aa38c64e3ac8a5674');
       $facebook = new Facebook(array('appId' => FACEBOOK_APP_ID, 'secret' => FACEBOOK_SECRET_KEY));
       $uid = $facebook->getUser();
       if (!$uid) {
	      	$login_url = $facebook->getLoginUrl($params = array('scope' => "publish_stream"));
    		echo ("<script> top.location.href='".$login_url."'</script>");
              exit;
       }
	try {
		$uid = $facebook->getUser();
		$user_profile = $facebook->api('/me','GET');
		$friends = $facebook->api('/me/friends');

		$newfilename = "files/players/" . $uid . ".txt";

		if (!file_exists($newfilename)) {
			$out = shell_exec("touch $newfilename");
		}
		$allfriends;
		$dirFriends = getcwd() . "/files/players/";
              	foreach ($friends["data"] as $value) {
			$fileName = $dirFriends . $value["id"] . ".txt";
			if (file_exists($fileName)) {	
				$file = fopen ($fileName, "rb");
				$allfriends.=$value["id"]  . "||" . $value["name"] . "\n";
				fclose($file);
			}
              	}

		// testing to see if the friends thing worked^
		$dirFile = getcwd() . "/files/friendList.txt";
		$file = fopen ($dirFile, "wb");
      		fwrite($file, $allfriends);
		fclose($file);
		
		$name = $user_profile['first_name'] . " " . $user_profile['last_name'];
		echo("<applet code=MainGUI.class width=550 height=575>");
		echo("<param name=\"accessToken\" value=\"" . $facebook->getAccessToken() . "\">" );
		echo("<param name=\"name\" value=\"" . $name . "\">" );
		//echo("<param name=\"first\" value=\"" . $fname . "\">" );
		//echo("<param name=\"last\" value=\"" . $lname . "\">" );
		echo("<param name=\"uid\" value=\"" . $uid . "\">" );
		echo("</applet>");

	} catch(FacebookApiException $e) {
              print "in error exception";
              echo ($e);
	}


?>
</body>
</html>