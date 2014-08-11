                                  My First Application using Play Framework

Since there is so much development happening around the world using Play Framework, I tried my hand-on the same.  Go through the code, because I have used a lot of utilities offered by play framework.
This has been a great learning experience for me as far as Play is concerned. Hopefully you will like it or learn few things too. Please let me know what you think.
This application will let you accomplish the following things:
1.	When a twilio user calls the twilio number, a voice prompts indicates the user to enter a number.  The application reads back the output of FizzBuzz game, to the user.
2.	A web page in the application, allows you to schedule a twilio call. When the user gets his/her scheduled call, a voice prompt indicates him to enter a number. Again, the FizzBuzz is read back to the user. This time the data is persisted and an entry is made to record the Number entered by the user and the Delay-Duration for which our user scheduled the call.
3.	The “History” button on the home page allows you to look at all the calls that got successfully scheduled. The history also shows the number that the user entered. 
Clicking on any item initiates a Twilio call back to the user  and the user gets to hear the FizzBuzz output, without entering any digits, based on the entry of the historical record that our user clicked

Database: MySql.
Scheduler for scheduling Calls:  Akka Framework, specifically, the ActorSystem
Programming Language: Java

Assumption: 
1.	An entry is made to database only after successful call submission after delay
2.	Authentication: Current authentication only checks for an Existing Twilio Signature in the Request Header.  We do not have a Login page when the user can enter the SSID and Authentication Token.  Both of these have been hardcoded in application.conf file.
3.	The application uses my twilio SSID and Authentication Token.  You can change both in application.conf
4.	My web page asks you to enter Caller and Call To numbers.  The way I have tested this is:
Since I do not added any  numbers to my twilio account, I put the same number  in both fields.
So my Caller = xxx-xxx-xxxx and Call From = xxx-xxx-xxxx. Both are the same.
5.	The UI is very basic. As I said, I am not a master of UI. I know my backend thoroughly 
 


Deployment:
1.	I used ngrok to expose the port (9000) I am running my application on. The app is running on my personal laptop
ToDo:
1.	Add better authentication. Ask for Logins
2.	Write unit tests. Time constraint did not allow me to write unit tests. But everytime I write code, I write thinking about how can I run unit tests on it. So I have designed the code with unit-testing in mind.  Having said that, I still need to add unit-tests. I can do that if given more time
3.	 Add more Logging. Use slf4j for Logging.  I have added the my own application-logger.xml, but it is not working. I need to fix this. But for now, I have switched back to the default logging provided by Play
