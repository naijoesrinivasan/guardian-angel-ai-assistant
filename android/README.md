## Guardian Angel Mobile
Sync the Project and install all the gradle dependencies.

Run the backend code from app.py.

There are 2 ways to run the mobile application.

A. 
-  Use the USB cable to connect to the computer and run `adb reverse tcp:5000 tcp:5000`
-  This will let you run the mobile code as is and no changes are required
-  Make sure to switch off the windows firewall or add an inbound rule for port 5000 in the windows firewall.

B. 
You can also connect to Mobile using wifi pairing.
- install ngrok from -> [https://ngrok.com/download](https://ngrok.com/download)
- run ngrok
- in the ngrok command prompt run `ngrok http 5000` and it will give you a URL that maps to localhost:5000
- In MainActivity line no 191 replace `.baseUrl("http://localhost:5000/")` with ngrok url
  

After running the mobile application you will be able to see the screen with options to give text and audio prompt.

