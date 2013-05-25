BaseView = require 'views/baseView'
templates = require 'templates'

class StatsScoreView extends BaseView

	className: 'item'

	entity: 'ui/stats'
	
	template: templates.get 'stats/score.tmpl'

	bindEvents: ->
		@listenTo @model, 'change:score', @render

return StatsScoreView