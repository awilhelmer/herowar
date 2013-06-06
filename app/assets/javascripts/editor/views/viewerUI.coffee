BaseView = require 'views/baseView'
templates = require 'templates'

class ViewerUI extends BaseView

	id: 'viewerUI'
	
	className: 'hidden'
	
	entity: 'viewer'
	
	template: templates.get 'viewerUI.tmpl'
		
	bindEvents: ->
		@listenTo @model, 'fetching:data', @isFetching
		@listenTo @model, 'fetched:data', @isFetched
	
	isFetching: ->
		$span = @$el.find 'span'
		$span.text 'Loading'
		@$el.removeClass 'hidden'
		return
	
	isFetched: (obj) ->
		$span = @$el.find 'span'
		$span.text obj.name
		setTimeout =>
			@$el.addClass 'hidden'
		, 500
		return

return ViewerUI