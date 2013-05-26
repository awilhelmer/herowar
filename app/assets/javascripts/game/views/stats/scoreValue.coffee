BaseView = require 'views/baseView'
templates = require 'templates'

class StatsScoreValueView extends BaseView

	entity: 'ui/stats'
	
	template: templates.get 'stats/scoreValue.tmpl'

	bindEvents: ->
		@listenTo @model, 'change:score change:_active', @render

return StatsScoreValueView