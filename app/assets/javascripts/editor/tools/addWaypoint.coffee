SelectorPlane = require 'tools/selectorPlane'
EditorEventbus = require 'editorEventbus'
Waypoint = require 'models/waypoint'
log = require 'util/logger'
db = require 'database'

class AddWaypoint extends SelectorPlane
	
	color: 0x0000FF
	
	initialize: ->
		@currentPathId = -1
		@nextId = 1
		super()
	
	bindEvents: ->
		EditorEventbus.selectPathUI.add @selectPathId
	
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
			'path'			: @currentPathId
		log.info "Created new waypoint at #{pos.x} x #{pos.y} x #{pos.z}"
		col = db.get 'waypoints'
		col.add waypoint
	
	selectPathId: (value) =>
		@currentPathId = value
	
return AddWaypoint