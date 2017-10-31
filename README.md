## iddqdBot

### Intro
This project was created during a university hackathon. Development took 48 hours and the bot took 1st place in the DOOM automation challenge.

### Mission
Create a bot that is able to complete the first campaign level entirely autonomously.

<img src="/playthrough.gif?raw=true" width="600px">

### Features
* Parses IWADs
    * Extracts player starting position
    * Extracts LineDefs
    * Determines SideDef height to include two-sided LineDefs that are actually not walkable
* Utilizes A* pathfinding to reach the end of the level
* Utilizes RESTful DOOM's API to move, turn, shoot and use
* Uses JavaFX to visualize map, obstacles and calculated path
* Features a "listener" system to detect when an API action has finished
* Features a "tick" system to prevent overloading the API
* If no pistol ammo is available, the bot can most of the times speedrun the level without killing.

### Challenges
1. Parsing the IWAD
    * We used a third-party [IWAD Parser](https://github.com/neilo40/doomChallengeInfo/tree/master/wadParser) to do most of the heavy-lifting. However we had to figure out which LineDefs were not walkable even though they were two-sided, and thus implemented checking of `LineDef->SideDef->Sector->Height`.
2. Applying pathfinding
    * We used a third-party [A* implementation](http://www.cokeandcode.com/main/tutorials/path-finding/), but had to modify it to keep in mind both the future and the previous step. The biggest challenge was speed optimization, since due to the size of the map, it took more than a minute to complete. We achieved this by scaling LineDefs' coordinates down by a (big) percentage. This made the calculations instantaneous.
3. Prevent wall grinding/bumping
    * Due to the nature of A*, the path the bot followed was always stuck to the walls. To prevent this, we implemented an additional check in the A* algorithm that doesn't follow points that are too close to a wall.
4. No API callbacks
    * We couldn't know when the player had finished performing an action, and queuing or stopping actions was not possible. We created a special listener class to overcome this limitation.

### How to use
The project was created using IntelliJ IDEA and so uses its project structure. Generally, there are just 3 dependancies:
* JavaFX (only for visualization)
* Scala
* json-simple-1.1.1.jar

There are two entry points, `API/MainAutobot` and `Main/MainGUI`.

### Future work
* Refactor/clean up code
    * The bot is quite hacky in its current state, but this is the nature of a hackathon!
* Tweak some variables to improve performance
* Parse level end point instead of hardcoding it
* Fix aiming 1 pixel off and target not taking damage, but the bot wasting all ammo. Personal idea: enforce a turn command every 5 fired shots

Third-party libraries used:
* [IWAD Parser](https://github.com/neilo40/doomChallengeInfo/tree/master/wadParser)
* [A* Pathfinding](http://www.cokeandcode.com/main/tutorials/path-finding/)
