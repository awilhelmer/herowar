db = require 'database'

class RTSControls
	
	constructor: ->
		_.extend @, Backbone.Events
		@input = db.get 'input'
		@bindEvents()
	
	bindEvents: ->
		@listenTo @input, 'mouse:wheel', @onMouseWheel
	
	update: ->
		console.log 'RTSControls update()'
	
	onMouseWheel: (event) ->
		console.log 'RTSControls onMouseWheel()', event, @		
	
return RTSControls