BaseView = require 'views/baseView'
templates = require 'templates'

class List extends BaseView

	className: 'list'
		
	template: templates.get 'util/list.tmpl'

	initialize: (options) ->
		@entity = @$el.data 'entity'
		@$el.removeAttr 'data-entity'
		super options

return List