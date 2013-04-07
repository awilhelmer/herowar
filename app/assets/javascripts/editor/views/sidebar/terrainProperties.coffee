BaseView = require 'views/baseView'
templates = require 'templates'

class TerrainProperties extends BaseView
	
	id: 'sidebar-properties-terrain'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/terrainProperties.tmpl'
	
return TerrainProperties