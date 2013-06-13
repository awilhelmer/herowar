BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchJoinView extends BaseView
	
	className: 'match-join'
				
	template: templates.get 'play/matchJoin.tmpl'
	
	entity: 'api/matchJoin'
	
	events:
		'click' : 'gameJoin'

	gameJoin: (event) ->
		@model.clear()
		@model.fetch()

return MatchJoinView