extends SubViewport

var started = false
var stopped = false
var is_ready = [false, false]
var score = [0, 0]
var max_score = 0
var player_ids = [-1, -1]

@onready var server = get_parent().get_parent()
@onready var map = get_node("map")
@onready var players = get_node("map/players")
@onready var bullets = get_node("map/bullets")

var player_node = preload("res://nodes/player.tscn")
var bullet_node = preload("res://nodes/bullet.tscn")

# ------------------------------------------------------------------------------------------------

# Checks if the id is a player in the game.
func is_playing(id):
	return id in player_ids

func is_full():
	return player_ids.count(-1) == 0

func send_location(id, x, y, rot):
	for player in player_ids:
		if player != -1 and server.auth.id_to_email.has(player):
			rpc_id(player, "update_pos", id, x, y, rot)

func send_bullet(id, x, y, rot):
	for player in player_ids:
		if player != -1 and server.auth.id_to_email.has(player):
			rpc_id(player, "update_bullet", id, x, y, rot)

# Ready up sequence, This is used to start the game..
@rpc("any_peer") func ready_up():
	var sender = multiplayer.get_remote_sender_id()
	# Check if the sender is in the current game.
	if is_playing(sender):
		# Get if player one or player two.
		var player_num = player_ids.find(sender)
		# Should technically never be '-1', but.... who knows.
		if(player_num != -1):
			is_ready[player_num] = true

func add_player(id):
	if not is_full():
		var index = player_ids.find(-1)
		player_ids[index] = id
		var player = player_node.instantiate()
		player.collision_layer = 0b00000000000000000001
		player.name = String.num_int64(id)
		players.add_child(player)
		
		var bullet = bullet_node.instantiate()
		bullet.name = str(id)
		bullet.collision_mask = 0b00000000000000000110
		bullets.add_child(bullet)
		
		rpc_id(id, "spawn_enemy", player_ids[1-index])
		rpc_id(player_ids[1-index], "spawn_enemy", id)
		
		rpc_id(id, "sendState", "gameStarting", null)
		rpc_id(player_ids[1-index], "sendState", "gameStarting", null)
		
	else:
		get_parent().get_parent().rpc_id(id, "response", 400, "Game is already full.")

func gameOver():
	await get_tree().create_timer(5).timeout
	get_parent().remove_child(self)
	call_deferred("free")

# ------------------------------------------------------------------------------------------------

# Godot functions

func _process(_delta):
	
	if not stopped:
		if score.any(func(number): return number == max_score):
			stopped = true
			for player in player_ids:
				rpc_id(player, "sendState", "ended", null)
			gameOver()
	
	# This utilizes the read_up sequence to start the game.
	# Added the started variable purely because monitoring based on pause state would retrigger
	#    a start every time the game is paused for unrelated reasons.
	if !started:
		if is_ready[0] and is_ready[1]:
			started = true

func _ready():
	# Load the map from a mapfile.
	map.loadMap("res://resources/dust2.json")

	var bullet = bullet_node.instantiate()
	bullet.name = str(player_ids[0])
	bullet.collision_mask = 0b00000000000000000101
	bullets.add_child(bullet)

	var p1 = player_node.instantiate()
	p1.name = String.num_int64(player_ids[0])
	p1.collision_layer = 0b00000000000000000010
	players.add_child(p1)
	

# ------------------------------------------------------------------------------------------------

# RPC Templates

@rpc func update_pos(_player, _x, _y, _rot): pass

@rpc func update_bullet(_id, _x, _y, _rot): pass

@rpc func spawn_enemy(_id): pass

@rpc func sendState(_state, _content): pass

@rpc func updateScore(_score): pass
