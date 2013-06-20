PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'

class Waves extends PacketModel

	types: [ PacketType.SERVER_WAVE_INIT, PacketType.SERVER_WAVE_UPDATE ]

	update: =>
		if @get('_active') and not @has '_freeze'
			@calculateArrival() if @get 'eta'
			@unset 'arrival' if @get('eta') is 0 and @get 'arrival'

	onPacket: (packet) ->
		super packet
		@update()

	calculateArrival: ->
		arrival = @calculateCountdown @get('eta') - Date.now()
		@set 
			'arrival_short' : arrival[1]
			'arrival': arrival[0]
			
	calculateCountdown: (t) ->
		if t <= 0 then return ''
		# Calculate values
		t = Math.round t / 1000
		d = Math.floor(t / (60 * 60 * 24)) % 24
		h = Math.floor(t / (60 * 60)) % 24
		m = Math.floor(t / 60) % 60
		s = t % 60
		# Format values
		sv = s + m * 60
		sh = if sv < 10 then "0#{sv}" else sv
		d = if d > 0 then "#{d}d " else ''
		h = if h < 10 then "0#{h}:" else if h isnt 0 then "#{h}:" else ''
		m = if m < 10 then "0#{m}" else m
		s = if s < 10 then "0#{s}" else s
		# return output
		return [ "#{d}#{h}#{m}:#{s}", "#{sh}" ]

return Waves