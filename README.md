# cse-110-project-team-18

> Thuan Do  
> Justin Bui  
> Jerson Perez  
> Magnus Zuniga  
> Tyler Meyers    
> Om Rabadia    

# Link to project

https://github.com/ucsd-cse110-fa23/cse-110-project-team-18/tree/main 

# Running the project

Ensure you have the standard reccomended java extensions(gradle especially for visual studio code). We all manage our project with Visual Studio Code and Gradle.

Set your port to 8100 and server host to 0.0.0.0 and set USER HOST to your ip address in config.properties.

And make sure to set your mongoDB connection string as an environment variable.

Upon entering the project, do ./gradlew build, this will run the JUnit tests and build the project. ./gradlew run will run the project and open the application.

If you don't want to do that, you can put your javafx libs(depending on platform) and json lib into the /lib folder(json should already be in there) and configure a launch path in launch.json.

The actual running of the app is fairly self explanatory. The main page is a big list of recipes, click new recipe to be guided along the path to create a new recipe, which you can save or not save, and you can edit/delete recipes from the recipe display.