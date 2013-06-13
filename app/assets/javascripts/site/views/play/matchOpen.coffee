BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class MatchOpenView extends BaseView
	
	className: 'match-open'
		
	entity: 'api/matchOpen'
		
	template: templates.get 'play/matchOpen.tmpl'
	
	initialize: (options) ->
		super options
		@model.fetch()
	
return MatchOpenView