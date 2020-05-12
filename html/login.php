<?php
session_start();
?>

<!DOCTYPE HTML>
<html>
<head>
<title>Log in</title>
<style> .error {color: #FF0000;} </style>
</head>
<body>

	<h2>Login</h2>

	<form method="get" name="form" action="adminLogin.php">
		Username: <input type="text" id="username" name="username">
		<br><br>
		Password: <input type="password" id="password" name="password">
		<br><br>
		<button>Login</button>
	</form>

</body>
</html>
