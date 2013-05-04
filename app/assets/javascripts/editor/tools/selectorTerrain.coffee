EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class SelectorTerrain
	
	constructor: (@editor, @intersectHelper) ->
		@initialize()
	
	initialize: ->
		@tool = db.get 'ui/tool'
		@radius = 0.5
		@bindEvents()
	
	bindEvents: ->

	onLeaveTool: ->

	onIntersect: ->

	onNonIntersect: ->

	update: (position, intersect) ->

	onMouseUp: (event) =>
		if event.which is 3
			@onLeaveTool()
			log.debug 'Set Tool Selection'
			@tool.set 'active', Constants.TOOL_SELECTION
		
	onMouseMove: (event) =>
		radius = @radius
		if radius > 1
			radius /=2
			radius += 0.33
		intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ], radius
		if intersectList.length > 0
			@lastIntersect = @intersectHelper.getIntersectObject intersectList
			@lastPosition = new THREE.Vector3().addVectors @lastIntersect.point, @lastIntersect.face.normal.clone().applyMatrix4 @lastIntersect.object.matrixRotationWorld
			@onIntersect()
			@update @lastPosition, @lastIntersect
		else
			@onNonIntersect()

return SelectorTerrain