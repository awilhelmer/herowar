EditorEventbus = require 'editorEventbus'
log = require 'util/logger'
events = require 'events'
db = require 'database'

class PathingHelper
	
	constructor: (@editor) ->
		@sidebar = db.get 'ui/sidebar'
		@waypoints = db.get 'waypoints'
		@path = null
		@pathWaypoints = []
		@meshes = []
		@bindEvents()
	
	bindEvents: ->
		EditorEventbus.selectPathUI.add @selectPath
		events.listenTo @sidebar, 'change:active', @updatePath
		events.listenTo @waypoints, 'add remove reset', @updateWaypoints

	selectPath: (id) =>
		@path = db.get 'paths', id
		@updateWaypoints()

	updatePath: =>
		if @sidebar.get('active') is 'sidebar-properties-pathing'
			if @path and @pathWaypoints.length isnt 0 and @meshes.length is 0
				@buildPath()
		else
			if @meshes.length isnt 0
				@removePath()

	updateWaypoints: =>
		# TODO: check waypoint arrays for differences
		if @path
			@pathWaypoints = db.get('waypoints').where path: @path.get('id')
			@removePath() if @meshes.length isnt 0
			@updatePath()

	buildPath: ->
		for waypoint in @pathWaypoints
			mesh = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial (color: 0x0000FF, transparent: true, opacity:1)
			mesh.position = waypoint.get('position')
			mesh.rotation.x = - Math.PI/2
			@editor.engine.scenegraph.scene.add mesh
			@meshes.push mesh
		@editor.engine.render()

	removePath: ->
		@editor.engine.scenegraph.scene.remove mesh for mesh in @meshes
		@meshes.length = 0
		@editor.engine.render()

return PathingHelper