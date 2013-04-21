BaseView = require 'views/baseView'
templates = require 'templates'

class Tree extends BaseView

	className: 'tree'
		
	template: templates.get 'util/tree.tmpl'

return Tree