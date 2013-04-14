EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class MaterialTexture extends BaseView
		
	className: 'mp-texture'
	
	entity: 'textures'
	
	template: templates.get 'sidebar/materialTexture.tmpl'

return MaterialTexture