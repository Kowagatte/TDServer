extends Node2D

var port = 9696
var max_players = 20
var passwords = "res://passwords.json"
var secret: String
var api = "http://api.damocles.ca:8080"

@onready var auth = get_node("Clients")
@onready var gamef = get_node("Games")

var multiplayer_peer = ENetMultiplayerPeer.new()

func _ready():
	
	# Gets secret passphrase used to request controlled functionality from API
	var file = FileAccess.open(passwords, FileAccess.READ)
	if FileAccess.file_exists(passwords):
		var test_json_conv = JSON.new()
		test_json_conv.parse(file.get_as_text())
		var data = test_json_conv.get_data()
		secret = data["secret"]
		file.close()
	else:
		print("Password file missing, server will misfunction!")
	
	# Instantiate server
	multiplayer_peer.create_server(port, max_players)
	multiplayer_peer.peer_connected.connect(_player_connected)
	multiplayer_peer.peer_disconnected.connect(_player_disconnected)
	multiplayer.multiplayer_peer = multiplayer_peer
	
	print("Server Started on port ", port)

# --------------------------------------------------------------------------------------------------

# Networked signals

func _player_connected(id):
	await get_tree().create_timer(1).timeout
	rpc_id(id, connectionConfirmation)
	print("Player with ID (", id, ") Connected.")

@rpc
func connectionConfirmation():
	pass
	#print("being called")


func _player_disconnected(id):
	auth.remove_player(id)
	print("Player with ID (", id, ") Disconnected.")

# --------------------------------------------------------------------------------------------------

# Login Remote Call

# Sent when a user attempts to login.
# A request is sent to the internal API containing the credentials.
# Recieving a response runs the login_callback functions
# 	Response 200 - Success
# 	Anything else - Failure
@rpc("any_peer") func login(email, password):
	var id = get_tree().get_remote_sender_id()
	print("Login Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"login_callback").bind(id, httpRequest))
	var query = JSON.stringify({"secret": secret, "email": email, "password": password})
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/compare/" % api
	httpRequest.request(url, headers, HTTPClient.METHOD_POST, query)


func login_callback(_result, response, _headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", response, body.get_string_from_utf8())
	if response == 200:
		# Probably will not work, need to respond with actuall account info.
		auth.add_player(id, JSON.parse_string(body.get_string_from_utf8()))
		rpc_id(id, "switchScenes", "CreateGameScreen")

# --------------------------------------------------------------------------------------------------

# Account Info Remote Call

@rpc("any_peer") func account(username):
	var id = get_tree().get_remote_sender_id()
	print("Account Info Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"account_callback").bind(id, httpRequest))
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/account/%s" % [api, username]
	httpRequest.request(url, headers, HTTPClient.METHOD_GET)

func account_callback(_result, response, _headers, body, _id, req):
	req.call_deferred("free")
	if response == 200:
		print(body.get_string_from_utf8())

# --------------------------------------------------------------------------------------------------

# Create Account Remote Call

@rpc("any_peer") func createAccount(email, username, password):
	var id = get_tree().get_remote_sender_id()
	print("Account Creation Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"createAccount_callback").bind(id, httpRequest))
	var query = JSON.stringify({"secret": secret, "email": email, "username": username, "password": password})
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/createAccount/" % api
	httpRequest.request(url, headers, HTTPClient.METHOD_POST, query)


func createAccount_callback(_result, response, _headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", response, body.get_string_from_utf8())

# --------------------------------------------------------------------------------------------------
