Preloader = require 'controllers/preloader'
PreloadCompletePacket = require 'network/packets/preloadCompletePacket'
events = require 'events'

class GamePreloader extends Preloader
	
	redirectTo: 'game3'
	
	# TODO: Removed hardcoded map and set via game token or what ever...
	initialize: (options) ->
		@options = _.extend { map: 1 }, options
		super @options
	
	finish: ->
		events.trigger 'send:packet', new PreloadCompletePacket()
		super()
	
return GamePreloader