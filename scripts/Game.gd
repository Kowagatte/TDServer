extends Node2D

var started = false
var paused = true
var is_ready = [false, false]
var score = [0, 0]
var players = [-1, -1]

onready var map = get_node("map")

# Checks if the id (rpc sender) is a player in the game.
func is_playing(id):
	return id in players

# Entry point to control a player inside this game instance.
# Don't know how I want to implement this yet..
remote func control_player():
	var sender = get_tree().get_rpc_sender_id()
	if is_playing(sender):
		pass

# Ready up sequence, This is used to start the game..
remote func ready_up():
	var sender = get_tree().get_rpc_sender_id()
	# Check if the sender is in the current game.
	if is_playing(sender):
		# Get if player one or player two.
		var player_num = players.find(sender)
		# Should technically never be '-1', but.... who knows.
		if(player_num != -1):
			is_ready[player_num] = true


# Main loop of Game Object..
func _process(_delta):

	# This utilizes the read_up sequence to start the game.
	# Added the started variable purely because monitoring based on pause state would retrigger
	#    a start every time the game is paused for unrelated reasons.
	if !started:
		if is_ready:
			started = true
			paused = false

func _ready():
	# Load the map from a mapfile.
	map.loadMap("res://resources/dust2.json")
	
