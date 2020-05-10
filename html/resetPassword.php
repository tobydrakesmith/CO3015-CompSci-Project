<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';

$emailAddress = $argv[1];
$name = $argv[2];
//$link = 'http://192.168.0.33/passwordReset.php?id=' . $emailAddress;
$link = 'http://81.101.72.233:80/passwordReset.php?id=' . $emailAddress;
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

        $mail->Subject = 'Reset password';

        $mail->isHTML(true);
        $mail->Body = '<h2>Password reset</h2>'.
	'<p>Dear '. $name .',</p><p>We received a request to reset your password for Theatre Tickets App, if this was request was made by you, please click on the below link.'.
        '<p><a href="'.$link.'">Click here to reset your password</a></p>'.
	'<p>If you did not request a change of password, please ignore this email.</p>'.
	'<p>Many thanks,<br>Theatre Tickets App team</p>';

        $mail->send();

	echo "E-mail sent";
}
catch (Exception $e){
        echo $e->errorMessage();
}
catch (\Exception $e){
        echo $e->getMessage();
}

