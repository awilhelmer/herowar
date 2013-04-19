IntersectHelper = require 'helper/intersectHelper'
MaterialHelper = require 'helper/materialHelper'
EditorEventbus = require 'editorEventbus'
MapProperties = require 'mapProperties'
Variables = require 'variables'
Constants = require 'constants'
db = require 'database'

class SelectorArea
	
	constructor: (@editor, @materialHelper, @selectorObject) ->
		@intersectHelper = new IntersectHelper @editor
		@selector = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial (color: 0xFF0000, transparent: true, opacity:1)
		@selector.rotation.x = - Math.PI/2
		@isVisible = false
		@model = null
		@brushSize = 1
		@bindEvents()
		
	bindEvents: ->
		EditorEventbus.selectMaterial.add @onMaterialSelected
		EditorEventbus.deselectMaterial.add @onMaterialDeselect
		EditorEventbus.selectBrush.add @selectBrush
		EditorEventbus.selectBrushSize.add @selectBrushSize
		
	update: ->
		radius = @brushSize/2
		if radius > 1
			radius += 0.33 #Precision
		intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ], radius
		if intersectList.length > 0
			@addSel() unless @isVisible
			@updatePosition @getIntersectObject(intersectList)
		else
			@removeSel() if @isVisible

	addSel: ->
		@isVisible = true
		@editor.engine.scenegraph.scene.add @selector

	removeSel: ->
		@isVisible = false
		@editor.engine.scenegraph.scene.remove @selector
		@editor.engine.render()

	getIntersectObject: (intersectList) ->
		for value, key in intersectList
			if value.object and value.object.name isnt 'wireframe'
				result = value
				break
		unless result
			result = intersectList[0]
		result
		
	updatePosition: (intersect) ->
		position = new THREE.Vector3().addVectors intersect.point, intersect.face.normal.clone().applyMatrix4(intersect.object.matrixRotationWorld)
		unless @selectorObject.selectedObject
			console.log "Selecting terrain!"
			@selectorObject.selectTerrain()
		if Variables.MOUSE_PRESSED_LEFT	
			if @brushTool is Constants.BRUSH_APPLY_MATERIAL
				@selector.material.opacity = 0.3
				update = @handleBrush intersect
				if update
					MapProperties.TERRAIN_FACES = intersect.object.geometry.faces
					MapProperties.TERRAIN_VERTICES = intersect.object.geometry.vertices
					@saveMaterials()
			else if @brushTool is Constants.BRUSH_TERRAIN_RAISE
				intersect.object.geometry.vertices[intersect.face.a].z += 1
				intersect.object.geometry.vertices[intersect.face.b].z += 1
				intersect.object.geometry.vertices[intersect.face.c].z += 1
				intersect.object.geometry.vertices[intersect.face.d].z += 1
				intersect.object.geometry.verticesNeedUpdate = true
				MapProperties.TERRAIN_FACES = intersect.object.geometry.faces
				MapProperties.TERRAIN_VERTICES = intersect.object.geometry.vertices
				@selector.material.opacity = 0.3
			else if @brushTool is Constants.BRUSH_TERRAIN_DEGRADE 
				intersect.object.geometry.vertices[intersect.face.a].z -= 1
				intersect.object.geometry.vertices[intersect.face.b].z -= 1
				intersect.object.geometry.vertices[intersect.face.c].z -= 1
				intersect.object.geometry.vertices[intersect.face.d].z -= 1
				intersect.object.geometry.verticesNeedUpdate = true
				MapProperties.TERRAIN_FACES = intersect.object.geometry.faces
				MapProperties.TERRAIN_VERTICES = intersect.object.geometry.vertices
				@selector.material.opacity = 0.3
		else
			@selector.material.opacity = 1
		x = Math.floor(position.x / 10) * 10 + 5
		y = Math.floor(position.y / 10) * 10 + 1
		z = Math.floor(position.z / 10) * 10 + 5
		if x isnt @selector.position.x or y isnt @selector.position.y or z isnt @selector.position.z
			@selector.position.x = x
			@selector.position.y = y
			@selector.position.z = z
		@editor.engine.render()
		null

	saveMaterials: ->
		MapProperties.TERRAIN_MATERIALS = []
		for material in db.get('materials').models
			MapProperties.TERRAIN_MATERIALS.push material

	handleBrush: (intersect) ->
		object = intersect.object
		faceIndex = intersect.faceIndex
		baseObject = @selectorObject.objectHelper.getBaseObject object
		if baseObject is @editor.engine.scenegraph.map and @selectedMatId
			newIndex = @materialHelper.getThreeMaterialId object, @selectedMatId
			for face in intersect.faces
				oldIndex = face.materialIndex
				if oldIndex isnt newIndex 
					face.materialIndex = newIndex
					unless update
						scene = @editor.engine.scenegraph.scene
						baseObject.remove object
						@editor.engine.render()
						object.geometry.geometryGroups = undefined
						object.geometry.geometryGroupsList = undefined
						object.__webglInit = false #hack
						object.__webglActive = false #hack 				
						baseObject.add object
						update = true
		update
		
	onMaterialSelected: (idMapper) =>
		console.log "SelectorArea: Selected ID #{idMapper.id} MaterialId #{idMapper.materialId}!"
		@selectedMatId = idMapper
		
	onMaterialDeselect: () =>
		console.log 'SelectorArea: Deselected ID!'
		@selectedMatId = null

	selectBrush: (tool) =>
		@brushTool = tool
		
	selectBrushSize: (brushSize) =>
		@brushSize = brushSize
		@selector.scale.x = brushSize
		@selector.scale.y = brushSize
return SelectorArea
