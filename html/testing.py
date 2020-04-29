import firebase_admin
from firebase_admin import credentials
from firebase_admin import messaging

cred = credentials.Certificate("/home/tjd17/Documents/theatre-tickets-app-firebase-adminsdk-nm5iv-fb5e36781c.json")
firebase_admin.initialize_app(cred)


condition = "'newShowLondon' in topics"

message = messaging.Message(
        notification=messaging.Notification(
                title='Test title',
                body= 'Testing topics',
        ),
        condition = condition,
)

response = messaging.send(message)
# Response is a message ID string.
print('Successfully sent message:', response)

