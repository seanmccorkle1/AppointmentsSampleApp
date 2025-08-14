# Sample scheduling application

The purpose is to build a simple JavaFX GUI that lets someone log in and make appointments for their business. The course is about java but it uses SQL to get the sample data.
Date submitted: 4 Jan 2025

Rubric:

"The submitted code provides an alert when there is an appointment within 15 minutes of the user’s
log-in and displays a custom message in the user interface that is visible to the user that includes
the appointment ID, date, and time."

"A custom message is displayed upon log-in in the user interface indicating there are no upcoming
appointments if the user does not have any appointments in the next 15 minutes."

A3e
 * For this one, if it's a weekend, you won't be able to see the "appointment within 15 minutes" alert because you can't schedule anything on a weekend. You would have to wait until monday.
 * So I included a screenshot in the submission so you can see.
----------------------
How to run it in IntelliJ:

  1. For the configuration in the top-right, set 'java 17' as the SDK.

  2. Set com.example.qam2sampleapp.Go as the main class

  3. Set C:\{whatever}\QAM2SampleApp as 'working directory'

  4. Go to 'project structure' in in File > project structure

  5. Set 'language level' and 'SDK' to 17

  6. In modules > sources, if you have to, set 'source folders' as src\main\java, then set 'resource folders' to src\main\resources, and 'excluded folders' as target . Then set 'language level' to 17.

  7. In Modules > Dependencies, add mysql-connector-j-8.3.0.jar as as compile dependency

  8. In Libraries, click the plus sign, then locate your openjfx version 17.0.13 or lower's x\x\x\lib folder and add that folder. Then add mysql-connector-j-8.3.0.jar file under 'classes'.

 9. In SDKs, make sure 17 is selected.

 10. In the left, make sure mysql-connector.jar file and jdk 17 is under 'external libraries'.
----------------------
Images
configuration.png
  * Hit the down arrow that is there, and click 'edit configuration'
  * Make sure 'program arguments' has this line in it:
  * --module-path "C:\Program Files\Java\jdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml

----------------------
Additional report (A3f):

The additional 'reports' I did were on 2,
  1: Total number of appointments by which year they're in
  2: Total number of customers that live in a region

If there's 4 appointments in the year 2024, then it would show 4 as the total.
If there's 4 customers that live in Virginia, it would show 4.
----------------------
Customer ID #1 is 'Daddy Warbucks'
Customer ID #2 is 'Lady McAnderson'
Customer ID #3 is 'Dudley Do-Right'

Hitting 'stop' and running the project might reset `appointments_list` to an empty list.
Sometimes it does, sometimes it doesn't, but if it does, you have to re-add the appointments. Even if the appointments show on the GUI table.
----------------------
I used the VM provided by wgu for most of it. The URL for that is https://lrps.wgu.edu/provision/289188806 for me

IDE 
 * IntelliJ 2023.2.2

JDK version
 * 17.0.1

MySQL connector
 * mysql-connector-java-8.0.25.jar
