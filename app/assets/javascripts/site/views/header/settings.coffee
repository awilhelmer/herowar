BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'
events = require 'events'

###
    The Settings shows currently just Login and Logout Buttons.

    @author Sebastian Sachtleben
###
class Settings extends BaseView

	entity: 'ui/me'
	
	template: templates.get 'header/settings.tmpl'
	
	bindEvents: ->
		@listenTo @model, 'change:isFetched change:isGuest change:isUser', @render
		events.on 'googleLogin facebookLogin', @oauthLogin, @
	
	events:
		'click .logout-link'	    : 'logout'
		'click .login-link'		    : 'toggleTooltip'
		'click .connect-google'   : 'connectGoogle'
		'click .connect-facebook' : 'connectFacebook'
		
	logout: (event) ->
		if event
			event.preventDefault()
			$CurrentTarget = $ event.currentTarget
			$CurrentTarget.addClass 'disabled'
		$.ajax
			type: 'GET'
			url: '/logout'
			success: (data, textStatus, jqXHR) =>
				console.log 'Reset model'
				@model.reset()

	toggleTooltip: (event) ->
		$Tooltip = $ '.login-tooltip'
		$Tooltip.toggleClass 'visible'
		$UsernameInput = $ '.login-form input[name="username"]'
		$UsernameInput.focus() if $UsernameInput?.length > 0

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

	oauthLogin: ->
		console.log 'Authenticated via oauth provider'
		@model.initialize()
		app.navigate 'play', true
		return

return Settings