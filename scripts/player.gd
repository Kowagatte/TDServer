extends CharacterBody2D

# Move speed for the given player node.
var move_speed = 192
# Last direction seen, this is to orientate AFK nodes.
var last_velocity = Vector2.ZERO
# Orientation of the node
var direction = Vector2(0, 0)
# Map of angles for proper rotation.
var rotation_map = [[270, 225, 180], [315, 0, 135], [0, 45, 90]]

# Called when the node enters the scene tree for the first time.
func _ready():
	position.x = 72
	position.y = 72

func _physics_process(_delta):
	if velocity != Vector2.ZERO:
		last_velocity = velocity
		rotation_degrees = rotation_map[velocity.x+1][velocity.y+1]
	set_velocity(velocity.normalized() * move_speed)
	move_and_slide()
	var _m = velocity
	rpc_id(int(self.name), "updatePos", self.position.x, self.position.y, rotation_degrees)

func move(x, y):
	velocity.x = x
	velocity.y = y

# Entry point to control a player inside this game instance.
# Don't know how I want to implement this yet..
@rpc("any_peer") func control_player(x, y):
	move(x, y)
