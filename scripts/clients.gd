extends Node2D

# Stores the email associated to the peer_id
# Example structure: var id_to_email = {1: "test@test.co"}
var id_to_email = {}
# Stores the peer_ids (Multiple) associated with the email
# Example structure: var email_to_id = {"test@test.co": [1]}
var email_to_id = {}
# Stores the games (Multiple) associated with the email
# Example structure: var games = {"test@test.co": {'012345678': 1}}
var games = {}

# Checks if there are any ids connected.
func is_anyone_connected():
	return not id_to_email.is_empty()

# Adds the association between peer_id and email.
# Needs to manage multiple dictionaries.
# Would be nice to have a bi-directional table...
func add_player(id, email):
	id_to_email[id] = email
	
	if email_to_id.has(email):
		email_to_id[email].append(id)
	else:
		email_to_id[email] = [id]
	
	if not games.has(email):
		games[email] = {}
	
	update_counter()

# Used to link the game to the account associated with the given peer_id
# Since peers can leave at any time, there is some issues that could arise here.
# Cannot currently unlink a game. (TDServer #15)
func link_game(id, game):
	if id_to_email.has(id):
		games[id_to_email[id]][game] = id
	else:
		print("GAME ", game, " : Issue retrieving players account!")

# Removes peer_id
func remove_player(id):
	if id_to_email.has(id):
		var email = id_to_email[id]
		id_to_email.erase(id)
		email_to_id[email].erase(id)
		if email_to_id[email].is_empty():
			email_to_id.erase(email)
	
	update_counter()

# Returns number of logged in sessions. This includes users logged in on
# 	on multiple devices/clients.
func size():
	return id_to_email.size()

# Updates the counter UI component
func update_counter():
	var label = get_parent().get_node("UI/Panel/client-count") as Label
	label.text = str(size())
