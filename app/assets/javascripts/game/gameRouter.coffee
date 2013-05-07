Router = require 'router'

class GameRouter extends Router

	routes:
		'game'	: 'authentication'
		'game2'	: 'gamePreloader'
		'game3'	: 'game'

return GameRouter