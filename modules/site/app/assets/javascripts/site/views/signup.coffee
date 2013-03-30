FormView = require 'views/formView'
templates = require 'templates'
app = require 'application'

###
    The Signup shows the registration form for new users.

    @author Sebastian Sachtleben
###
class Signup extends FormView

	id: 'signup'
	
	template: templates.get 'signup.tmpl'
	
	url: "#{app.resourcePath()}signup"
	
	events:
		'submit form'						: 'submitForm'
		'change #inputUsername'	: 'changedUsername'
		'change #inputEmail'		: 'changedEmail'
	
	changedUsername: (event) ->
		if event
			$CurrentTarget = $ event.currentTarget
			username = $CurrentTarget.val()
			$.ajax
				dataType: @dataType
				type: @type
				url: "#{app.resourcePath()}checkUsername/#{username}"
				success: (data, textStatus, jqXHR) =>
					@setInputState $CurrentTarget, 'success', 'Username is ok' unless data
					@setInputState $CurrentTarget, 'error', 'Username is taken' if data

	changedEmail: (event) ->
		if event
			$CurrentTarget = $ event.currentTarget
			email = $CurrentTarget.val()
			isValid = @validateEmail email
			if !isValid
				@setInputState $CurrentTarget, 'error', 'Email is not valid'
			else
				$.ajax
					dataType: @dataType
					type: @type
					url: "#{app.resourcePath()}checkEmail/#{email}"
					success: (data, textStatus, jqXHR) =>
						@setInputState $CurrentTarget, 'success', 'Email is ok' unless data
						@setInputState $CurrentTarget, 'error', 'Email is taken' if data
	
	validateEmail: (email) ->
		regex = /\S+@\S+\.\S+/
		regex.test email
					
	onSuccess: (data, textStatus, jqXHR) ->
		console.log 'Success'
		console.log data
		app.navigate '', true
			
	onError: (jqXHR, textStatus, errorThrown) ->
		console.log 'Error'
		console.log $.parseJSON(jqXHR.responseText)
	
return Signup