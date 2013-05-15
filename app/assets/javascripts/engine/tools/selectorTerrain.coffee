db = require 'database'

class SelectorTerrain
	
	constructor: (@app, @intersectHelper) ->
		@initialize()
	
	initialize: ->
		@radius = 0.5
		@bindEvents()
	
	bindEvents: ->

	onLeaveTool: ->

	onIntersect: ->

	onNonIntersect: ->

	update: (position, intersect) ->

	onMouseUp: (event) =>
		
	onMouseMove: (event) =>
		radius = @radius
		if radius > 1
			radius /=2
			radius += 0.33
		intersectList = @intersectHelper.mouseIntersects [ @app.engine.scenegraph.getMap() ], radius
		if intersectList.length > 0
			@lastIntersect = @intersectHelper.getIntersectObject intersectList
			@lastPosition = new THREE.Vector3().addVectors @lastIntersect.point, @lastIntersect.face.normal.clone().applyMatrix4 @lastIntersect.object.matrixRotationWorld
			@onIntersect()
			@update @lastPosition, @lastIntersect
		else
			@onNonIntersect()

return SelectorTerrain