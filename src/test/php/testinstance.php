<?php
require_once("PowerBase.php");

$user1 = new User("admin", "admin");
$endPoint1 = new EndPoint("http://localhost:8080/powerbase");

$pb1 = new PowerBase($user1, $endPoint1);
$con1 = $pb1->getConnector();
print $con1->getEndPoint()->toString();

print "<br />";
$user2 = new User("hirai", "hirai");
$endPoint2 = new EndPoint("http://localhost:8080/resource");

$pb2 = new PowerBase($user2, $endPoint2);
$con2 = $pb2->getConnector();
print $con2->getEndPoint()->toString();

?>
