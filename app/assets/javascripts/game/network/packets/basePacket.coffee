class BasePacket
	
	constructor: (@type, params) ->
		unless @type then throw 'Property type should be set'
		@params = _.extend { type: @type, createdTime: new Date().getTime() }, params
	
	get: ->
		JSON.stringify @params
	
return BasePacket