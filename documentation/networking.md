# Networking
Networking breakdown for TDS

## Packets
Protobuf classes that contain data to be sent between server and client.

### __Client Packets__

#### <u>What is a Client Packet?</u>
A client packet is a packet owned by the client,
it is used to send client requested changes to the server.

Such as;
 * Login Requests
 * Password Change Requests
 * Input

### __Server Packets__

#### <u>What is a Server Packet?</u>
A server packet is a packet owned by the server,
it is used to send server changes to the client.

Such as;
 * Game/Score Updates
 * Response to client packets
 * Server status

### __Packet Transfer__
Packets are converted to Byte Arrays and sent over the Socket.
