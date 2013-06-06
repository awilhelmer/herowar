class BaseGUI
	
	removable: true
	
	constructor: (@name, @parent) ->
		_.extend @, Backbone.Events
		@model = {}
		@children = {}
		@bindEvents()

	bindEvents: ->

	add: (element) ->
		if @root and element.name and not _.has @children, element.name
			element.parent = @root
			element.root = element.create()
			@children[name] = element
		return

	dispose: ->
		@parent.removeChild @root if removable and @root and @parent
		return

return BaseGUI