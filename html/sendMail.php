<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';

$emailAddress = $argv[1];
$firstName = $argv[2];

$mail = new PHPMailer(TRUE);

try {
	$mail->isSMTP();
	$mail->Host = 'smtp.gmail.com';
	$mail->Port = 587;
	$mail->SMTPAuth = true;
	$mail->SMTPSecure = 'tls';
	$mail->Username = 'theatreticketsapplication@gmail.com';

	$mail->Password = 'cgzdopnrvgjufylj';

	$mail->setFrom('admin@theatreticketsapp.com', 'Theatre Tickets App');

	$mail->addAddress($emailAddress);

	$mail->Subject = 'Account creation';

	$mail->isHTML(true);
	$mail->Body = '<h2>Account creation</h2>'.
	'<p>Hello ' . $firstName .',<br><br>This is an email to confirm your new account.<br><br>You can now sign into the app with the details you booked<br><br>Best wishes</p>';

	$mail->send();
	echo "E-mail sent";
}
catch (Exception $e){
	echo $e->errorMessage();
}
catch (\Exception $e){
	echo $e->getMessage();
}
