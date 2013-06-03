SelectorPlane = require 'tools/selectorPlane'
EditorEventbus = require 'editorEventbus'
Waypoint = require 'models/db/waypoint'
log = require 'util/logger'
db = require 'database'

class AddWaypoint extends SelectorPlane
	
	color: 0x0000FF
	
	initialize: ->
		@currentPathId = -1
		@nextId = 1
		@nextDbId = -1
		super()
	
	bindEvents: ->
		EditorEventbus.selectPathUI.add @selectPathId
	
	onMouseUp: (event) ->
		@createWaypoint() if event.which is 1 and @isVisible
	
	createWaypoint: ->
		pos = @calculatePossiblePosition @lastPosition
		id = @nextId++
		dbId = @nextDbId--
		waypoint = new Waypoint()
		waypoint.set
			'id' 				: id
			'dbId'			: dbId
			'name'			: "Waypoint #{id} - #{pos.x} x #{pos.y} x #{pos.z}"
			'position'	: pos
			'path'			: @currentPathId
		log.info "Created new waypoint at #{pos.x} x #{pos.y} x #{pos.z}"
		col = db.get 'db/waypoints'
		col.add waypoint
	
	selectPathId: (value) =>
		@currentPathId = value
	
return AddWaypoint