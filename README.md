# MarioKart

Server to Client messages:
player #        Sent at the beginning (before start), tells the client which number it is
moved x y ANG #     Sent to all clients when player # moves to x y and angle ANG
start # x y     Sent to all clients when the race starts with # of players and starting position

Client to Server messages:
moved x y ANG      Client moved to x y, is at angle ANG
joined          Sent when client joins server

To test out, must change IP addresses in Main.java
