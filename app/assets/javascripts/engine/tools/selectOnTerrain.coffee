BaseTool = require 'tools/baseTool'
scenegraph = require 'scenegraph'
db = require 'database'

class SelectOnTerrain extends BaseTool
	
	constructor: (@intersectHelper) ->
		@initialize()
	
	initialize: ->
		@radius = 0.5
		@bindEvents()
		return
	
	bindEvents: ->
		return

	onIntersect: ->
		return

	onNonIntersect: ->
		return

	update: (position, intersect) ->
		return

	onMouseMove: (event) =>
		radius = @radius
		if radius > 1
			radius /=2
			radius += 0.33
		intersectList = @intersectHelper.mouseIntersects [ scenegraph.getMap().getMainObject() ], radius
		if intersectList.length > 0
			@lastIntersect = @intersectHelper.getIntersectObject intersectList
			@lastPosition = new THREE.Vector3().addVectors @lastIntersect.point, @lastIntersect.face.normal.clone().applyMatrix4 @lastIntersect.object.matrixRotationWorld
			@onIntersect()
			@update @lastPosition, @lastIntersect
		else
			@onNonIntersect()
		return

return SelectOnTerrain