BasePanel = require 'ui/panel/basePanel'
	
class TerrainPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-terrain'

	initialize: ->
		console.log 'Initialize editor terrain properties'
		super()

	bindEvents: ->
		@$container.on 'keyup', 'input[name="width"],input[name="height"]', @changeTerrainSize
		
	changeTerrainSize: =>
		width = @$container.find('input[name="width"]').val()
		height = @$container.find('input[name="height"]').val()
		@app.scenegraph().setMap @app.scenegraph().createDefaultMap width, height
		@app.render()

return TerrainPropertiesPanel