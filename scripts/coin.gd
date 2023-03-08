extends Sprite2D

@rpc func toggleVisiblity(_isvisible): pass

@onready var area = get_node("area") as Area2D
@onready var game = get_parent().get_parent()

# Called when the node enters the scene tree for the first time.
func _ready():
	area.area_entered.connect(collided)

func release(player):
	rpc_id(player.name.to_int(), "toggleVisiblity", true)
	visible = true
	var playerNum = game.player_ids.find(player.name.to_int())
	game.score[playerNum] -= 1
	game.rpc_id(player.name.to_int(), "updateScore", game.score)


func collided(collider):
	if visible:
		var playerNum = game.player_ids.find(collider.get_parent().name.to_int())
		game.score[playerNum] += 1
		collider.get_parent().collectedCoins.append(self)
		print(collider.get_parent().collectedCoins)
		for player in game.player_ids:
			game.rpc_id(player, "updateScore", game.score)
			rpc_id(player, "toggleVisiblity", false)
		visible = false
