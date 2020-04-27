<?php

	$email = $_GET['email'];

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if(!$con)
                die("ERROR: Could not connect. " .$this->connect_error);



	$newPassword = $_REQUEST['new_password'];
	$password = sha1($newPassword);

	$stmt = $con->prepare("UPDATE user SET password = ? WHERE email = ?");
        $stmt->bind_param("ss", $password, $email);
	if ($stmt->execute()) echo "Password succesfully updated";
	else echo $con->error;

?>
