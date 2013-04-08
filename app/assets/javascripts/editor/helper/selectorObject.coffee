ObjectHelper = require 'helper/objectHelper'
IntersectHelper = require 'helper/intersectHelper'
EditorEventbus = require 'editorEventbus'

class SelectorObject

	constructor: (@editor) ->
		@objectHelper = new ObjectHelper @editor
		@intersectHelper = new IntersectHelper @editor
		@bindEventListeners()

	bindEventListeners: ->
		EditorEventbus.selectWorldUI.add @selectWorld
		EditorEventbus.selectTerrainUI.add @selectTerrain
		EditorEventbus.selectObjectUI.add @selectObject

	update: ->
		@removeSelectionWireframe @editor.engine.scenegraph.getMap() if @selectedObject and @selectedType is 'terrain'
		objects = @intersectHelper.mouseIntersects @editor.engine.scenegraph.scene.children
		if objects.length > 0
			obj = @objectHelper.getBaseObject objects[0].object
			if @objectHelper.isTerrain 
				@addSelectionWireframe obj
				@selectedType = 'terrain'
				EditorEventbus.selectTerrainViewport.dispatch()
			else 
				@selectedType = 'object'
				EditorEventbus.selectObjectViewport.dispatch()
			@selectedObject = obj
		else
			@selectedType = 'world'
			@selectedObject = null
			EditorEventbus.selectWorldViewport.dispatch()
		@editor.engine.render()

	selectWorld: =>
		@removeSelectionWireframe @editor.engine.scenegraph.getMap() if @selectedObject and @selectedType is 'terrain'
		@selectedType = 'world'
		@selectedObject = null
		@editor.engine.render()

	selectTerrain: =>
		if @selectedObject and @selectedType isnt 'terrain'
			@removeSelectionWireframe @selectedObject
		else if @selectedType isnt 'terrain'
			@selectedObject = @editor.engine.scenegraph.getMap()
			@addSelectionWireframe @selectedObject
			@selectedType = 'terrain'
			@editor.engine.render()

	selectObject: =>
		# TODO: implement this ...

	addSelectionWireframe: (obj) ->
		if @objectHelper.hasWireframe obj
			@objectHelper.changeWireframeColor obj, 0xFFFF00
		else
			@objectHelper.addWireframe obj, 0xFFFF00

	removeSelectionWireframe: (obj) ->
		if @objectHelper.hasWireframe obj
			@objectHelper.changeWireframeColor obj, 0xFFFFFF
		else
			@objectHelper.removeWireframe obj

return SelectorObject