BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'
events = require 'events'

class SmallLogin extends BaseView
	
	entity: 'ui/me'
	
	template: templates.get 'login/small.tmpl'

	events:
		'click .connect-google'   : 'connectGoogle'
		'click .connect-facebook' : 'connectFacebook'
		'click .connect-twitter'  : 'connectTwitter'

	bindEvents: ->
		@listenTo @model, 'change:isFetched change:isGuest change:isUser', @render
		events.on 'googleLogin facebookLogin', @oauthLogin, @

	connectGoogle: (event) ->
		event?.preventDefault()
		console.log 'Connect with google'
		window.open '/login/google', 'Connect', 'width=655,height=380,left=100,top=200,toolbar=no,scrollbars=no,menubar=no'
		return
		
	connectFacebook: (event) ->
		event?.preventDefault()
		console.log 'Connect with facebook'
		window.open '/login/facebook', 'Connect', 'width=655,height=380,left=100,top=200,toolbar=no,scrollbars=no,menubar=no'
		return
		
	connectTwitter: (event) ->
		event?.preventDefault()
		alert 'Not yet implemented...'
		return

	oauthLogin: ->
		console.log 'Authenticated via oauth provider'
		@model.initialize()
		app.navigate 'play', true
		return
	
return SmallLogin