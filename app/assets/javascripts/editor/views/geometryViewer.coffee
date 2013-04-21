BaseView = require 'views/baseView'
templates = require 'templates'

class GeometryViewer extends BaseView

	id: 'geometryViewer'
	
	template: templates.get 'geometryViewer.tmpl'
	
return GeometryViewer