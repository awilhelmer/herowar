BaseStatsView = require 'views/stats/baseStatsView'
templates = require 'templates'
events = require 'events'

class StatsGoldView extends BaseStatsView

	statsName: 'gold'

	template: templates.get 'stats/gold.tmpl'

return StatsGoldView