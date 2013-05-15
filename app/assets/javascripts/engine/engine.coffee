Variables = require 'variables'
SceneGraph = require 'scenegraph'
Eventbus = require 'eventbus'
Controls = require 'core/controls'
Views = require 'core/views'
events = require 'events'

class Engine 

	constructor: (@opts) ->
		@opts = @opts || {}
		@main = @opts.container
		@main = $ '#main' unless @main
		@data = @opts.data || {}

	init: ->
		console.log 'Engine starting...'
		position = @main.position()
		Variables.SCREEN_TOP = position.top
		Variables.SCREEN_LEFT = position.left
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@scenegraph = new SceneGraph @
		@views = new Views @
		@controls = new Controls @
		@clock = new THREE.Clock()
		@pause = false
		console.log "Engine started!"
		return
		
	start: ->
		if (@main == undefined)
			@init()
		console.log "Starting main loop..."
		@animate()
		return

	render: ->
		delta = @clock.getDelta()
		@scenegraph.update delta
		Eventbus.beforeRender.dispatch()
		@views.render @scenegraph.scene, @scenegraph.skyboxScene
		events.trigger 'engine:render', delta
		Eventbus.afterRender.dispatch()
		return
		
	animate: =>
		unless @pause
			@render()
			requestAnimationFrame(@animate)
		else
			console.log 'Main Loop stopped ...'
		return

	getData: (type, name) ->
		throw "Data of type #{type} with name #{name} not found" unless type of @data and name of @data[type]
		@data[type][name]

return Engine