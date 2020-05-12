<?php

        session_start();

        if(!$_SESSION['valid'])
		die('nah m8');

        function populateShowList(){

                include_once dirname(__FILE__) . '/constants.php';

                $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME) or die ('Cannot connect to db');

                $result = $con->query("SELECT showName FROM shows");

                while ($row = $result->fetch_assoc()) {
                        $showName = $row['showName'];
                        echo '<option value="'.$showName.'">'.$showName.'</option>';
                }
        }


?>

<html>
<head>
<title>Edit show</title>
<script>
	function showNameChange(name){
	        xmlhttp = new XMLHttpRequest();
	        xmlhttp.onreadystatechange = function() {
	                if (this.readyState == 4 && this.status == 200){
				var show = JSON.parse(this.responseText);
				document.getElementById("description").value = show.description;
				document.getElementById("running_time").value = show.runningTime;
				document.getElementById("classification").value = show.classification;
	                }
	        };

	        xmlhttp.open("GET", "getShowDetails.php?q="+name, true);
	        xmlhttp.send();

	}

</script>
</head>
<body>
<form name="form" action="editShowSubmit.php" class="alt" method="POST">
        <p>
                <label for="show_name">Show name:</label>
                <select name="show_name" onchange="showNameChange(this.value)">
                <option value="">Select a show</option>
                <?php
                        populateShowList();
                ?>
                </select>
        </p>

        <p>
                <label for="show_description">Show description:</label>
        </p>

        <textarea name="show_description" id="description" rows="4" cols="50"></textarea>

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

	<button>Submit</submit>
	<br>
</form>
	<a href="homepage.php"><button>Home</button></a>

</body>



</html>
