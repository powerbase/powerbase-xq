<?php
class Connector
{
	private $user;
	private $endPoint;

	protected function __construct(User $user, EndPoint $endPoint)
	{
		$this->user = $user;
		$this->endPoint = $endPoint;
	}

	public function getUser()
	{
		return $this->user;
	}

	public function getEndPoint()
	{
		return $this->endPoint;
	}

}
?>
