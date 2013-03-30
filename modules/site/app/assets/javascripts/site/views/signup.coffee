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
		'submit form'									: 'submitForm'
		'change #inputUsername'				: 'validateUsername'
		'change #inputPassword'				: 'validatePassword'
		'change #inputEmail'					: 'validateEmail'
	
	onSuccess: (data, textStatus, jqXHR) ->
		console.log 'Success'
		console.log data
		app.navigate '', true
			
	onError: (jqXHR, textStatus, errorThrown) ->
		console.log 'Error'
		console.log $.parseJSON(jqXHR.responseText)
	
	validateUsername: (event) ->
		if event
			$CurrentTarget = $ event.currentTarget
			username = $CurrentTarget.val()
			if username.length <= 4
				@setInputState $CurrentTarget, 'error', 'Username must be at least 5 characters'
			else
				@setInputState $CurrentTarget, '', 'Valdating ...'
				$.ajax
					dataType: @dataType
					type: @type
					url: "#{app.resourcePath()}checkUsername/#{username}"
					success: (data, textStatus, jqXHR) =>
						@setInputState $CurrentTarget, 'success', 'This username looks great.' unless data
						@setInputState $CurrentTarget, 'error', 'This username is already taken.' if data

	validatePassword: (event) ->
		if event
			$CurrentTarget = $ event.currentTarget
			password = $CurrentTarget.val()
			if password.length <= 5
				@setInputState $CurrentTarget, 'error', 'Password must be at least 6 characters'
			else
				@setInputState $CurrentTarget, 'success', 'Password is okay'

	validateEmail: (event) ->
		if event
			$CurrentTarget = $ event.currentTarget
			email = $CurrentTarget.val()
			regex = /\S+@\S+\.\S+/
			if !regex.test(email)
				@setInputState $CurrentTarget, 'error', 'Doesn\'t look like a valid email.'
			else
				@setInputState $CurrentTarget, '', 'Valdating ...'
				$.ajax
					dataType: @dataType
					type: @type
					url: "#{app.resourcePath()}checkEmail/#{email}"
					success: (data, textStatus, jqXHR) =>
						@setInputState $CurrentTarget, 'success', 'Email is ok' unless data
						@setInputState $CurrentTarget, 'error', 'Email is taken' if data
	
return Signup