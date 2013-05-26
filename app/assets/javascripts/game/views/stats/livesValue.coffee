BaseView = require 'views/baseView'
templates = require 'templates'

class StatsLivesValueView extends BaseView

	entity: 'ui/stats'
	
	template: templates.get 'stats/livesValue.tmpl'

	bindEvents: ->
		@listenTo @model, 'change:lives change:_active', @render

return StatsLivesValueView