BaseView = require 'views/baseView'
templates = require 'templates'
Material = require 'models/material'
db = require 'database'

class MaterialManagerMenu extends BaseView

	id: 'materialManagerMenu'

	template: templates.get 'materialManagerMenu.tmpl'

	events:
		'click .mm-new-material' : 'newMaterial'
	
	initialize: (options) ->
		@nextId = 2
		super options
	
	newMaterial: (event) =>
		event?.preventDefault()
		col = db.get 'materials'
		mat = new Material()
		id = @nextId++
		mat.set 
			'id'					: id
			'materialId' 	: id
			'name'				: "Mat.#{id}"
			'color' 			: '#CCCCCC'
		col.add mat

return MaterialManagerMenu