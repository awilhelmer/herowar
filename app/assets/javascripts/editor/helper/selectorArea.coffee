EditorEventbus = require 'editorEventbus'
Variables = require 'variables'
Constants = require 'constants'
db = require 'database'
materialHelper = require 'helper/materialHelper'

class SelectorArea
	
	constructor: (@editor, @intersectHelper, @selectorObject) ->
		@selector = new THREE.Mesh new THREE.PlaneGeometry(10, 10), new THREE.MeshBasicMaterial (color: 0xFF0000, transparent: true, opacity:1)
		@selector.rotation.x = - Math.PI/2
		@selector.material.opacity = 0.3
		@world = db.get 'world'
		@isVisible = false
		@model = null
		@brushSize = 1
		@bindEvents()
		
	bindEvents: ->
		EditorEventbus.selectMaterial.add @onMaterialSelected
		EditorEventbus.deselectMaterial.add @onMaterialDeselect
		EditorEventbus.selectBrush.add @selectBrush
		EditorEventbus.selectBrushSize.add @selectBrushSize

	onMouseUp: (event) ->
		if event.which is 1
			EditorEventbus.resetWireframe.dispatch @selectorObject.selectedObject
		else if event.which is 3
			Constants.TOOL_BRUSH_SELECTED = false
			console.log 'Set Tool Selection'
			EditorEventbus.selectTool.dispatch Constants.TOOL_SELECTION
			terrain = db.get 'ui/terrain'
			terrain.unset Constants.BRUSH_MODE if terrain
			

	onMouseMove: ->
		radius = @brushSize/2
		if radius > 1
			radius += 0.33 #Precision
		intersectList = @intersectHelper.mouseIntersects [ @editor.engine.scenegraph.getMap() ], radius
		if intersectList.length > 0
			@addSel() unless @isVisible
			@updatePosition @intersectHelper.getIntersectObject intersectList
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
		position = new THREE.Vector3().addVectors intersect.point, intersect.face.normal.clone().applyMatrix4 intersect.object.matrixRotationWorld
		unless @selectorObject.selectedObject
			console.log "Selecting terrain!"
			@selectorObject.selectTerrain()
		if Variables.MOUSE_PRESSED_LEFT	
			if @brushTool is Constants.BRUSH_APPLY_MATERIAL
				update = @handleBrush intersect
				if update
					@saveGeometry intersect.object.geometry
					@saveMaterials()
			else if @brushTool is Constants.BRUSH_TERRAIN_RAISE
				intersect.object.geometry.vertices[intersect.face.a].z += 1
				intersect.object.geometry.vertices[intersect.face.b].z += 1
				intersect.object.geometry.vertices[intersect.face.c].z += 1
				intersect.object.geometry.vertices[intersect.face.d].z += 1
				intersect.object.geometry.verticesNeedUpdate = true
				@saveGeometry intersect.object.geometry
			else if @brushTool is Constants.BRUSH_TERRAIN_DEGRADE 
				intersect.object.geometry.vertices[intersect.face.a].z -= 1
				intersect.object.geometry.vertices[intersect.face.b].z -= 1
				intersect.object.geometry.vertices[intersect.face.c].z -= 1
				intersect.object.geometry.vertices[intersect.face.d].z -= 1
				intersect.object.geometry.verticesNeedUpdate = true
				@saveGeometry intersect.object.geometry
		else
		x = Math.floor(position.x / 10) * 10 + 5
		y = Math.floor(position.y / 10) * 10 + 1
		z = Math.floor(position.z / 10) * 10 + 5
		if x isnt @selector.position.x or y isnt @selector.position.y or z isnt @selector.position.z
			@selector.position.x = x
			@selector.position.y = y
			@selector.position.z = z
		@editor.engine.render()
		null

	saveGeometry: (geometry) ->
		@world.get('terrain').geometry.faces = geometry.faces
		@world.get('terrain').geometry.vertices = geometry.vertices
		@world.trigger 'change:terrain'
		@world.trigger 'change'

	saveMaterials: ->
		# MapProperties.TERRAIN_MATERIALS = []
		# for material in db.get('materials').models
			# MapProperties.TERRAIN_MATERIALS.push material

	handleBrush: (intersect) ->
		object = intersect.object
		faceIndex = intersect.faceIndex
		baseObject = @selectorObject.objectHelper.getBaseObject object
		if baseObject is @editor.engine.scenegraph.map and @selectedMatId
			newIndex = materialHelper.getThreeMaterialId object, @selectedMatId
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
