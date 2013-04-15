EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
Constants = require 'constants'
templates = require 'templates'
db = require 'database'

class MaterialTexture extends BaseView
		
	className: 'mp-texture'
	
	entity: 'textures'
	
	template: templates.get 'sidebar/materialTexture.tmpl'

	events:
		'click' : 'loadTexture'
	
	loadTexture: (event) =>
		unless event then return
		event.preventDefault()
		if Constants.MATERIAL_SELECTED
			console.log 'Load texture'
			material = db.get 'materials', Constants.MATERIAL_SELECTED
			material.set 'map', @model.get 'threeTexture'
			EditorEventbus.menuSelectMaterial.dispatch Constants.MATERIAL_SELECTED, false

return MaterialTexture