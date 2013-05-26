BaseView = require 'views/baseView'
templates = require 'templates'

class StatsGoldValueView extends BaseView

	entity: 'ui/stats'
	
	template: templates.get 'stats/goldValue.tmpl'

	bindEvents: ->
		@listenTo @model, 'change:gold change:_active', @render

return StatsGoldValueView