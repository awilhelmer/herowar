BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Settings shows currently just Login and Logout Buttons.

    @author Sebastian Sachtleben
###
class Settings extends BaseView

	entity: 'ui/me'

	className: 'settings'
	
	template: templates.get 'header/settings.tmpl'
	
	bindEvents: ->
		@listenTo @model, 'change:isGuest change:isUser', @render if @model
	
	events:
		'click .logout-link': 'logout'
		
	logout: (event) ->
		if event
			event.preventDefault()
			$CurrentTarget = $ event.currentTarget
			$CurrentTarget.addClass 'disabled'
		$.ajax
			type: 'POST'
			url: "#{app.resourcePath()}logout"
			success: (data, textStatus, jqXHR) =>
				console.log 'Reset model'
				@model.reset()

return Settings