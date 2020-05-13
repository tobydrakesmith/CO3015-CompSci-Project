<?php

	$id = $_GET['id'];
	$newPassword = $_GET['p'];

        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if(!$con)
                die("ERROR: Could not connect. " .$this->connect_error);

	$password = sha1($newPassword);

	$stmt = $con->prepare("UPDATE user SET password = ? WHERE userID = ?");
        $stmt->bind_param("si", $password, $id);
	if ($stmt->execute()) echo "Password succesfully updated";
	else echo $con->error;

?>
