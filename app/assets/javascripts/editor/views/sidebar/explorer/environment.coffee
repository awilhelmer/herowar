BaseView = require 'views/baseView'
templates = require 'templates'

class EnvironmentExplorer extends BaseView

	entity: 'environmentsStatic'

	template: templates.get 'sidebar/explorer/environment.tmpl'

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

return EnvironmentExplorer