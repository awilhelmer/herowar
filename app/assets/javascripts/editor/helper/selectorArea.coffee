IntersectHelper = require 'helper/intersectHelper'
EditorEventbus = require 'editorEventbus'
Variables = require 'variables'
Constants = require 'constants'
MaterialHelper = require 'helper/materialHelper'

class SelectorArea
	
	constructor: (@editor, @materialHelper, @selectorObject) ->
		@intersectHelper = new IntersectHelper @editor
		@selector = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial color: 0xFF0000
		@selector.rotation.x = - Math.PI/2
		@isVisible = false
		@model = null
		@bindEvents()
		
	bindEvents: ->
		EditorEventbus.selectMaterial.add @onMaterialSelected
		EditorEventbus.deselectMaterial.add @onMaterialDeselect
		EditorEventbus.selectBrush.add @selectBrush
		
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
		if Variables.MOUSE_PRESSED_LEFT	
			if @brushTool is Constants.BRUSH_APPLY_MATERIAL
				@handleBrush intersect.object, intersect.faceIndex
				@removeSel()
			else if @brushTool is Constants.BRUSH_TERRAIN_RAISE
				intersect.object.geometry.vertices[intersect.face.a].z += 1
				intersect.object.geometry.vertices[intersect.face.b].z += 1
				intersect.object.geometry.vertices[intersect.face.c].z += 1
				intersect.object.geometry.vertices[intersect.face.d].z += 1
				intersect.object.geometry.verticesNeedUpdate = true
				#intersect.object.geometry.computeCentroids()
				@editor.engine.render()
			else if @brushTool is Constants.BRUSH_TERRAIN_DEGRADE 
				intersect.object.geometry.vertices[intersect.face.a].z -= 1
				intersect.object.geometry.vertices[intersect.face.b].z -= 1
				intersect.object.geometry.vertices[intersect.face.c].z -= 1
				intersect.object.geometry.vertices[intersect.face.d].z -= 1
				intersect.object.geometry.verticesNeedUpdate = true
				#intersect.object.geometry.computeCentroids()
				@editor.engine.render()
		else
			x = Math.floor(position.x / 10) * 10 + 5
			y = Math.floor(position.y / 10) * 10 + 1
			z = Math.floor(position.z / 10) * 10 + 5
			if x isnt @selector.position.x or y isnt @selector.position.y or z isnt @selector.position.z
				# console.log intersect
				@selector.position.x = x
				@selector.position.y = y
				@selector.position.z = z
				@editor.engine.render()

	handleBrush: (object, faceIndex) ->
		baseObject = @selectorObject.objectHelper.getBaseObject object
		if baseObject is @editor.engine.scenegraph.map and @selectedMatId
			oldIndex = object.geometry.faces[faceIndex].materialIndex
			object.geometry.faces[faceIndex].materialIndex = @materialHelper.getThreeMaterialId object, @selectedMatId
			if oldIndex isnt object.geometry.faces[faceIndex].materialIndex
				@editor.engine.scenegraph.scene.remove baseObject
				@editor.engine.render()
				object.geometry.verticesNeedUpdate = true
				object.geometry.elementsNeedUpdate = true
				object.geometry.morphTargetsNeedUpdate = true
				object.geometry.uvsNeedUpdate = true
				object.geometry.normalsNeedUpdate = true
				object.geometry.colorsNeedUpdate = true
				object.geometry.tangentsNeedUpdate = true
				object.material.needsUpdate = true
				object.geometry.computeCentroids()
				object.geometry.computeFaceNormals()
				object.geometry.computeVertexNormals()
				@editor.engine.scenegraph.scene.add baseObject
				console.log "setted brush material: materialIndex #{object.geometry.faces[faceIndex].materialIndex}"
		null
		
	updateMesh: (object) ->
		@editor.engine.scenegraph.scene.remove object
		@editor.engine.scenegraph.scene.add object
		
	onMaterialSelected: (materialId) =>
		console.log 'SelectorArea: Selected ID!'
		@selectedMatId = materialId
		
	onMaterialDeselect: () =>
		console.log 'SelectorArea: Deselected ID!'
		@selectedMatId = null

	selectBrush: (tool) =>
		@brushTool = tool

return SelectorArea
