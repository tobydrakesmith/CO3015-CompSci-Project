<?php

	//getting the dboperation class
	require_once 'DbOperation.php';

	//function validating all the paramters are available
	//we will pass the required parameters to this function
	function areTheseParametersAvailable($params){
		//assuming all parameters are available
		$available = true;
		$missingparams = "";

		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false;
				$missingparams = $missingparams . ", " . $param;
			}
		}

		//if parameters are missing
		if(!$available){
			$response = array();
			$response['error'] = true;
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' are missing';

			//displaying error
			echo json_encode($response);

			//stopping further execution
			die();
		}
	}

	//an array to display response
	$response = array();

	//if it is an api call
	//that means a get parameter named api call is set in the URL
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET['apicall'])){

		switch($_GET['apicall']){

			//the CREATE operation
			//if the api call value is 'createuser'
			//we will create a record in the database
			case 'createuser':
				//first check the parameters required for this request are available or not
				areTheseParametersAvailable(array('email','firstName','lastName','password'));

				//creating a new dboperation object
				$db = new DbOperation();

				//creating a new record in the database
				$result = $db->createUser(
					$_POST['email'],
					$_POST['firstName'],
					$_POST['lastName'],
					$_POST['password']
				);


				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false;
					//in message we have a success message
					$response['message'] = 'User added successfully';
					$response['id'] = $result;
					$db->sendWelcomeEmail($_POST['email'], $_POST['firstName']);
				}else{

					//if record is not added that means there is an error
					$response['error'] = true;
					//and we have the error message
					$response['message'] = 'Some error occurred please try again';
				}

				break;

			//the READ operation
			//if the call is getusers
			case 'getusers':
				$db = new DbOperation();
				$response['error'] = false;
				$response['message'] = 'Request successfully completed';
				$response['users'] = $db->getUsers();
				break;

			case 'getuser':

				if(isset($_GET['email'], $_GET['password'])){
					$db = new DbOperation();
					if($db->getUser($_GET['email'],$_GET['password'])){
						$response['error'] = false;
						$response['message']= 'Request successfully completed';
						$response['user'] = $db->getUserDetails($_GET['email']);
					}else{
						$response['error'] = true;
						$response['message'] = 'No user found with these details';
					}
				}else{
					$response['error'] = true;
					$response['message'] = 'Nothing to fetch, provide an email please';
				}
				break;


			case 'checkpassword':

				if(isset($_GET['userID'], $_GET['password'])){
					$db = new DbOperation();
					if($db->checkPassword($_GET['userID'], $_GET['password']))
						$response['error'] = false;
					else
						$response['error'] = true;
				}


				break;

			case 'updatepasswordloggedon':


				if(isset($_GET['userID'], $_GET['password'])){
					$db = new DbOperation();
					if($db->updatePasswordLoggedOn($_GET['userID'], $_GET['password']))
						$response['error'] = false;
					else
						$response['error'] = true;

				}

				break;

			//the delete operation
			case 'deleteuser':

				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['userID'])){
					$db = new DbOperation();
					if($db->deleteUser($_GET['userID'])){
						$response['error'] = false;
						$response['message'] = 'User deleted successfully';
						$response['users'] = $db->getUsers();
					}else{
						$response['error'] = true;
						$response['message'] = 'Some error occurred please try again';
					}
				}else{
					$response['error'] = true;
					$response['message'] = 'Nothing to delete, provide an id please';
				}
				break;

			case 'getliveshows':

				$db = new DbOperation();
				$response = $db->getLiveShows();

				break;


			case 'getshowinfo':
				if(isset($_GET['showName'])){
					$db = new DbOperation();
					if($db->getShowInfo($_GET['showName']))
						$response = $db->getShowInfo($_GET['showName']);
					 else
						$response['error'] = true;
				}
				break;

			case 'createbooking':
				areTheseParametersAvailable(array('instanceID', 'userID', 'numberOfTickets', 'date', 'showTime', 'showName', 'tempID'));
				$db = new DbOperation();

				$result = $db->createBooking($_POST['instanceID'],$_POST['userID'],$_POST['numberOfTickets'],$_POST['date'], $_POST['showTime'], $_POST['showName'], $_POST['tempID']);

				if($result){
					$response['error'] = false;
					$response['message'] = 'Booking added';
					$response['bookingid'] = $result;
				}else{
					$response['error'] = true;
					$response['message'] = $result;
				}

				break;

			case 'createticket':
				areTheseParametersAvailable(array('bookingID', 'price', 'priceBand'));
				$db = new DbOperation();

				$result = $db->createTicket($_POST['bookingID'], $_POST['price'], $_POST['priceBand']);

				if($result){
					$response['error'] = false;
					$response['message'] = 'Ticket added';
				}else{
					$response['error'] = true;
					$response['message'] = "Some error occurred - check input data and try again";
				}
				break;

			case 'getbookings':
				if(isset($_GET['userID'])){
					$db = new DbOperation();
					if ($response = $db->getBookings($_GET['userID']))
						$response = $db->getBookings($_GET['userID']);
					else
						$response['message'] = "Empty";
				}else{
					$response['error'] = true;
					$response['message'] = "UserID missing";
				}
				break;

			case 'createreview':
				areTheseParametersAvailable(array('bookingID', 'userID', 'showName', 'showInstanceID', 'rating'));
				$db = new DbOperation();
				$result = $db->createReview($_POST['bookingID'], $_POST['userID'], $_POST['showName'], $_POST['showInstanceID'], $_POST['rating'], $_POST['review']);
				if ($result) {
					$response['error'] = false;
					$response['message'] = "Review added successfully";
				}else{
					$response['error'] = true;
					$response['message'] = $db->createReview($_POST['bookingID'], $_POST['userID'], $_POST['showName'], $_POST['showInstanceID'], $_POST['rating'], $_POST['review']);
				}
				break;

			case 'gettickets':
				if(isset($_GET['bookingID'])){
					$db = new DbOperation();
					$response = $db->getTickets($_GET['bookingID']);
				}else{
					$response['error'] = true;
					$response['message'] = "BookingID missing";
				}
				break;

			case 'updatepassword':
				if(isset($_GET['password'], $_GET['email'])){
					$db = new DbOperation();
					if ($db->updatePassword($_GET['password'], $_GET['email'])){
						$response['error'] = false;
						$response['message'] = "Password successfully updated";
					}else{
						$response['error'] = true;
						$response['message'] = "Email not found";
					}
				}
				break;


			case 'getvenueinfo':

				if(isset($_GET['venueName'])){
					$db = new DbOperation();
					$response = $db->getVenueInfo($_GET['venueName']);
				}

				break;

			case 'getreviews':

				if(isset($_GET['showName'])){
					$db = new DbOperation();
					$response = $db->getReviews($_GET['showName']);
				}
				else{
					$response['error'] = true;
					$response['message'] = "Missing parameters";
				}

				break;

			case 'getsales':
				if(isset($_GET['showInstanceID'], $_GET['date'], $_GET['time'])){
					$db = new DbOperation();
					$response = $db->checkPriceBand($_GET['showInstanceID'], $_GET['date'], $_GET['time']);
				}else{
					$response['error'] = true;
					$response['message'] = "Missing parameters";
				}

				break;


			case 'getreviewsdetailed':
				if(isset($_GET['showName'])){
					$db = new DbOperation();
					$response = $db->getReviewsDetailed($_GET['showName']);
				}

				break;

			case 'addbasketbooking':
				if(isset($_GET['showInstanceID'], $_GET['priceBand'], $_GET['date'], $_GET['time'], $_GET['userID'], $_GET['numberOfTickets'])){
					$db = new DbOperation();
					$response['id'] = $db->addBasketSales($_GET['showInstanceID'], $_GET['priceBand'], $_GET['date'], $_GET['time'], $_GET['userID'], $_GET['numberOfTickets']);
				}
				break;


			case 'deletebasketbooking':
				if(isset($_GET['tempID'])){
					$db = new DbOperation();
					if ($db->deleteTemp($_GET['tempID'])) $response['error'] = false;
					else $response['error'] = true;
				}


				break;

			case 'checkscanned':

				if(isset($_GET['ticketID'])){
					$db = new DbOperation();
					if ($db->checkScan($_GET['ticketID'])){
						$response['error'] = true;
						$response['message'] = "Ticket has already been scanned";
					}
					else{
						if ($db->scan($_GET['ticketID'])){
							$response['error'] = false;
							$response['message'] = "Not scanned - Ticket has been scanned now";
						}else{
							$response['error'] = true;
						}
					}
				}

				break;

			case 'venueinfobooking':

				if(isset($_GET['showInstanceID'])){
					$db = new DbOperation();
					$response = $db->getVenueInfoForBooking($_GET['showInstanceID']);

				}

				break;

			case 'getrunningtime':
				if(isset($_GET['showName'])){
					$db = new DbOperation();
					$response = $db->getShowRunningTime($_GET['showName']);
				}
				break;

			case 'getuserreviews':

				if(isset($_GET['userID'])){
					$db = new DbOperation();
					$response = $db->getUserReviews($_GET['userID']);
				}

				break;

			case 'sendresetpasswordemail':
				if(isset($_GET['email'])){
					$db = new DbOperation();
					if ($db->checkEmail($_GET['email'])){
						$name = $db->getFirstName($_GET['email']);
						$id = $db->getId($_GET['email']);
						$response['message'] = $db->sendResetPasswordEmail($_GET['email'], $name, $id);
						$response['error'] = false;
					}else{
						$response['error'] = true;
						$response['message'] = "The provided email was not found on our system. Please try again.";
					}
				}

				break;

			case 'sendbookingconfirmation':
				areTheseParametersAvailable(array('email', 'subject', 'content'));
				$db = new DbOperation();

				$response = $db->sendBookingConfirmation(
					$_POST['email'],
					$_POST['subject'],
					$_POST['content']
				);


				break;

			case 'editusereview':
				areTheseParametersAvailable(array('reviewid', 'rating'));
				$db = new DbOperation();

				if ($db->editUserReview($_POST['reviewid'],$_POST['rating'],$_POST['review'])) $response = "edited";
				else $response = "error";

				break;

			case 'getvenues':
				$db = new DbOperation();
				$response = $db->getVenues();
				break;

			case 'getshowinstance':
				if(isset($_GET['venueName'])){
					$db = new DbOperation();
					$response = $db->getShowInstance($_GET['venueName']);
				}
				break;
		}
	}else{
		//if it is not api call
		//pushing appropriate values to response array
		$response['error'] = true;
		$response['message'] = 'Invalid API Call';
	}
	//displaying the response in json structure
	echo json_encode($response);
?>
