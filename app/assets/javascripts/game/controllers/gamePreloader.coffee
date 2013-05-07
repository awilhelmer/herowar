Preloader = require 'controllers/preloader'

class GamePreloader extends Preloader
	
	redirectTo: 'game3'
	
	# TODO: Removed hardcoded map and set via game token or what ever...
	initialize: (options) ->
		@options = _.extend { map: 1 }, options
		super @options
	
return GamePreloader