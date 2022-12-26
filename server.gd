extends Node2D

var port = 9696
var max_players = 20
var passwords = "res://passwords.json"
var secret: String
var api = "http://api.damocles.ca"

onready var auth = get_node("Clients")

func _ready():
	
	# This might break
	var file = File.new()
	if file.file_exists(passwords, File.READ):
		file.open(passwords, File.READ)
		var data = parse_json(file.get_as_text())
		secret = data["secret"]
		file.close()
	file.free()
	
	var peer = NetworkedMultiplayerENet.new()
	peer.create_server(port, max_players)
	get_tree().network_peer = peer
	
	var _c = get_tree().connect("network_peer_connected", self, "_player_connected")
	_c = get_tree().connect("network_peer_disconnected", self, "_player_disconnected")


func _player_connected(id):
	rpc_id(id, "connectionConfirmation")
	print("Player with ID (", id, ") Connected.")


func _player_disconnected(id):
	auth.remove_player(id)
	print("Player with ID (", id, ") Disconnected.")

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


func login_callback(result, response, headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", response, body.get_string_from_utf8())
	if response == 200:
		# Probably will not work, need to respond with actuall account info.
		auth.add_player(id, JSON.parse(body.get_string_from_utf8()).result)
		rpc_id(id, "switchScenes", "Game")


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


func createAccount_callback(result, response, headers, body, id, req):
	req.call_deferred("free")
	rpc_id(id, "response", response, body.get_string_from_utf8())

func createGame(playerOne, playerTwo):
	pass
