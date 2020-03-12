<?php

	class DbOperation{

		private $con;

		function __construct(){
			//Getting the DbConnect.php file
			require_once dirname(__FILE__) . '/DbConnect.php';

			//Creating a DbConnect object to connect to the database
			$db = new DbConnect();

			//Initializing our connection link of this class
			//by calling the method connect of DbConnect class
			$this->con = $db->connect();
		}

		/*
		* The create operation
		* When this method is called a new record is created in the database
		*/
		function createUser($email, $firstName, $lastName, $password){
			$stmt = $this->con->prepare("INSERT INTO user (email, firstName, lastName, password) VALUES (?, ?, ?, ?)");
			$stmt->bind_param("ssss", $email, $firstName, $lastName, $password);

			if($stmt->execute())
				return true;
			return false;
		}

		/*
		* The read operation
		* When this method is called it is returning all the existing record of the database
		*/

		function getUserID($email){
			$stmt = $this->con->prepare("SELECT userID FROM user WHERE email = ? ");
			$stmt->bind_param("s", $email);
			$stmt->execute();
			$stmt->bind_result($userID);
			if($stmt->fetch())
				return $userID;
			else
				return false;
		}


		function getUsers(){
			$stmt = $this->con->prepare("SELECT userID, email, firstName, lastName, password FROM user");
			$stmt->execute();
			$stmt->bind_result($userID, $email, $firstName, $lastName, $password);

			$users = array();

			while($stmt->fetch()){
				$user  = array();
				$user['userID'] = $userID;
				$user['email'] = $email;
				$user['firstName'] = $firstName;
				$user['lastName'] = $lastName;
				$user['password'] = $password;

				array_push($users, $user);
			}

			return $users;
		}

		function getUser($email, $password){
			if($stmt = $this->con->prepare("SELECT email, password FROM user WHERE email = ? AND password = ? ")){
				$stmt->bind_param("ss", $email, $password);
				$stmt->execute();
				$stmt->bind_result($email, $password);
				if($stmt->fetch())
					return true;
			}
			return false;
		}

		/*
		* The update operation
		* When this method is called the record with the given id is updated with the new given values
		*/
		function updateUser($userID, $email, $firstName, $lastName, $password){
			$stmt = $this->con->prepare("UPDATE user SET email = ?, firstName = ?, lastName = ?, password = ? WHERE userID = ?");
			$stmt->bind_param("ssssi", $email, $firstName, $lastName, $password, $userID);
			if($stmt->execute())
				return true;
			return false;
		}
		/*
		* The delete operation
		* When this method is called record is deleted for the given id
		*/
		function deleteUser($userID){
			$stmt = $this->con->prepare("DELETE FROM user WHERE userID = ? ");
			$stmt->bind_param("i", $userID);
			if($stmt->execute())
				return true;

			return false;
		}

		function getLiveShows(){
			$stmt = $this->con->prepare("SELECT * FROM showInstance WHERE startDate <= CURDATE() AND endDate > CURDATE()");
			$stmt->execute();
			$stmt->bind_result($id, $showName, $venueName, $startDate, $endDate, $rating, $bandAPrices, $bandBPrices, $bandCPrices, $bandDPrices, $bandANum, $bandBNum, $bandCNum, $bandDNum, $monMat, $monEve, $tueMat, $tueEve, $wedMat, $wedEve, $thuMat, $thuEve, $friMat, $friEve, $satMat, $satEve, $sunMat, $sunEve, $matTime, $eveTime);

                        $liveshows = array();

                        while($stmt->fetch()){
                                $liveshow  = array();
				$liveshow['id'] = $id;
                                $liveshow['showName'] = $showName;
                                $liveshow['venueName'] = $venueName;
                                $liveshow['startDate'] = $startDate;
                                $liveshow['endDate'] = $endDate;
				$liveshow['rating']= $rating;
				$liveshow['bandAPrice'] = $bandAPrices;
				$liveshow['bandBPrice'] = $bandBPrices;
				$liveshow['bandCPrice'] = $bandCPrices;
				$liveshow['bandDPrice'] = $bandDPrices;
				$liveshow['bandANumberOfTickets'] = $bandANum;
				$liveshow['bandBNumberOfTickets'] = $bandBNum;
				$liveshow['bandCNumberOfTickets'] = $bandCNum;
				$liveshow['bandDNumberOfTickets'] = $bandDNum;
				$liveshow['monMat'] = $monMat;
				$liveshow['monEve'] = $monEve;
				$liveshow['tueMat'] = $tueMat;
                                $liveshow['tueEve'] = $tueEve;
				$liveshow['wedMat'] = $wedMat;
                                $liveshow['wedEve'] = $wedEve;
                                $liveshow['thuMat'] = $thuMat;
                                $liveshow['thuEve'] = $thuEve;
                                $liveshow['friMat'] = $friMat;
                                $liveshow['friEve'] = $friEve;
                                $liveshow['satMat'] = $satMat;
                                $liveshow['satEve'] = $satEve;
                                $liveshow['sunMat'] = $sunMat;
                                $liveshow['sunEve'] = $sunEve;
				$liveshow['matTime'] = $matTime;
				$liveshow['eveTime'] = $eveTime;
                                array_push($liveshows, $liveshow);
                        }

                        return $liveshows;


		}

		function getShowInfo($showName){

			if($stmt = $this->con->prepare("SELECT showDescription FROM shows WHERE showName = ? ")){
				$stmt->bind_param("s", $showName);
				$stmt->execute();
				$stmt->bind_result($showdesc);

				$stmt->fetch();
				$show = array();
				$show['showDesc'] = $showdesc;
				return $show;

			}
			return false;
		}

		function createBooking($instanceID, $userID, $numberOfTickets, $date, $showTime, $showName){
			$stmt = $this->con->prepare("INSERT INTO booking(showInstanceID, userID, numberOfTickets, bookingDate, showTime, showName) VALUES (?, ?, ?, ?, ?, ?)");
                       	$stmt->bind_param("iiisss", $instanceID, $userID, $numberOfTickets, $date, $showTime, $showName);
			if($stmt->execute())
				return mysqli_insert_id($this->con);
			else
				return $this->con->error;

		}

		function createTicket($bookingID, $price, $priceBand){
			$stmt = $this->con->prepare("INSERT INTO ticket(bookingID, price, priceBand) VALUES (?, ?, ?)");
			$stmt->bind_param("iis", $bookingID, $price, $priceBand);
			if($stmt->execute()) return true;
			else return false;
		}

		function getFutureBookings($userID){
			$stmt = $this->con->prepare("SELECT bookingID, showInstanceID, numberOfTickets, bookingDate, showTime, showName FROM booking WHERE userID= ? AND bookingDate >= CURDATE() ");
			$stmt->bind_param("i", $userID);
			$stmt->execute();
			$stmt->bind_result($bookingID, $showInstanceID, $numberOfTickets, $bookingDate, $showTime, $showName);

			$bookings = array();
			while($stmt->fetch()){
				$booking = array();
				$booking['bookingID'] = $bookingID;
				$booking['showInstanceID'] = $showInstanceID;
				$booking['numberOfTickets'] = $numberOfTickets;
				$booking['bookingDate'] = $bookingDate;
				$booking['showTime'] = $showTime;
				$booking['showName'] = $showName;

				array_push($bookings, $booking);
			}

			return $bookings;
		}

		function getPastBookings($userID){
	  		$stmt = $this->con->prepare("SELECT bookingID, showInstanceID, numberOfTickets, bookingDate, showTime, showName FROM booking WHERE userID= ? AND bookingDate < CURDATE() ");
	                $stmt->bind_param("i", $userID);
        	        $stmt->execute();
                        $stmt->bind_result($bookingID ,$showInstanceID, $numberOfTickets, $bookingDate, $showTime, $showName);

                        $bookings = array();
                        while($stmt->fetch()){
                                $booking = array();
				$booking['bookingID'] = $bookingID;
                                $booking['showInstanceID'] = $showInstanceID;
                                $booking['numberOfTickets'] = $numberOfTickets;
                                $booking['bookingDate'] = $bookingDate;
				$booking['showTime'] = $showTime;
                                $booking['showName'] = $showName;

                                array_push($bookings, $booking);
                        }

                        return $bookings;
		}

		function createReview($bookingID, $userID, $showName, $showInstanceID, $rating, $review){
			$stmt = $this->con->prepare("INSERT INTO review(bookingID, userID, showName, showInstanceID, rating, review) VALUES(?, ?, ?, ?, ?, ?)");
			$stmt->bind_param("iisiis", $bookingID, $userID, $showName, $showInstanceID, $rating, $review);
			if($stmt->execute()){
				$stmt = $this->con->prepare("UPDATE booking SET reviewLeft = 1 WHERE bookingID = ?");
				$stmt->bind_param("i", $bookingID);
				$stmt->execute();
				return true;
			}else
				return $this->con->error;
		}

		function getTickets($bookingID){
			$stmt = $this->con->prepare("SELECT ticketID, price FROM ticket WHERE bookingID = ?");
			$stmt->bind_param("i", $bookingID);
			$stmt->execute();
			$stmt->bind_result($ticketID, $price);

			$tickets = array();
			while($stmt->fetch()){
				$ticket = array();
				$ticket['ticketID'] = $ticketID;
				$ticket['price'] = $price;
				array_push($tickets, $ticket);
			}

			return $tickets;
		}

		function updatePassword($password, $userID){
			$stmt = $this->con->prepare("UPDATE user SET password = ? WHERE userID = ?");
			$stmt->bind_param("si", $password, $userID);
			if ($stmt->execute()) return true;
			else return false;
		}

		function checkReview($bookingID){
			$stmt = $this->con->prepare("SELECT reviewLeft FROM booking WHERE bookingID = ?");
			$stmt->bind_param("i", $bookingID);
			$stmt->execute();
			$stmt->bind_result($reviewLeft);
			$stmt->fetch();

			if($reviewLeft==0) return false;
			else return true;
		}

		function getVenueInfo($venueName){
			$stmt = $this->con->prepare("SELECT venueDesc, postcode FROM venue WHERE venueName = ?");
			$stmt->bind_param("s", $venueName);
			$stmt->execute();
			$stmt->bind_result($venueDescription, $postcode);
			$stmt->fetch();
			$venue = array();
			$venue['venueDescription'] = $venueDescription;
			$venue['postcode'] = $postcode;

			return $venue;
		}

		function getReviews($showName){
			$stmt = $this->con->prepare("SELECT rating, review FROM review WHERE showName = ?");
			$stmt->bind_param("s", $showName);
			$stmt->execute();
			$stmt->bind_result($rating, $reviewText);
			$reviews = array();
			while($stmt->fetch()){
				$review = array();
				$review['rating'] = $rating;
				$review['review'] = $reviewText;

				array_push($reviews, $review);
			}

			return $reviews;
		}

		function getSales($showInstanceID, $date, $time){
			$stmt = $this->con->prepare("SELECT bookingID FROM booking WHERE showInstanceID = ? AND bookingDate = ? AND showTime = ?");
			$stmt->bind_param("iss", $showInstanceID, $date, $time);
			$stmt->execute();
			$stmt->bind_result($bookingID);
			$bookingIDs = array();
			while($stmt->fetch()){
				$booking = array();
				$booking = $bookingID;
				array_push($bookingIDs, $booking);
			}

			return $bookingIDs;
		}

		function checkPriceBand($showInstanceID, $date, $time){
			$bookings = array();
			$bookings = $this->getSales($showInstanceID, $date, $time);
			$ids = join(',', $bookings);
			$stmt = $this->con->prepare("SELECT priceBand FROM ticket WHERE bookingID IN ($ids)");
			$stmt->execute();
			$stmt->bind_result($priceBand);
			$priceBands = array();
			while($stmt->fetch()){
				$result = array();
				$result['priceBand'] = $priceBand;
				array_push($priceBands, $result);
			}
			return $priceBands;
		}

		function getReviewsDetailed($showName){
			$stmt = $this->con->prepare("SELECT userID, bookingID, rating, review, date FROM review WHERE showName = ?");
			$stmt->bind_param("s", $showName);
			$stmt->execute();
			$stmt->bind_result($userID, $bookingID, $rating, $reviewText, $date);
			$reviews = array();
			while($stmt->fetch()){
				$review = array();
				$review['userID'] = $userID;
				$review['bookingID'] = $bookingID;
				$review['rating'] = $rating;
				$review['reviewText'] = $reviewText;
				$review['date'] = $date;
				array_push($reviews, $review);
			}

			return $reviews;

		}


	}

?>

