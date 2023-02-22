extends Node2D

var score = [0, 0]

onready var map = get_node("map")

func _ready():
	# Load the map from a mapfile.
	map.loadMap("Dust2.tds")
	
