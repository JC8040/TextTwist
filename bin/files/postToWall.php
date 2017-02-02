<html>
<head>
<title>Posting to Wall</title>
</head>
<body>

<?php
		require ("facebook.php");
       define("FACEBOOK_APP_ID", '364013840353137');
       define("FACEBOOK_SECRET_KEY", '7314312a0231c05aa38c64e3ac8a5674');
       $facebook = new Facebook(array('appId' => FACEBOOK_APP_ID, 'secret' => FACEBOOK_SECRET_KEY));

$facebook = new Facebook(array('appId' => FACEBOOK_APP_ID, 'secret' => FACEBOOK_SECRET_KEY));


$accessToken = $_POST['accessToken'];
$puzzle = $_POST['puzzle'];
$score = $_POST['score'];
$time = $_POST['time'];

$uid = $facebook->getUser();
if($user == 0) {

/**
* Get a Login URL for use with redirects. By default, full page redirect is
* assumed. If you are using the generated URL with a window.open() call in
* JavaScript, you can pass in display=popup as part of the $params.
*
* The parameters:
* - redirect_uri: the url to go to after a successful login
* - scope: comma separated list of requested extended perms
*/

$login_url = $facebook->getLoginUrl($params = array('scope' => "publish_stream"));

echo ("<script> top.location.href='".$login_url."'</script>");

}


$attachment = array('access_token'=>$accessToken, 'message' => "Puzzle: $puzzle, Score: $score, Time: $time",
'name' => "Posting Stats",
'caption' => "Played Group1's Text Twist App",
'description' => "This was AWESOME!",
'link' => "http://mshan3.cs2212.ca/Gui11/bin/westerntexttwistgroup1.php",
'picture' => "http://mshan3.cs2212.ca/Gui11/bin/files/group1logo.jpg"
);

try {
// Proceed knowing you have a user who is logged in and authenticated
//$result = $facebook->api('/' . $uid . '/feed/','post',$attachment);
$result = $facebook->api("/$uid/feed","POST",$attachment);
//$result = curl_exec($url);
echo ($result);
} catch (FacebookApiException $e) {
echo ($e);
$user = null;
}




?>
</body>
</html>