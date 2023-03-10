extends RigidBody2D

@onready var game = get_parent().get_parent().get_parent()

var speed = 500
var accel = 0

var direction: Vector2

@rpc func toggleVisibility(_isVisible): pass

func _ready():
	set_physics_process(false)
	visible = false

func _physics_process(delta):
	var collision = move_and_collide(direction.normalized() * speed * delta)
	if collision:
		set_physics_process(false)
		visible = false
		for player in game.player_ids:
			if player != -1:
				rpc_id(player, "toggleVisibility", false)
	game.send_bullet(self.name.to_int(), self.position.x, self.position.y, rotation_degrees)

func fire(pos, dir):
	position = pos
	direction = dir
	
	rotation = direction.angle()

	game.send_bullet(self.name.to_int(), self.position.x, self.position.y, rotation_degrees)
	
	set_physics_process(true)
	visible = true
	for player in game.player_ids:
		if player != -1:
			rpc_id(player, "toggleVisibility", true)
