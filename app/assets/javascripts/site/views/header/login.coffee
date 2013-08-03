FormView = require 'views/formView'
templates = require 'templates'
app = require 'application'

###
    The Login shows our login part in home view.

    @author Sebastian Sachtleben
###
class Login extends FormView

	entity: 'ui/me'
	
	template: templates.get 'header/login.tmpl'

	url: '/login/username/auth'
	
	validateForm: ($Form) ->
		isValid = true
		inputUsername = $Form.find 'input[name="username"]'
		if inputUsername.val().length <= 4
			@setInputState inputUsername, 'error'
			$.gritter.add
				title: 'Login failed',
				text: 'The username doesn\'t exists.'
			isValid = false
		else
			@setInputState inputUsername, ''
		passwordUsername = $Form.find 'input[name="password"]'
		if passwordUsername.val().length <= 4
			@setInputState passwordUsername, 'error'
			if isValid
				$.gritter.add
					title: 'Login failed',
					text: 'The password doesn\'t match.'
			isValid = false
		else
			@setInputState passwordUsername, ''
		isValid
	
	onSuccess: (data, textStatus, jqXHR) ->
		console.log 'Success'
		@model.set data
		@model.validateResponse(data)
		app.navigate 'play', true
			
	onError: (jqXHR, textStatus, errorThrown) ->
		console.log 'Error'
		console.log jqXHR.responseText
		console.log $.parseJSON(jqXHR.responseText)
	
return Login