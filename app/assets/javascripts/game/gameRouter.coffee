Router = require 'router'

class GameRouter extends Router

	routes:
		'game'	           : 'authentication'
		'game2'	           : 'gamePreloader'
		'game3'	           : 'game'
		'game/defeat'      : 'defeat'
		'game/victory'     : 'victory'
		'game/preferences' : 'preferences'
		'test'             : 'test'
		
return GameRouter