<?php

	$id = $_GET['q'];
?>

<html>
<head>
<title>Reset your password</title>

<script>

	function valid(){
		var p1 = document.getElementById("new_password").value;
		var p2 = document.getElementById("new_password_confirm").value;

		if (p1.localeCompare(p2) == 0){
			if (p1.length >= 5){
				//update password
		                xmlhttp = new XMLHttpRequest();
		                xmlhttp.onreadystatechange = function() {
		                        if (this.readyState == 4 && this.status == 200){
						alert(this.responseText);
		                        }
		                };
		                xmlhttp.open("POST", "passwordResetSubmit.php?id="+<?php echo $id;?>+"&p="+p1, true);
		                xmlhttp.send();

			}  else{
				alert("Passwords must be 5 characters at least");
			}
		} else {
			alert("Passwords do not match");
		}
	}

</script>

</head>

<body>

        <p>
                <label for="new_password">New password</label>
                <input type="password" id="new_password" name="new_password" required>
        </p>

        <p>
                <label for="new_password_confirm">Confirm new password</label>
                <input type="password" id="new_password_confirm" name="new_password_confirm" required>
        </p>

        <button onclick="valid()">Submit</button>


</body>

</html>

