EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
Constants = require 'constants'
templates = require 'templates'
log = require 'util/logger'
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
			log.debug 'Load texture'
			material = db.get 'materials', Constants.MATERIAL_SELECTED
			material.set 'map', @model.get 'threeTexture'
			EditorEventbus.dispatch 'changeMaterial', Constants.MATERIAL_SELECTED

return MaterialTexture