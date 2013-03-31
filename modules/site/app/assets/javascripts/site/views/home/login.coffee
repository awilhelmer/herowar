BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Login shows our login part in home view.

    @author Sebastian Sachtleben
###
class Login extends BaseView

	entity: 'ui/me'

	id: 'login'
	
	template: templates.get 'home/login.tmpl'

	events:
		"submit .login-form": 'login'
		
	bindEvents: ->
		@listenTo @model, 'change:isGuest change:isUser', @render if @model
	
	login: (event) ->
		event?.preventDefault()
		$.ajax
			dataType: 'json'
			type: 'POST'
			url: "#{app.resourcePath()}login"
			data:
				'username' : $("input[name='username']").val()
				'password' : $("input[name='password']").val()
			success: (data) =>
				console.log 'Success'
				@model.set data
				@model.validateResponse(data)
				app.navigate 'play', true 
			error: (jqXHR, textStatus, errorThrown) =>
				console.log 'Error'
				console.log textStatus
				console.log errorThrown
	
return Login