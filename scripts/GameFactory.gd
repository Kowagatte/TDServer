extends Node2D

onready var requests = get_node("requests")
var game_obj = preload("res://nodes/game.tscn")


# Should generate a unique ID with no collisions.
func createGame(playerOneID, playerTwoID):
	var httpRequest = HTTPRequest.new()
	requests.add_child(httpRequest)
	httpRequest.connect("request_completed", self, "generateGame", [httpRequest, playerOneID, playerTwoID])
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/generateID/" % self.get_parent().api
	httpRequest.request(url, headers, false, HTTPClient.METHOD_GET)


# Creates a game instance and adds it to the tree
func generateGame(_result, response, _headers, body, req, playerOneID, playerTwoID):
	req.call_deferred("free")
	if response == 200:
		# Unique ID of the created game
		var id = JSON.parse(body.get_string_from_utf8()).result["id"]

		# Create game object on tree
		var game_inst = game_obj.instance()
		game_inst.name = id
		game_inst.player_ids[0] = playerOneID
		game_inst.player_ids[1] = playerTwoID
		add_child(game_inst)
		get_parent().gameCreated(playerOneID, id)
		get_parent().gameCreated(playerTwoID, id)
	
