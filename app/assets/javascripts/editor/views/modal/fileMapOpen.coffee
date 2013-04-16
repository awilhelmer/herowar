BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class ModalFileMapOpen extends BaseView

	id: 'modalFileMapOpen'
	
	className: 'modal hide fade'
	
	entity: 'maps'
		
	template: templates.get 'modal/fileMapOpen.tmpl'
	
	initialize: (options) ->
		super options
		@model.fetch()

return ModalFileMapOpen