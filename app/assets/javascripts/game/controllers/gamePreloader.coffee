Preloader = require 'controllers/preloader'
PreloadUpdatePacket = require 'network/packets/preloadUpdatePacket'
events = require 'events'
db = require 'database'

class GamePreloader extends Preloader
	
	redirectTo: 'game3'
	
	initialize: (options) ->
		preload = db.get 'ui/preload'
		@options = _.extend preload.attributes, options
		super @options
	
	afterUpdateState: ->
		events.trigger 'send:packet', new PreloadUpdatePacket Math.round @percentage if @percentage > 0
	
return GamePreloader