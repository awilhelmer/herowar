BaseView = require 'views/baseView'
templates = require 'templates'

class SceneExplorer extends BaseView
	
	id: 'sidebar-sceneexlorer'
	
	className: 'sidebar-panel'
	
	template: templates.get 'sidebar/sceneExplorer.tmpl'
	
return SceneExplorer