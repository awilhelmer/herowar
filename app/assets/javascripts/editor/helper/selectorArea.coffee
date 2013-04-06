IntersectHelper = require 'helper/intersectHelper'

class SelectorArea

	constructor: (@editor) ->
		@intersectHelper = new IntersectHelper @editor
		@selector = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial color: 0xFF0000
		@selector.rotation.x = - Math.PI/2
		@isVisible = false

	update: ->
		intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ]
		if intersectList.length > 0
			@addSel() unless @isVisible
			@updatePosition intersectList[0]
		else
			@removeSel() if @isVisible

	addSel: ->
		@isVisible = true
		@editor.engine.scenegraph.scene.add @selector

	removeSel: ->
		@isVisible = false
		@editor.engine.scenegraph.scene.remove @selector
		@editor.engine.render()

	updatePosition: (intersect) ->
			position = new THREE.Vector3().addVectors intersect.point, intersect.face.normal.clone().applyMatrix4(intersect.object.matrixRotationWorld)
			x = Math.floor(position.x / 10) * 10 + 5
			y = Math.floor(position.y / 10) * 10 + 1
			z = Math.floor(position.z / 10) * 10 + 5
			if x isnt @selector.position.x or y isnt @selector.position.y or z isnt @selector.position.z
				@selector.position.x = x
				@selector.position.y = y
				@selector.position.z = z
				@editor.engine.render()

return SelectorArea