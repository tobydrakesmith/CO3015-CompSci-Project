<!DOCTYPE HTML>
<html>
<head>
<title>Log in</title>
<style> .error {color: #FF0000;} </style>
</head>
<body>

<?php
// define variables and set to empty values
$usernameErr = $passwordErr = "";
$username = $password = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	if(empty($_POST["username"])){
		$usernameErr = "Username is required";
	}else{
		$username = test_input($_POST["username"]);
	}
	if(empty($_POST["password"])){
		$passwordErr = "Password is required";
	}else{
		$password = test_input($_POST["password"]);
	}
	if(!empty($_POST["username"]) && !empty($_POST["password"])){
		if ($username == "admin" && $password == "password"){
			header("location: homepage.html");
		}else{
			echo "Username or password incorrect";
		}
	}
}

function test_input($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	return $data;
}
?>

<h2>Login</h2>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
  Username: <input type="text" name="username">
  <span class="error">* <?php echo $usernameErr;?></span>
  <br><br>
  Password: <input type="password" name="password">
  <span class="error">* <?php echo $passwordErr;?></span>
  <br><br>
  <input type="submit" value="Submit">
</form>

