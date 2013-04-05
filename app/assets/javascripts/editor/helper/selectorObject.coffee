class SelectorObject

	constructor: (@editor) ->

	update: (intersect) ->
		objects = @editor.intersectHelper.mouseIntersects @editor.engine().scenegraph.scene.children
		if objects.length > 0
			@editor.editorScenegraph.handleSelection objects[0].object
		else
			@editor.editorScenegraph.handleSelection()

return SelectorObject