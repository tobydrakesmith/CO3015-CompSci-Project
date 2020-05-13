<?php
session_start();
if(!$_SESSION['valid']) die('Access denied. Please log on');
?>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Add new show</title>
<script>
function showNameChange(name){
	xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function(){
		if (this.readyState == 4 && this.status == 200){
			document.getElementById("show_name_exists").innerHTML = this.responseText;
			if (this.responseText == "Show name OK"){
				document.forms["form"]["submit"].disabled = false;
			}else{
				document.forms["form"]["submit"].disabled = true;
			}
		}
	};

	xmlhttp.open("GET", "getShowName.php?showName="+name, true);
	xmlhttp.send();
}
</script>
</head>

<body>
<form name="form" action="addShowSubmit.php" class="alt" method="POST">

	<p>
		<label for="show_name">Show Name:</label>
		<input type="text" name="show_name" onchange="showNameChange(this.value)" id="show_name">
	</p>

	<div id="show_name_exists"></div>

	<p>
		<label for="show_description">Show description:</label>
	</p>

	<textarea name="show_description" rows="4" cols="50"></textarea>

	<p>
		<label for="running_time">Running time (minutes, inc. interval):</label>
		<input type="number" name="running_time" id="running_time">
	</p>

	<p>
		<label for="classification">Play or musical:</label>
		<select id="classification" name="classification">
			<option value="Musical">Musical</option>
			<option value="Play">Play</option>
		</select>
	</p>


	<br>

	<input type="submit" id="submit">


	<br>
</form>
	<a href="homepage.php"><button>Home</button></a>


</body>
</html>
