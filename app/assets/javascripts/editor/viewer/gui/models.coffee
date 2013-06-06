BaseGUI = require 'viewer/gui/base'
db = require 'database'

class ModelGUI extends BaseGUI
	
	removable: false
	
	constructor: ->
		@viewer = db.get 'viewer'
		@units = db.get 'db/units'
		super 'Models'
		@units.fetch() if @units.models.length is 0
		@isFirst = true

	bindEvents: ->
		@listenTo @units, 'add remove change reset', @updateUnit
	
	create: ->
		@root = @parent.addFolder @name
		@updateUnit() unless @units.models.length is 0
		return @root
	
	updateUnit: ->
		for unit, idx in @units.models
			@addObject unit, 2

	addObject: (obj, type) ->
			name = obj.get 'name'
			unless _.has @children, name
				cb = (id, type, name) =>
					return () => @viewer.load id, type, name
				@model[name] = cb obj.id, type, name
				if @isFirst and not @viewer.sceneObject
					@model[name]()
					@isFirst = false
				@children[name] = @root.add(@model, name).name name
	
return ModelGUI