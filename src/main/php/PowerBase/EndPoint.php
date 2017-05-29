<?php
require_once('Net/URL.php');

class EndPoint {
	private $url;

	public function __construct($url) {
		$this->url = new Net_URL($url);
	}

	public function toString() {
		return $this->getURL();
	}

	public function addQueryString($name, $value, $preencoded = false) {
		$this->url->addQueryString($name, $value, $preencoded);
	}
	public function addRawQueryString($querystring) {
		$this->url->addRawQueryString($querystring);
	}
	public function getOption($optionName) {
		return $this->url->getOption($optionName);
	}
	public function getQueryString() {
		return $this->url->getQueryString();
	}
	public function getStandardPort($scheme) {
		return $this->url->getStandardPort($scheme);
	}
	public function getURL() {
		return $this->url->getURL();
	}
	public function removeQueryString($name) {
		$this->url->removeQueryString($name);
	}
	public function resolvePath($path) {
		return $this->url->resolvePath($path);
	}
	public function setOption($optionName, $value) {
		$this->url->setOption($optionName, $value);
	}
	public function setProtocol($protocol, $port = null) {
		$this->url->setProtocol($protocol, $port);
	}
}
?>
