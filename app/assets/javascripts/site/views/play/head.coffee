BaseView = require 'views/baseView'
templates = require 'templates'

class PlayHead extends BaseView
	
	className: 'head'
	
	entity: 'ui/me'
	
	template: templates.get 'play/head.tmpl'
	
return PlayHead