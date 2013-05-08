Preloader = require 'controllers/preloader'
PreloadCompletePacket = require 'network/packets/preloadCompletePacket'
events = require 'events'
db = require 'database'

class GamePreloader extends Preloader
	
	redirectTo: 'game3'
	
	initialize: (options) ->
		preload = db.get 'ui/preload'
		@options = _.extend preload.attributes, options
		super @options
	
	finish: ->
		events.trigger 'send:packet', new PreloadCompletePacket()
		super()
	
return GamePreloader