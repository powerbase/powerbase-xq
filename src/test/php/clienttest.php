<?php
require_once("../../main/php/PowerBase.php");
require_once("../../main/php/PowerBase/User.php");
require_once("../../main/php/PowerBase/Client.php");

header("Content-type: text/xml; charset=utf-8");

$user = new User("admin", "admin");
$client = new Client($user, "http://localhost:8080/powerbase/aozora/sakka");
try {
	$client->sendRequest();
} catch (PowerBaseException $e) {
	var_dump($e->getMessage());
}

$fp = fopen('php://output', 'w');
if ($fp === false) {
	die('open failed.');
}
fputs($fp, stream_get_contents($client->getStreamResource()));
fclose($fp);

?>
