BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The AuthView shows temporarily the auth view untill we know if the user is logged in.

    @author Sebastian Sachtleben
###
class AuthView extends BaseView

	entity: 'ui/me'
	
	authTemplate: templates.get 'auth.tmpl'
	
	redirectTo: ''
	
	bindEvents: ->
		@listenTo @model, 'change:isGuest change:isUser change:isFetched', @render if @model
	
	initialize: (options) ->
		@realTemplate = @template
		super()
		
	render: ->
		console.log 'Render auth'
		console.log @model
		if @model.get('isFetched') && @model.get('isGuest')
			app.navigate @redirectTo, true
		else
			if @model.get 'isUser'
				@template = @realTemplate
			else 
				@template = @authTemplate
			super()
	
return AuthView