# SpeetUp

If you have ever tried to organize a sports event with friends then you know how difficult and frustrating it can be to get a group of even 5 people to come to an agreenment. The greatest difficulty arises from trying to keep track of everyones preferences regarding what sport to play, when to play and where to play. It is simply impossible to organize an event with which everyone is happy with. This is precisely the problem SpeetUp was designed to solve, so next time you need to organize an event don't open WhatsApp, instead head over to SpeetUp. SpeetUp allows it's users to create lobbies where every member can express which sports they would like to play as well as where and when they can play. After all members of the lobby have locked their choices or if the timer has run out an event will be created using the majority option in each category. This way an event can be created with which everyone is satisfied by.


## Table of Contents

- [Technologies](#technologies)
- [High-Level Components](#components)
- [Launch & Deployment](#launch)
- [Roadmap](#roadmap)
- [Authors and Acknowledgments](#authors-and-acknowledgments)
- [License](#license)


## Technologies
* [Springboot Framework](https://spring.io/projects/spring-boot)
* [Google App Engine](https://cloud.google.com)
* [H2 Database Engine](https://www.h2database.com/html/main.html)
* [Java](https://www.java.com/en/)
* [Gradle](https://gradle.org)


## High-Level Components <a name="components"></a>
### Lobby
Lobbies are the soul of SpeetUp and model the synchronization and communication between users. It stores information on all the members that have joined the lobby, what choices they have selected (with help from a member component), what locations have been suggested as well as storing the lobby chat. The lobby is responsible for checking if it should close, either becuase all members of the lobby have locked their choices or because the timer has run out. In the case it should end, it is repsonsible for creating an event based on the majority choices of the members in the lobby.

### User
The users are the heart of SpeetUp and our User component is responsible for modelling the users. It stores all relevenat information regarding the user from their username to their avatar. It is also essential regarding user authentication and ensuring uniqueness within users as it stores information such as a users password. Additionally the User model is used as a base when creating a member object to represent a user in a lobby. The User component also keeps track of all previous and upcoming events that concern the user.

### Member
The Member component can be easily confused with the User component but is vastly different. The Member component models a user inside a lobby. It only requires a subset of the uers information, such as username and unlike the User, the Member component is responsible for storing all choices that the user has made in a lobby and thus allows all other members to see their choices. A Member component is created using a User and is used by a Lobby, making it the bridge between the two components allowing seamless interaction between users in a lobby.

##  Launch & Deployment <a name="launch"></a>

1. Download the server repo as a zip file from GitHub
2. Unzip the files
3. Open the directory in your IDE of choice

Run the following commands to build and run Gradle

#### Build
```
./gradlew build
```

#### Run
```
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`


## Roadmap

### Friending
The next step for SpeetUp would be adding a friending system where users can friend in the app and share lobby invites directly through SpeetUp. 

### Private Lobbies

Another feature that could be implemented is private lobbies, where only those users invited can join. Then you can create a lobby with just your friends by only inviting them to the lobby.

### Rating Users

An additional feature could be the ability to rate users after an event is done. This way others can see if a user is fun to play with, or if he is not so friendly.


## Authors and Acknowledgments

* **Suleman Ali Khan** - *Developer* - [SuleKhan](https://github.com/SuleKhan)
* **Omar Abo Hamida** - *Developer* - [oaboha](https://github.com/oaboha)
* **Patrick Schelling** - *Developer* - [patrick9051](https://github.com/patrick9051)
* **Yannic Laurent Meyer** - *Developer* - [cinnayre](https://github.com/cinnayre) (committed with [yanmey](https://github.com/yanmey))
* **Raffael Kummer** - *Developer* - [theraffael](https://github.com/theraffael)

* **Mete Polat** - *Scrum Master* - [polatmete](https://github.com/polatmete)

## License

MIT License

Copyright (c) [2023] [Suleman Ali Khan, Omar Abo Hamida, Patrick Schelling, Yannic Laurent Meyer, Raffael Kummer]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

