extends CharacterBody2D

@onready var game = get_parent().get_parent().get_parent()
@onready var map = get_parent().get_parent()

var collectedCoins = []

# Move speed for the given player node.
var move_speed = 192
# Last direction seen, this is to orientate AFK nodes.
var last_velocity = Vector2(1, -1)
# Orientation of the node
var direction = Vector2(0, 0)
# Map of angles for proper rotation.
var rotation_map = [[270, 225, 180], [315, 0, 135], [0, 45, 90]]

# Called when the node enters the scene tree for the first time.
func _ready():
	died()

func shot():
	died()

func _physics_process(_delta):
	if game.started and not game.stopped:
		if direction != Vector2.ZERO:
			last_velocity = direction
			rotation_degrees = rotation_map[direction.x+1][direction.y+1]
		set_velocity(direction.normalized() * move_speed)
		move_and_slide()
	
	# Send position updates to players
	game.send_location(self.name.to_int(), self.position.x, self.position.y, rotation_degrees)

func move(x, y):
	direction.x = x
	direction.y = y

@rpc("any_peer", "unreliable_ordered")
func try_shoot():
	if game.started and not game.stopped:
		var bullet = game.get_node("map/bullets/%s" % self.name)
		if not bullet.visible:
			bullet.fire(position, last_velocity)

@rpc("any_peer", "unreliable") func control_player(x, y):
	move(x, y)

func died():
	for coin in collectedCoins:
		coin.release(self)
	collectedCoins.clear()
	for player in game.player_ids:
		if player != -1:
			game.rpc_id(player, "updateScore", game.score)
	
	position = map.spawns[game.player_ids.find(self.name.to_int())].position
