<?php

	$email = $_GET['id'];
?>

<html>
<head>
<title>Reset your password</title>
</head>

<body>
<form action="passwordResetSubmit.php?email=<?php echo $email;?>" method="POST">

        <p>
                <label for="new_password">New password</label>
                <input type="text" name="new_password">
        </p>

        <p>
                <label for="new_password_confirm">Confirm new password</label>
                <input type="text" name="new_password_confirm">
        </p>

        <input type="submit">


</form>
</body>

</html>

