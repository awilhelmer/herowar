BaseStatsView = require 'views/stats/baseStatsView'
templates = require 'templates'

class StatsScoreView extends BaseStatsView

	statsName: 'score'
	
	template: templates.get 'stats/score.tmpl'

return StatsScoreView