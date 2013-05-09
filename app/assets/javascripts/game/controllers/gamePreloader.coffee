Preloader = require 'controllers/preloader'
PreloadUpdatePacket = require 'network/packets/preloadUpdatePacket'
PacketType = require 'network/packets/packetType'
log = require 'util/logger'
events = require 'events'
db = require 'database'

class GamePreloader extends Preloader
	
	redirectTo: 'game3'
	
	initialize: (options) ->
		preload = db.get 'ui/preload'
		@options = _.extend preload.attributes, options
		@bindEvents()
		super @options
		@progress.started = false

	bindEvents: ->
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_START}", @onGameStart, @

	afterUpdateState: ->
		events.trigger 'send:packet', new PreloadUpdatePacket Math.round @percentage if @percentage > 0

	onGameStart: ->
		log.debug 'Server game has been started'
		@progress.started = true

return GamePreloader