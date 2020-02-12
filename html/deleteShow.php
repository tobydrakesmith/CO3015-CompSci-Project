<?php

//	echo "<html>";
//	echo "<body>";
//	echo "<select name='show_name'>";
	function populateList(){

	include_once dirname(__FILE__) . '/constants.php';

	$con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME) or die ('Cannot connect to db');

	$result = $con->query("SELECT showName FROM shows ");

	echo '<option value="Hello world">Hello World</option>';
		while ($row = $result->fetch_assoc()) {
			$showName = $row['showName'];
			echo '<option value="'.$showName.'">'.$showName.'</option>';
		}
	}
//	echo "</select>";
//	echo "</body>";
//	echo "</html>";
?>


<html>
<head><title>Delete show</title></head>
<body>
<select name="shows">
<?php
populateList();
?>
</select>
</body>

</html>



