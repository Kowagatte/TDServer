extends Node2D

var clients = {}

func _ready():
	pass # Replace with function body.

func is_anyone_connected():
	return not clients.is_empty()

func add_player(id, player):
	clients[id] = player
	update_counter()

func remove_player(id):
	if clients.has(id):
		clients.erase(id)
	update_counter()

func size():
	return clients.size()

func update_counter():
	var label = get_parent().get_node("UI/Panel/client-count") as Label
	label.text = String.num_int64(size())
