extends Node2D

var score = [0, 0]
var players = [-1, -1]

var playerOneID = -1
var playerTwoID = -1

onready var map = get_node("map")

func _ready():
	# Load the map from a mapfile.
	map.loadMap("res://resources/dust2.json")
	
