<?php
class WSSE
{
	public static function getHeader($userId, $passWord)
	{
		$created = date('Y-m-d\TH:i:s\Z');
		$nonce = pack('H*', sha1(md5(time())));
		$digest = base64_encode(pack('H*', sha1($nonce.$created.$passWord)));
		$header = 'UsernameToken Username="'.$userId.'", '.
				  'PasswordDigest="'.$digest.'", '.
				  'Nonce="'.base64_encode($nonce).'",'.
				  'Created="'.$created.'"';
		return $header;
	}
}
?>
