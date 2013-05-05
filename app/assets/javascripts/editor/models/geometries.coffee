EditorEventbus = require 'editorEventbus'

class Geometries extends Backbone.Collection
	
	initialize: (models, options) ->
		@id = -1
		@model = require 'models/geometry'
		EditorEventbus.treeSelectItem.add @selectItem
		super models, options
		
	selectItem: (name, value) =>
		if name is 'sidebar-environment-categories-list' and @id isnt value
			@id = value
			@url = "/api/editor/environment/#{@id}"
			@fetch()

return Geometries