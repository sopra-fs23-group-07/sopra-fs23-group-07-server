# SpeetUp

If you have ever tried to organize a sports event with friends then you know how difficult and frustrating it can be to get a group of even 5 people to come to an agreenment. The greatest difficulty arises from trying to keep track of everyones preferences regarding what sport to play, when to play and where to play. It is simply impossible to organize an event with which everyone is happy with. This is precisely the problem SpeetUp was designed to solve, so next time you need to organize an event don't open WhatsApp, instead head over to SpeetUp. SpeetUp allows it's users to create lobbies where every member can express which sports they would like to play as well as where and when they can play. After all members of the lobby have locked their choices or if the timer has run out an event will be created using the majority option in each category. This way an event can be created with which everyone is satisfied by.

## Technologies
* Springboot Framework
* Google Cloud
* JPA


## High-Level Components
### Lobby
The lobby is the soul of SpeetUp. It is where users can work together to create an event that satisfies everyone. The lobby component is responsible for keeping track of all members who have joined as well as all the chioces they have selected. The lobby is also responsible for creating an event based on the final choices. It does this either after all members have locked their choices or if the lobby timer has run out.

### User
The users are the heart of SpeetUp. The user component is responsible for storing all relevant information for a user. It is used for authentication when logging in and is used to create a member component when joining a lobby.

### Event
Events are responsible for storing information for all users to see. It also allows users to join and leave an event as they please. Additionally users are able to view which upcoming events they have joined as well as all previous events they were a part of.

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

