
<?php
	$host = "127.0.0.1:3307";
	$db_name = "tjd17";
	$username = "tjd17";
	$password = "Mae6am3i";

	$conn = new mysqli($host, $username, $password, $db_name);

	if($conn === false){
		die("ERROR: Could not connect. " . $conn->connect_error);
	}

	$email = $conn->real_escape_string($_REQUEST['email']);
	$firstname = $conn->real_escape_string($_REQUEST['firstname']);
	$lastname = $conn->real_escape_string($_REQUEST['lastname']);
	$password = $conn->real_escape_string($_REQUEST['password']);

	$query = "INSERT INTO user(email, firstName, lastName, password) VALUES ('$email', '$firstname', '$lastname', '$password')";

	if($conn->query($query) === true){
		echo "Records inserted successfully.";
	} else{
		echo "ERROR: error " . $conn->error;
	}


$conn->close();
?>


