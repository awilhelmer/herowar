SelectorPlane = require 'tools/selectorPlane'
Waypoint = require 'models/waypoint'
log = require 'util/logger'
db = require 'database'

class AddWaypoint extends SelectorPlane
	
	color: 0x0000FF
	
	initialize: ->
		@nextId = 1
		super()
	
	onMouseUp: (event) ->
		@createWaypoint() if event.which is 1 and @isVisible
		super event
	
	createWaypoint: ->
		pos = @calculatePossiblePosition @lastPosition
		id = @nextId++
		waypoint = new Waypoint()
		waypoint.set
			'id' 				: id
			'name'			: "Waypoint #{id} - #{pos.x} x #{pos.y} x #{pos.z}"
			'position'	: pos
		log.info "Created new waypoint at #{pos.x} x #{pos.y} x #{pos.z}"
		col = db.get 'waypoints'
		col.add waypoint
	
return AddWaypoint