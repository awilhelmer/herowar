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
		@nextId = 1
		super options
	
	newMaterial: (event) =>
		event?.preventDefault()
		col = db.get 'materials'
		mat = new Material()
		mat.set 'name', "Mat.#{@nextId++}"
		col.add mat

return MaterialManagerMenu