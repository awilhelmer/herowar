BaseView = require 'views/baseView'
templates = require 'templates'

class StatsGoldView extends BaseView

	className: 'item'

	entity: 'ui/stats'
	
	template: templates.get 'stats/gold.tmpl'

	bindEvents: ->
		@listenTo @model, 'change:gold', @render

return StatsGoldView