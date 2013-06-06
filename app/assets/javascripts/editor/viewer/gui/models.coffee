BaseGUI = require 'viewer/gui/base'
db = require 'database'

class ModelGUI extends BaseGUI
	
	removable: false
	
	constructor: ->
		@viewer = db.get 'viewer'
		@units = db.get 'db/units'
		@units.fetch()
		super 'Models'

	bindEvents: ->
		@listenTo @units, 'add remove change reset', @updateUnit
	
	create: ->
		return @parent.addFolder @name
	
	updateUnit: ->
		for unit, idx in @units.models
			@addObject unit, 2

	addObject: (obj, type) ->
			name = obj.get 'name'
			unless _.has @children, name
				cb = (id, type, name) =>
					return () => @viewer.load id, type, name
				@model[name] = cb obj.id, type, name
				@model[name]()
				@children[name] = @root.add(@model, name).name name
	
return ModelGUI