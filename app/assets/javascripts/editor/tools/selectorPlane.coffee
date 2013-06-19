SelectOnTerrain = require 'tools/selectOnTerrain'
scenegraph = require 'scenegraph'
engine = require 'engine'

class SelectorPlane extends SelectOnTerrain
	
	color: 0xFF0000
	
	initialize: ->
		@isVisible = false
		@createSel()
		super()

	update: (position, intersect) ->
		pos = @calculatePossiblePosition position
		if pos.x isnt @selector.position.x or pos.y isnt @selector.position.y or pos.z isnt @selector.position.z
			@selector.position.x = pos.x
			@selector.position.y = pos.y
			@selector.position.z = pos.z
		engine.render()
	
	calculatePossiblePosition: (position) ->
		x: Math.floor(position.x / 10) * 10 + 5
		y: Math.floor(position.y / 10) * 10 + 1
		z: Math.floor(position.z / 10) * 10 + 5

	onLeaveTool: ->
		@removeSel() if @isVisible

	onIntersect: ->
		@addSel() unless @isVisible

	onNonIntersect: ->
		@removeSel() if @isVisible

	createSel: ->
		@selector = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial (color: @color, transparent: true, opacity:1)
		@selector.rotation.x = THREE.Math.degToRad -90
		@selector.material.opacity = 0.3

	addSel: ->
		@isVisible = true
		scenegraph.scene().add @selector

	removeSel: ->
		@isVisible = false
		scenegraph.scene().remove @selector
		engine.render()

return SelectorPlane