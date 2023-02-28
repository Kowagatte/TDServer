extends Node2D

var port = 9696
var max_players = 20
var passwords = "res://passwords.json"
var secret: String
var api = "http://api.damocles.ca:8080"

onready var auth = get_node("Clients")
onready var gamef = get_node("Games")

func _ready():
	
	# Gets secret passphrase used to request controlled functionality from API
	var file = File.new()
	if file.file_exists(passwords):
		file.open(passwords, File.READ)
		var data = parse_json(file.get_as_text())
		secret = data["secret"]
		file.close()
	
	# Instantiate server
	var peer = NetworkedMultiplayerENet.new()
	peer.create_server(port, max_players)
	get_tree().network_peer = peer
	
	var _c = get_tree().connect("network_peer_connected", self, "_player_connected")
	_c = get_tree().connect("network_peer_disconnected", self, "_player_disconnected")

# --------------------------------------------------------------------------------------------------

# Networked signals

func _player_connected(id):
	rpc_id(id, "connectionConfirmation")
	print("Player with ID (", id, ") Connected.")


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
remote func login(email, password):
	var id = get_tree().get_rpc_sender_id()
	print("Login Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed", self, "login_callback", [id, httpRequest])
	var query = JSON.print({"secret": secret, "email": email, "password": password})
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/compare/" % api
	httpRequest.request(url, headers, false, HTTPClient.METHOD_POST, query)


func login_callback(_result, response, _headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", response, body.get_string_from_utf8())
	if response == 200:
		# Probably will not work, need to respond with actuall account info.
		auth.add_player(id, JSON.parse(body.get_string_from_utf8()).result)
		#rpc_id(id, "switchScenes", "Game")

# --------------------------------------------------------------------------------------------------

# Account Info Remote Call

remote func account(username):
	var id = get_tree().get_rpc_sender_id()
	print("Account Info Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed", self, "account_callback", [id, httpRequest])
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/account/%s" % [api, username]
	httpRequest.request(url, headers, false, HTTPClient.METHOD_GET)

func account_callback(_result, response, _headers, body, _id, req):
	req.call_deferred("free")
	if response == 200:
		print(body.get_string_from_utf8())

# --------------------------------------------------------------------------------------------------

# Create Account Remote Call

remote func createAccount(email, username, password):
	var id = get_tree().get_rpc_sender_id()
	print("Account Creation Request from ", id)
	var httpRequest = HTTPRequest.new()
	add_child(httpRequest)
	httpRequest.connect("request_completed", self, "createAccount_callback", [id, httpRequest])
	var query = JSON.print({"secret": secret, "email": email, "username": username, "password": password})
	var headers = ["Content-Type: application/json"]
	var url = "%s/tds/createAccount/" % api
	httpRequest.request(url, headers, false, HTTPClient.METHOD_POST, query)


func createAccount_callback(_result, response, _headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", response, body.get_string_from_utf8())

# --------------------------------------------------------------------------------------------------
