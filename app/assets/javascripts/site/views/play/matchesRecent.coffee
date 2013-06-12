DateFormat = require 'util/dateFormat'
BaseView = require 'views/baseView'
templates = require 'templates'

class MatchesRecentView extends BaseView
	
	className: 'matches-recent'
	
	entity: 'ui/matchesRecent'
	
	template: templates.get 'play/matchesRecent.tmpl'

	getTemplateData: ->
		json = super()
		entry.formattedCdate = DateFormat.format new Date(entry.cdate), 'ddd mmm dd hh:nn:ss' for entry in json
		return json
	
return MatchesRecentView