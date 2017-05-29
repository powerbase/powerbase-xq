<?php
require_once("HTTP/Request.php");
require_once("HTTP/Request/Listener.php");

class Client {
	const HTTP_METHOD_GET = "GET";
	const HTTP_METHOD_DELETET = "DELETE";
	const HTTP_METHOD_PUT = "PUT";
	const HTTP_METHOD_POST = "POST";

	private $request;
	private $response;
	private $listener;

	public function __construct(User $user, $url, $timeout = 30) {
		$option = array("timeout" => $timeout,);

		$this->request = new HTTP_Request($url, $option);
		$this->listener = new Receiver();

		$this->request->attach($this->listener);
		$this->request->setMethod(HTTP_REQUEST_METHOD_GET);

		$wsse = $user->getAuthenticationHeader();
		$this->request->addHeader("X-WSSE", $wsse);
	}

	public function sendRequest() {
		$this->response = $this->request->sendRequest();
		if (!PEAR::isError($this->response)) {
			return $this->request->getResponseCode();
		} else {
			throw new PowerBaseException($this->response->getMessage());
		}
	}

	public function getStreamResource() {
		return $this->listener->getFp();
	}

}

class Receiver extends HTTP_Request_Listener {
	private $fp;

	public function __construct() {
		parent::__construct();
	}

	public function getFp() {
		return $this->fp;
	}
	public function update(&$subject, $event, $data = null) {
		if ($event == 'connect') {
			$fiveMBs = 5 * 1024 * 1024;
			$this->fp = fopen("php://temp/maxmemory:$fiveMBs", 'r+');
		} else if ($event == 'sentRequest') {
		} else if ($event == 'disconnect') {
		} else if ($event == 'gotHeaders') {
		} else if ($event == 'tick') {
			fputs($this->fp, $data);
		} else if ($event == 'gzTick') {
		} else if ($event == 'gotBody') {
			rewind($this->fp);
		} else {
			throw new Exception($event);
		}
	}
}

?>
