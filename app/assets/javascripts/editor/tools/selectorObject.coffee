EditorEventbus = require 'editorEventbus'
materialHelper = require 'helper/materialHelper'
BaseTool = require 'tools/baseTool'
scenegraph = require 'scenegraph'
Variables = require 'variables'
engine = require 'engine'
db = require 'database'

class SelectorObject extends BaseTool

	constructor: (@objectHelper, @intersectHelper) ->
		@input = db.get 'input'
		@tool = db.get 'ui/tool'
		@world = db.get 'world'
		@bindEventListeners()

	bindEventListeners: ->
		EditorEventbus.selectWorldUI.add @selectWorld
		EditorEventbus.selectTerrainUI.add @selectTerrain
		EditorEventbus.selectObjectUI.add @selectObject
		EditorEventbus.updateModelMaterial.add @materialUpdate
	
	onMouseUp: (event) ->
		if event.which is 1
			@update() unless @tool.get('currentMesh') and @input.get('mouse_moved')
	
	onMouseMove: ->
	
	update: ->
		@removeSelectionWireframe @selectedObject, @selectedType if @selectedObject
		objects = @intersectHelper.mouseIntersects scenegraph.scene().children
		if objects.length > 0
			obj = @objectHelper.getBaseObject objects[0].object
			if @objectHelper.isTerrain obj
				@selectedType = 'terrain'
				EditorEventbus.dispatch 'selectTerrainViewport'
			else 
				list = db.get('environmentsStatic').where dbId: obj.userData.dbId, listIndex: obj.userData.listIndex
				if list.length > 0
					@selectedType = 'object'
					EditorEventbus.dispatch 'selectObjectViewport', list[0].get 'id'
			@addSelectionWireframe obj, @selectedType
			@selectedObject = obj
		else
			@selectedType = 'world'
			@selectedObject = null
			EditorEventbus.dispatch 'selectWorldViewport'
		engine.render()

	selectWorld: =>
		@removeSelectionWireframe @selectedObject, @selectedType if @selectedObject
		@selectedType = 'world'
		@selectedObject = null
		engine.render()

	selectTerrain: =>
		if @selectedObject and @selectedType isnt 'terrain'
			@removeSelectionWireframe @selectedObject, @selectedType
		if @selectedType isnt 'terrain'
			@selectedObject = scenegraph.getMap().getMainObject()
			@selectedType = 'terrain'
			@addSelectionWireframe @selectedObject, @selectedType
			engine.render()

	selectObject: =>
		# TODO: implement this ...

	addSelectionWireframe: (obj, type) ->
		if @objectHelper.hasWireframe obj
			@objectHelper.changeWireframeColor obj, 0xFFFF00
		else
			@objectHelper.addWireframe obj, 0xFFFF00

	removeSelectionWireframe: (obj, type) ->
		if @objectHelper.hasWireframe(obj) and type is 'terrain' and @world.get('terrain').wireframe
			@objectHelper.changeWireframeColor obj, 0xFFFFFF
		else
			@objectHelper.removeWireframe obj
	
	materialUpdate: (idMapper) =>
		if idMapper
			map = scenegraph.getMap().getMainObject()
			mesh = @objectHelper.getModel map
			matIndex = materialHelper.updateMaterial mesh, idMapper
			if matIndex > -1 and mesh.material.materials[matIndex].map and mesh.material.materials[matIndex].map.needsUpdate
				map.remove mesh
				engine.render()
				mesh.geometry.geometryGroups = undefined
				mesh.geometry.geometryGroupsList = undefined
				mesh.__webglInit = false
				mesh.__webglActive = false			
				map.add mesh
			engine.render()
		null

return SelectorObject