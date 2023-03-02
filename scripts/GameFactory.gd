extends Node2D

onready var requests = get_node("requests")
var game_obj = preload("res://nodes/game.tscn")


# Should generate a unique ID with no collisions.
remote func createGame():
	var id = get_tree().get_rpc_sender_id()
	var httpRequest = HTTPRequest.new()
	requests.add_child(httpRequest)
	httpRequest.connect("request_completed", self, "generateGame", [httpRequest, id, -1])
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
		#Send game creation notification to both players
		get_parent().rpc_id(playerOneID, "gameCreated", id)
		get_parent().rpc_id(playerTwoID, "gameCreated", id)
