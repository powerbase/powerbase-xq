<?php
require_once("WSSE.php");

class User
{
	private $userId;
	private $passWord;

	public function __construct($userId, $passWord)
	{
		$this->userId = $userId;
		$this->passWord = $passWord;
	}

	public function getUserId()
	{
		return $this->userId;
	}

	public function getPassWord()
	{
		return $this->passWord;
	}

	public function getAuthenticationHeader()
	{
		return WSSE::getHeader($this->userId, $this->passWord);
	}

}
?>
