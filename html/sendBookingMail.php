<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';

$emailAddress = $argv[1];
$subject = $argv[2];
$content = $argv[3];
$content = str_replace('newline', '<br>', $content);
$content = str_replace('poundsymbol', '£', $content);

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

        $mail->Subject = $subject;

        $mail->isHTML(true);
        $mail->Body = '<h2>Booking confirmation</h2><br>' . $content;

        $mail->send();

	echo "E-mail sent";
}
catch (Exception $e){
        echo $e->errorMessage();
}
catch (\Exception $e){
        echo $e->getMessage();
}

?>
