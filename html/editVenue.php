<?php
        session_start();
	if(!$_SESSION['valid']) die('Access denied. Please log on');

        function populateVenueList(){
                include_once dirname(__FILE__) . '/constants.php';
                $con = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME) or die ('Cannot connect to db');

                $result = $con->query("SELECT venueName FROM venue ");

                while ($row = $result->fetch_assoc()) {
                        $venueName = $row['venueName'];
                        echo '<option value="'.$venueName.'">'.$venueName.'</option>';
                }
        }

?>

<html>
<head>
<title>Edit venue</title>
<script>
        function showVenueInfo(name){
                xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                        if (this.readyState == 4 && this.status == 200){
				var venue = JSON.parse(this.responseText);
				document.getElementById("venueName").value = name;
				document.getElementById("description").value = venue.description;
				document.getElementById("venue_noseats").value = venue.capacity;
				document.getElementById("venue_postcode").value = venue.postcode;
				document.getElementById("venue_city").value = venue.city;
				document.getElementById("venue_region").value = venue.region;
                        }
                };

                xmlhttp.open("GET", "getVenueDetails.php?q="+name, true);
                xmlhttp.send();

        }
</script>
</head>
<body>
<form name="form" action="editVenueSubmit.php" class="alt" method="POST">
        <p>
                <select name="venue_name" onchange="showVenueInfo(this.value)">
                <option value="">Select a venue</option>
                <?php
                        populateVenueList();
                ?>
                </select>

        </p>

	<p>
		<label for="venueName">Venue name:</label>
		<input type="text" name="venueName" id="venueName">

	</p>


        <label for="venue_description">Venue description:</label>
        </p>

        <textarea name="venue_description"rows="4" id="description" cols="50"></textarea>


        <p>
                <label for="venue_noseats">Number of seats:</label>
                <input type="text" name="venue_noseats" id="venue_noseats">
        </p>

        <p>
                <label for="venue_postcode">Postcode:</label>
                <input type="text" name="venue_postcode" id="venue_postcode">
        </p>

        <p>
                <label for="venue_city">City (or nearest city):</label>
                <input type="text" name="venue_city" id="venue_city">
        </p>

        <p>
                <label for="venue_region">Region</label>
                <select id="venue_region" name="venue_region">
                        <option value="Scotland">Scotland</option>
                        <option value="Northern Ireland">Northern Ireland</option>
                        <option value="Wales">Wales</option>
                        <option value="North East">North East</option>
                        <option value="North West">North West</option>
                        <option value="Yorkshire">Yorkshire</option>
                        <option value="West Midlands">West Midlands</option>
                        <option value="East Midlands">East Midlands</option>
                        <option value="South West">South West</option>
                        <option value="South East">South East</option>
                        <option value="East of England">East of England</option>
                        <option value="Greater London">Greater London</option>
                </select>
        </p>

        <input type="submit" value="Submit" name="edit">
        <br>
        <input type="submit" value="Delete venue" name="delete">
        <br>

	<br>
</form>
	<a href="homepage.php"><button>Home</button></a>
</body>
</html>
