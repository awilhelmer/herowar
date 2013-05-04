SelectorTerrain = require 'tools/selectorTerrain'

class SelectorPlane extends SelectorTerrain
	
	color: 0xFF0000
	
	initialize: ->
		@isVisible = false
		@createSel()
		super()

	update: (position, intersect) ->
		x = Math.floor(position.x / 10) * 10 + 5
		y = Math.floor(position.y / 10) * 10 + 1
		z = Math.floor(position.z / 10) * 10 + 5
		if x isnt @selector.position.x or y isnt @selector.position.y or z isnt @selector.position.z
			@selector.position.x = x
			@selector.position.y = y
			@selector.position.z = z
		@editor.engine.render()

	onLeaveTool: ->
		@removeSel() if @isVisible

	onIntersect: ->
		@addSel() unless @isVisible

	onNonIntersect: ->
		@removeSel() if @isVisible

	createSel: ->
		@selector = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial (color: @color, transparent: true, opacity:1)
		@selector.rotation.x = - Math.PI/2
		@selector.material.opacity = 0.3

	addSel: ->
		@isVisible = true
		@editor.engine.scenegraph.scene.add @selector

	removeSel: ->
		@isVisible = false
		@editor.engine.scenegraph.scene.remove @selector
		@editor.engine.render()

return SelectorPlane