ObjectHelper = require 'helper/objectHelper'
IntersectHelper = require 'helper/intersectHelper'
EditorEventbus = require 'editorEventbus'

class SelectorObject

	constructor: (@editor) ->
		@objectHelper = new ObjectHelper @editor
		@intersectHelper = new IntersectHelper @editor

	update: ->
		if @selectedObject
			@removeSelectionWireframe @editor.engine.scenegraph.getMap() if @selectedType is 'terrain'
		objects = @intersectHelper.mouseIntersects @editor.engine.scenegraph.scene.children
		if objects.length > 0
			obj = @objectHelper.getBaseObject objects[0].object
			if @objectHelper.isTerrain 
				@addSelectionWireframe obj
				@selectedType = 'terrain'
			else 
				@selectedType = 'object'
			@selectedObject = obj
			EditorEventbus.selectObjectViewport.dispatch obj
		else
			@selectedType = 'world'
			@selectedObject = null
			EditorEventbus.selectObjectViewport.dispatch
		@editor.engine.render()

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