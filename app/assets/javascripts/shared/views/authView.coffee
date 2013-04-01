BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'
db = require 'database'

###
    The AuthView shows temporarily the auth view untill we know if the user is logged in.

    @author Sebastian Sachtleben
###
class AuthView extends BaseView
	
	authTemplate: templates.get 'auth.tmpl'
	
	redirectTo: ''
	
	initialize: (options) ->
		@me = db.get 'ui/me'
		@realTemplate = @template
		@bindMeEvents()
		super()
	
	bindMeEvents: ->
		@listenTo @me, 'change:isGuest change:isUser change:isFetched', @render if @me
		
	render: ->
		if @me.get('isFetched') && @me.get('isGuest')
			app.navigate @redirectTo, true
		else
			if @me.get 'isUser'
				@template = @realTemplate
			else 
				@template = @authTemplate
			super()
	
return AuthView