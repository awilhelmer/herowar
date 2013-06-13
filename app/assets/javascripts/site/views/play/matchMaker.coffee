BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchMakerView extends BaseView
	
	className: 'match-maker'
				
	template: templates.get 'play/matchMaker.tmpl'
	
	entity: 'api/matchMaker'
	
return MatchMakerView