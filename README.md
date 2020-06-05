# ![Icon](https://imgur.com/GpaOt95.png) TDServer
#### This software is programmed and maintained by:
+ --=[ Kowagatte | Nicholas Noel Ryan ]=--
+ --=[ Aikanwha | Dalton Wettlaufer ]=--

This is the main server application for the game TopDownShooter aka TDS.
A free to play online game programmed in ~~java~~ kotlin.

***
<p align=center><b><font size="+1"><a href="https://github.com/Kowagatte/TDS-Server/releases">Working Builds</a></p>

***

### Milestones
* ~~Allow clients to connect to the server.~~
* ~~Store Accounts in a Database~~
* Connections can log in to accounts.
* Accounts can be created
* Responses can be sent to the client from the server.
* Games can be stored and managed
* MMR Implementation
* Players can be assigned into games against players of equal skill


### Post-Beta Milestones
* Casual Games
* Friends List
* Leaderboards
* Live game viewer
* Match History

***
### Design

#### Networking:
```
Server has a Socket that client sockets can connect too, this is stored
in the Server Object in Server.kt
Client Sockets accepted through the Server socket are accepted in the ClientGate.
After being accepted they are assigned an EstablishedConnection Object and stored
in a listOfEstablished connections in the Server Object.
```
#### Assigning Accounts to EstablishedConnections
```
An EstablishedConnection cannot interact with the server until it is assigned
an Account. It will be assigned an account when it sends a login packet
containing a username and password that matches the credentials of an Account
stored in the database.
```

#### Creating an Account
```

```
