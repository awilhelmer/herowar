Router = require 'router'

class GameRouter extends Router

	routes:
		'game'	           : 'authentication'
		'game2'	           : 'gamePreloader'
		'game3'	           : 'game'
		'game/defeat'      : 'defeat'
		'game/victory'     : 'victory'
		'game/interrupted' : 'interrupted'
		'game/settings'    : 'settings'
		'test'             : 'test'
		
return GameRouter