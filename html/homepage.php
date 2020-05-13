<?php
session_start();
if($_SESSION['valid']);
else die('Access denied. Please log on');
?>

<!DOCTYPE html>
<html lang="en">
<head><title>Home page</title></head>

<body>

	<p>
		<a href="addShow.php"><button>Add a show</button></a> <a href="editShow.php"><button>Edit existing show</button></a>
	</p>

	<p>
		<a href="addVenue.php"><button>Add a venue</button></a> <a href="editVenue.php"><button>Edit existing venue</button></a>
	</p>

	<p>
		<a href="addShowInstance.php"><button>Add a show instance</button></a> <a href="editShowInstance.php"><button>Edit existing instance</button></a>
	</p>
	<br>
	<br>
	<p>
		<a href="viewSales.php"><button>View sales for a show instance</button></a>
	</p>

	<br>
	<br>
	<p>
		<a href="logout.php"><button onclick>Log out</button></a>
	</p>

</body>
</html>
