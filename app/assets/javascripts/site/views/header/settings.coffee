BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Settings shows currently just Login and Logout Buttons.

    @author Sebastian Sachtleben
###
class Settings extends BaseView

	entity: 'ui/me'
	
	template: templates.get 'header/settings.tmpl'
	
	bindEvents: ->
		@listenTo @model, 'change:isGuest change:isUser', @render if @model
	
	events:
		'click .logout-link'	  : 'logout'
		'click .login-link'		  : 'toggleTooltip'
		'click .connect-google' : 'connectGoogle'
		
	logout: (event) ->
		if event
			event.preventDefault()
			$CurrentTarget = $ event.currentTarget
			$CurrentTarget.addClass 'disabled'
		$.ajax
			type: 'POST'
			url: '/api/logout'
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
		window.open '/login/google', 'GoogleConnect', 'width=655,height=380,left=100,top=200,toolbar=no,scrollbars=no,menubar=no'
		return

return Settings