extends Node2D

@onready var server = get_parent()
@onready var requests = get_node("requests")
var game_obj = preload("res://nodes/game.tscn")

@rpc("any_peer") func joinGame(gameID):
	var id = multiplayer.get_remote_sender_id()
	if has_node(gameID):
		var game = get_node(gameID)
		if not game.is_full():
			server.rpc_id(id, "response", 200, "Game Found!")
			game.add_player(id)
			server.get_node("Clients").link_game(id, gameID)
		else:
			server.rpc_id(id, "response", 400, "Game is already full.")
	else:
		server.rpc_id(id, "response", 400, "Game does not exist.")

# Should generate a unique ID with no collisions.
@rpc("any_peer") func createGame():
	var id = multiplayer.get_remote_sender_id()
	var httpRequest = HTTPRequest.new()
	requests.add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"generateGame").bind(httpRequest, id))
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/generateID/" % self.get_parent().api
	httpRequest.request(url, headers, HTTPClient.METHOD_GET)


# Creates a game instance and adds it to the tree
func generateGame(_result, response, _headers, body, req, playerOneID):
	req.call_deferred("free")
	if response == 200:
		# Unique ID of the Treated game
		var id = JSON.parse_string(body.get_string_from_utf8())["id"]

		# Create game object on tree
		var game_inst = game_obj.instantiate()
		game_inst.name = id
		add_child(game_inst)
		
		game_inst.add_player(playerOneID)
		
		get_parent().get_node("Clients").link_game(playerOneID, id)
		game_inst.rpc_id(playerOneID, "sendState", "waitingForPlayer", null)
		

@rpc func gameJoined(_gameID): pass
