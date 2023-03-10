extends RigidBody2D

@onready var game = get_parent().get_parent().get_parent()

var speed = 600
var accel = 0

var direction: Vector2

@rpc func toggleVisibility(_isVisible): pass

func set_visibility(isVisible):
	visible = isVisible
	set_physics_process(isVisible)
	for player in game.player_ids:
		if player != -1:
			rpc_id(player, "toggleVisibility", isVisible)

func _ready():
	visible = false
	freeze = true

func _physics_process(delta):
	if visible:
		var collision = move_and_collide(direction.normalized() * speed * delta)
		if collision:
			collision.get_collider().shot()
			freeze = true
			set_visibility(false)
	
	game.send_bullet(self.name.to_int(), self.position.x, self.position.y, rotation_degrees)

func fire(pos, dir):
	linear_velocity = Vector2.ZERO
	position.x = pos.x
	position.y = pos.y
	
	direction = dir
	rotation = direction.angle()

	freeze = false
	game.send_bullet(self.name.to_int(), self.position.x, self.position.y, rotation_degrees)
	set_visibility(true)
