SelectorPlane = require 'tools/selectorPlane'
materialHelper = require 'helper/materialHelper'
EditorEventbus = require 'editorEventbus'
Variables = require 'variables'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class BrushMaterial extends SelectorPlane
	
	constructor: (@editor, @intersectHelper, @selectorObject) ->
		super @editor, @intersectHelper
		
		initialize: ->
			@world = db.get 'world'
			super()
		
	bindEvents: ->
		EditorEventbus.selectMaterial.add @onMaterialSelected
		EditorEventbus.selectBrush.add @selectBrush
		EditorEventbus.selectBrushSize.add @selectBrushSize

	onMouseUp: (event) ->
		EditorEventbus.dispatch 'resetWireframe', @selectorObject.selectedObject if event.which is 1
		super event
		
	update: (position, intersect) ->
		unless @selectorObject.selectedObject
			log.debug "Selecting terrain!"
			@selectorObject.selectTerrain()
		if Variables.MOUSE_PRESSED_LEFT	
			if @brushTool is Constants.BRUSH_APPLY_MATERIAL
				update = @handleBrush intersect
				if update
					@world.saveGeometry intersect.object.geometry
			else if @brushTool is Constants.BRUSH_TERRAIN_RAISE
				intersect.object.geometry.vertices[intersect.face.a].z += 1
				intersect.object.geometry.vertices[intersect.face.b].z += 1
				intersect.object.geometry.vertices[intersect.face.c].z += 1
				intersect.object.geometry.vertices[intersect.face.d].z += 1
				intersect.object.geometry.verticesNeedUpdate = true
				@world.saveGeometry intersect.object.geometry
			else if @brushTool is Constants.BRUSH_TERRAIN_DEGRADE 
				intersect.object.geometry.vertices[intersect.face.a].z -= 1
				intersect.object.geometry.vertices[intersect.face.b].z -= 1
				intersect.object.geometry.vertices[intersect.face.c].z -= 1
				intersect.object.geometry.vertices[intersect.face.d].z -= 1
				intersect.object.geometry.verticesNeedUpdate = true
				@world.saveGeometry intersect.object.geometry
		super position, intersect

	onLeaveTool: ->
		terrain = db.get 'ui/terrain'
		terrain.unset Constants.BRUSH_MODE if terrain
		super()

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
		log.debug "SelectorArea: Selected ID #{idMapper.id} MaterialId #{idMapper.materialId}!"
		@selectedMatId = idMapper

	selectBrush: (tool) =>
		@brushTool = tool
		
	selectBrushSize: (brushSize) =>
		@radius = brushSize / 2
		@selector.scale.x = brushSize
		@selector.scale.y = brushSize
		
return BrushMaterial
