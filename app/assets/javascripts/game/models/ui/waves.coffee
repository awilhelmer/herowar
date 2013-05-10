PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
log = require 'util/logger'

class Waves extends PacketModel

	type: [ PacketType.SERVER_WAVE_INIT, PacketType.SERVER_WAVE_UPDATE ]

	update: =>
		@calculateArrival() if @get 'eta'
		@unset 'arrival' if @get('eta') is 0 and @get 'arrival'

	calculateArrival: ->
		date = new Date()
		time = @get('eta') - date.getTime()
		arrival = @calculateCountdown time
		@set 'arrival', arrival
		console.log 'Arrival in', @get('arrival'), time, "=", @get('eta'), "-", date.getTime()
			
	calculateCountdown: (t) ->
		if t <= 0 then return ''
		# Calculate values
		t = Math.round t / 1000
		d = Math.floor(t / (60 * 60 * 24)) % 24
		h = Math.floor(t / (60 * 60)) % 24
		m = Math.floor(t / 60) % 60
		s = t % 60
		# Format values
		d = if d > 0 then "#{d}d " else ''
		h = if h < 10 then "0#{h}:" else if h isnt 0 then "#{h}:" else ''
		m = if m < 10 then "0#{m}" else m
		s = if s < 10 then "0#{s}" else s
		# Format output
		"#{d}#{h}#{m}:#{s}"

return Waves