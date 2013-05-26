BaseStatsView = require 'views/stats/baseStatsView'
templates = require 'templates'

class StatsLivesView extends BaseStatsView

	statsName: 'lives'

	template: templates.get 'stats/lives.tmpl'

return StatsLivesView