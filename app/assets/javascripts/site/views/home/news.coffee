BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class HomeNews extends BaseView
	
	entity: 'api/newss'
	
	template: templates.get 'home/news.tmpl'
	
	initialize: (options) ->
		super options
		@model.fetch()
		
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

return HomeNews