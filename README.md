# SpeetUp

If you have ever tried to organize a sports event with friends then you know how difficult and frustrating it can be to get a group of even 5 people to come to an agreenment. The greatest difficulty arises from trying to keep track of everyones preferences regarding what sport to play, when to play and where to play. It is simply impossible to organize an event with which everyone is happy with. This is precisely the problem SpeetUp was designed to solve, so next time you need to organize an event don't open WhatsApp, instead head over to SpeetUp. SpeetUp allows it's users to create lobbies where every member can express which sports they would like to play as well as where and when they can play. After all members of the lobby have locked their choices or if the timer has run out an event will be created using the majority option in each category. This way an event can be created with which everyone is satisfied by.

## Technologies
* Springboot Framework
* Google Cloud
* JPA


## High-Level Components
### Lobby
Lobbies are the soul of SpeetUp and model the synchronization and communication between users. It stores information on all the members that have joined the lobby, what choices they have selected (with help from a member component), what locations have been suggested as well as storing the lobby chat. The lobby is responsible for checking if it should close, either becuase all members of the lobby have locked their choices or because the timer has run out. In the case it should end, it is repsonsible for creating an event based on the majority choices of the members in the lobby.

### User
The users are the heart of SpeetUp and our User component is responsible for modelling the users. It stores all relevenat information regarding the user from their username to their avatar. It is also essential regarding user authentication and ensuring uniqueness within users as it stores information such as a users password. Additionally the User model is used as a base when creating a member object to represent a user in a lobby. The User component also keeps track of all previous and upcoming events that concern the user.

### Member
The Member component can be easily confused with the User component but is vastly different. The Member component models a user inside a lobby. It only requires a subset of the uers information, such as username and unlike the User, the Member component is responsible for storing all choices that the user has made in a lobby and thus allows all other members to see their choices. A Member component is created using a User and is used by a Lobby, making it the bridge between the two components allowing seamless interaction between users in a lobby.

##  Launch & Deployment

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


## Roadmap

1. Friending
2. 

## Authors and Acknowledgments

* **Suleman Ali Khan** - *Developer* - [SuleKhan](https://github.com/SuleKhan)
* **Omar Abo Hamida** - *Developer* - [SuleKhan](https://github.com/SuleKhan)
* **Patrick Schelling** - *Developer* - [SuleKhan](https://github.com/SuleKhan)
* **Yannic Laurent Meyer** - *Developer* - [SuleKhan](https://github.com/SuleKhan)
* **Raffael Kummer** - *Developer* - [SuleKhan](https://github.com/SuleKhan)

* **Mete** - *Scrum Master* - [SuleKhan](https://github.com/SuleKhan)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

