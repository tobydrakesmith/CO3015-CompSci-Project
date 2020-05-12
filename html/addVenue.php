<?php
session_start();
if($_SESSION['valid']);
else die('nah m8');
?>

<!DOCTYPE html>
<html lang="en">
<head> <title>Add venue</title>
<script>
function venueNameChange(name){
        xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function(){
                if (this.readyState == 4 && this.status == 200){
                }
        };

        xmlhttp.open("GET", "getVenueDetails.php?venueName="+name, true);
        xmlhttp.send();
}
</script>
</head>

<body>
<form name="form" action="addVenueSubmit.php" class="alt" method="POST">

	<p>
		<label for="venue_name">Venue name:</label>
		<input type="text" name="venue_name" onchange="venueNameChange(this.value)" id="venue_name">
	</p>
	<div id="venue_name_exists"></div>
	<p>
	<label for="venue_description">Venue description:</label>
	</p>

	<textarea name="venue_description"rows="4" cols="50"></textarea>


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



	<input type="submit" id="submit">



</form>
<br>
<a href="homepage.php"><button>Home</button></a>

</body>

</html>

