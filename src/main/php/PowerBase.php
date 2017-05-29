<?php
require_once("PowerBase/User.php");
require_once("PowerBase/EndPoint.php");
require_once("PowerBase/Connector.php");

class PowerBase extends Connector
{
	const COMMAND_GET_INDEX = "GetIndex";
	const COMMAND_GET = "Get";
	const COMMAND_GET_LIST = "GetList";
	const COMMAND_GET_BY_ID = "GetById";

	private $connector;

	public function __construct(User $user, EndPoint $endPoint)
	{
		$this->connector = new Connector($user, $endPoint);
	}

	public function getConnector()
	{
		return $this->connector;
	}


}

class PowerBaseException extends Exception
{
}
?>
