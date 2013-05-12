AddObject = require 'tools/addObject'
events = require 'events'
db = require 'database'

class AddTowerTool extends AddObject
	
	internalId: 200
	
	bindEvents: ->
		events.on 'select:tower', @onSelectTower, @
	
	onSelectTower: (id) ->
		name = "tower#{id}"
		data = db.data().geometries[name]
		console.log 'Found geometry', data
		@tool.set
			'currentObjectId' 	: @internalId++
			'currentObjectName'	: name
		@onLoadGeometry data[0], data[1]

	addMesh: ->
		mesh = @tool.get('currentObject')
		@app.engine.scenegraph.addStaticObject mesh, @tool.get('currentObjectId')

return AddTowerTool