AddObject = require 'tools/addObject'
events = require 'events'
db = require 'database'

class AddTowerTool extends AddObject
	
	bindEvents: ->
		events.on 'select:tower', @onSelectTower, @
	
	onSelectTower: (id) ->
		data = db.data().geometries["tower#{id}"]
		console.log 'Found geometry', data
	
return AddTowerTool