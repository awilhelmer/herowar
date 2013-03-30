BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Signup shows the registration form for new users.

    @author Sebastian Sachtleben
###
class Signup extends BaseView

	id: 'signup'
	
	template: templates.get 'signup.tmpl'
	
	events:
		"submit .signup-form": 'signup'
	
	initialize: (options) ->
		@requestInProgress = false
		super options
	
	signup: (event) ->
		event?.preventDefault()
		if !@requestInProgress
			$Form = $ '.signup-form'
			$Button = $Form.find 'button'
			@requestInProgress = true
			$Button.addClass 'disabled'
			$.ajax
				dataType: 'json'
				type: 'POST'
				url: "#{app.resourcePath()}signup"
				data:
					'username' 				: $Form.find("#inputUsername").val()
					'password' 				: $Form.find("#inputPassword").val()
					'repeatPassword' 	: $Form.find("#inputRepeatPassword").val()
					'email'    				: $Form.find("#inputEmail").val()
					'newsletter'			: $Form.find("#newsletter").val()
				success: (resp) =>
					console.log 'Success'
					console.log resp
					# TODO: save user to database
					app.navigate '', true 
				error: (jqXHR, textStatus, errorThrown) =>
					console.log 'Error'
					console.log $.parseJSON(jqXHR.responseText)
				complete: =>
					@requestInProgress = false
					$Button.removeClass 'disabled'
				
	
return Signup