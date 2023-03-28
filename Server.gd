extends Node2D

@onready var auth = get_node("Clients")

var port = 9696
var max_peers = 20

var api = "https://api.damocles.ca"
var passwords = "res://passwords.json"
var secret: String

var enet = ENetMultiplayerPeer.new()
var multiplayer_api: MultiplayerAPI

func loadSecret():
	var file = FileAccess.open(passwords, FileAccess.READ)
	if FileAccess.file_exists(passwords):
		var test_json_conv = JSON.new()
		test_json_conv.parse(file.get_as_text())
		var data = test_json_conv.get_data()
		secret = data["secret"]
		file.close()
	else:
		print("Password file missing, server will misfunction!")

# Called when the node enters the scene tree for the first time.
func _ready():
	
	loadSecret()
	
	enet.peer_connected.connect(_peer_connected)
	enet.peer_disconnected.connect(_peer_disconnected)
	
	multiplayer_api = MultiplayerAPI.create_default_interface()
	enet.create_server(port, max_peers)
	
	get_tree().set_multiplayer(multiplayer_api, self.get_path())
	multiplayer_api.multiplayer_peer = enet
	
	print("Server started on port ", port)

# ------------------------------------------------------------------------------------------------

# HTTP Requests

# Sent when a user attempts to login.
# A request is sent to the internal API containing the credentials.
# Recieving a response runs the login_callback functions
# 	Response 200 - Success
# 	Anything else - Failure
@rpc("any_peer") func login(email, password):
	var id = multiplayer.get_remote_sender_id()
	print("Login Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"login_callback").bind(id, httpRequest, email))
	var query = JSON.stringify({"secret": secret, "email": email, "password": password})
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/compare/" % api
	httpRequest.request(url, headers, HTTPClient.METHOD_POST, query)


func login_callback(_result, rc, _headers, body, id, req, email):
	req.call_deferred("free")
	rpc_id(id, "response", rc, body.get_string_from_utf8())
	if rc == 200:
		auth.add_player(id, email)
		rpc_id(id, "switchScenes", "CreateGameScreen")

# -----

@rpc("any_peer") func account(username):
	var id = multiplayer.get_remote_sender_id()
	print("Account Info Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"account_callback").bind(id, httpRequest))
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/account/%s" % [api, username]
	httpRequest.request(url, headers, HTTPClient.METHOD_GET)

func account_callback(_result, rc, _headers, body, _id, req):
	req.call_deferred("free")
	if rc == 200:
		print(body.get_string_from_utf8())

# -----

@rpc("any_peer") func createAccount(email, username, password):
	var id = multiplayer.get_remote_sender_id()
	print("Account Creation Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed",Callable(self,"createAccount_callback").bind(id, httpRequest))
	var query = JSON.stringify({"secret": secret, "email": email, "username": username, "password": password})
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/createAccount/" % api
	httpRequest.request(url, headers, HTTPClient.METHOD_POST, query)


func createAccount_callback(_result, rc, _headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", rc, body.get_string_from_utf8())

# ------------------------------------------------------------------------------------------------

# Signal Functions

func _peer_connected(id):
	await get_tree().create_timer(1).timeout
	print("Client connected with id ", id)
	rpc_id(id, "connectionConfirmation")

func _peer_disconnected(id):
	print("Client with id ", id, " disconnected.")
	auth.remove_player(id)
	

# ------------------------------------------------------------------------------------------------

# RPC Templates

@rpc func ping(): pass
@rpc func response(_repsonse, _message): pass
@rpc func switchScenes(_scene): pass
@rpc func connectionConfirmation(): pass
