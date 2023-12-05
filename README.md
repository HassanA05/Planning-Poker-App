# Planning-Poker-App

Hello! This is my app called the Planning Poker App. It is an app made using Java swing, and does alot of things including:

## Key Features

1. **Java Swing Interface:**
   - The app features a user-friendly Java Swing interface created by the `PlanningPokerUI` class. This interface includes input panels for team members to submit their names and votes, a dynamic data table for transparent visibility, and control buttons for facilitators to reveal or reset votes.

2. **File Operations:**
   - File handling is managed by the `PlanningPokerFunctionality` class. Team members' votes are stored as individual files in the "NamesAndChoicesStorage" directory. The `inputToFile` method writes each vote to a file, and the `populateAndDisplayTable` method reads the files to display the estimation results in the data table.

3. **Polling and Threads:**
   - The continuous polling and threading functionality is handled by the `PlanningPokerFunctionality` class. The `startFlagPollingThread` method initiates a thread that continuously checks for the existence of a "reveal.flg" flag file, controlling when to reveal estimation results. The thread can be stopped using the `stopFlagPollingThread` method.

4. **Dynamic UI Interaction:**
   - The `PlanningPokerUI` class dynamically enables and disables UI components based on user actions. For example, voting buttons are disabled until a name is entered, enhancing the user experience and preventing unintended interactions.

5. **Transparent Data Presentation:**
   - The `PlanningPokerFunctionality` class ensures transparent data presentation by reading vote files and populating the data table upon revealing votes. The `handleTableVisibility` method controls the visibility of the data table based on the existence of the "reveal.flg" flag file.

6. **Reset and Reveal Functionality:**
   - The app allows facilitators to reset all votes or reveal them using the `Reset` and `Reveal` buttons, respectively. The corresponding actions are triggered by the `resetButton.addActionListener` and `revealButton.addActionListener` methods in the `PlanningPokerUI` class.

7. **User Input Validation:**
   - The `PlanningPokerUI` class validates user input in the name field to ensure it contains only letters. If an invalid entry is detected, the app displays an error message using `JOptionPane` and prompts the user to enter a valid name again.

8. **Efficient File Management:**
   - The `PlanningPokerFunctionality` class includes methods for creating and deleting files, ensuring efficient file management. The `createRevealFlag` method creates a "reveal.flg" flag file, and the `deleteAllFilesAndFlags` method deletes all files in the "NamesAndChoicesStorage" directory.

The primary objective of the Planning Poker app is to facilitate unbiased and independent estimation within a development team when determining the duration of a project. Team members can input their names and individual project duration estimates without being influenced or pressured by the choices of their peers. This approach promotes impartiality, allowing each team member to contribute their perspective freely, resulting in a more diverse and accurate range of project duration estimates.

*IMPORTANT*

The PlanningPokerFunctionality class relies on a shared drive, you will need to edit a line of code to enter a directory that is in a shared drive. The name of the Directory variable is BASEPATH. It relies on a shared drive so that the poll can see if a reveal.flg file exists and make the results display on everyone who is using the app.

Another thing to note is this uses TableLayout. That is not standard java, so you will need to import a tablelayout into your enviroment, otherwise this will give you problems.

DIRECTIONS OF USE

Make sure you do the above, otherwise the app will not work. 

1. Run PlanningPokerApp in your Java enviroment of choice eg Eclipse
2. This will bring up the interface, enter your name then hit enter on your keybaord.
3. Choose a choice, then when everyone else has made a choice aswell, click reveal
4. To reset the table, click reset and this will reset and name and choice displayed in the table
5. When you load the app, it might auto display a name and choice, it means someone made a name and choice, clicked reveal then did not click reset after use. Click reset and it will do the above and you can continue with use.
