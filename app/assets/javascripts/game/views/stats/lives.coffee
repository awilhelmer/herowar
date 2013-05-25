BaseView = require 'views/baseView'
templates = require 'templates'

class StatsLivesView extends BaseView

	className: 'item'

	entity: 'ui/stats'
	
	template: templates.get 'stats/lives.tmpl'

	bindEvents: ->
		@listenTo @model, 'change:lives', @render

return StatsLivesView