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
	
	afterRender: =>
		if @model.get 'token'
			$('.map-select').addClass 'hidden'
			$('.match-maker').addClass 'hidden'
			@$el.removeClass 'hidden'
		else
			$('.map-select').removeClass 'hidden'
			$('.match-maker').removeClass 'hidden'
			@$el.addClass 'hidden'
	
return MatchOpenView