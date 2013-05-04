EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class SelectorTerrain
	
	constructor: (@editor, @intersectHelper) ->
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
		if @radius > 1
			@radius += 0.33 #Precision
		intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ], @radius
		if intersectList.length > 0
			@onIntersect()
			intersect = @intersectHelper.getIntersectObject intersectList
			position = new THREE.Vector3().addVectors intersect.point, intersect.face.normal.clone().applyMatrix4 intersect.object.matrixRotationWorld
			@update position, intersect
		else
			@onNonIntersect()

return SelectorTerrain