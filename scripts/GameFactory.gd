extends Node2D

# Should generate a unique ID with no collisions.
# This functionality should probably just make a call to Butterfly?
func generateID():
    return 0

func createGame():
    # Unique ID of the created game
    var id = generateID()
    