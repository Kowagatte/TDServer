extends Panel

@onready var clients = get_parent().get_parent().get_node("Clients")

# Called when the node enters the scene tree for the first time.
func _ready():
	$games.pressed.connect(_print_games)
	
func _print_games():
	print(clients.games)
