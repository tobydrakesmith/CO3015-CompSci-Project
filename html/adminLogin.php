<?php
	session_start();
        include_once dirname(__FILE__) . '/constants.php';

        $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

        if($con === false)
                die("ERROR: could not connect. " . $this->connect_error);


        $username = $_REQUEST['username'];
	$password = $_REQUEST['password'];

        $stmt = $con->prepare("SELECT * FROM adminUser WHERE username = ? AND password = ?");
	$stmt->bind_param("ss", $username, $password);
	$stmt->execute();
	if ($stmt->fetch()){
		$_SESSION['valid'] = true;
		echo header("location: homepage.php");

	}
	else echo header("location: login.php");
//	echo $username;
?>



