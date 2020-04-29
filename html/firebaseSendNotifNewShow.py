import argparse
import re
import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging
from datetime import datetime

parser = argparse.ArgumentParser(description='Testing')
parser.add_argument("--showName")
parser.add_argument("--venueName")
parser.add_argument("--showType")
parser.add_argument("--venueRegion")

args = parser.parse_args()
a = args.showName
b = args.venueName
c = args.showType
d = args.venueRegion

cred = credentials.Certificate("/home/tjd17/Documents/theatre-tickets-app-firebase-adminsdk-nm5iv-fb5e36781c.json")
firebase_admin.initialize_app(cred)


REGISTRATION_TOKEN = 'eD6N3RqKQpKGR3a7AW9rzQ:APA91bHgFM65JK9EM7oTnZvQPvR-tTX26pDzsGAFpgfhwau3toEK5NDQ9xAS9AJ1pTcRe6eoixQ2vwh4pd_nd7codwn46fYOSy1tMDWdNTnF95TVPX76qYJhTGaA5EWuJNrz9z_U3EG8'

condition = "'" + c + "' in topics && '" + d + "' in topics"

message = messaging.Message(
	notification=messaging.Notification(
		title='A new show has been added!',
            	body= a+ ' is showing at the ' + b + '. Check it out on the app',
        ),
	#token = REGISTRATION_TOKEN,
	condition = condition,
    )

    # specified by the provided condition.
response = messaging.send(message)
    # Response is a message ID string.
print('Successfully sent message:', response)

