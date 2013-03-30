BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Signup shows the registration form for new users.

    @author Sebastian Sachtleben
###
class Signup extends BaseView

	id: 'signup'
	
	template: templates.get 'signup.tmpl'
	
	events:
		"submit .signup-form": 'signup'
	
	signup: (event) ->
		event?.preventDefault()
		$.ajax
			dataType: 'json'
			type: 'POST'
			url: "http://localhost:9000/api/signup"
			data:
				'username' 			: $("input[name='username']").val()
				'password' 			: $("input[name='password']").val()
				'repeatPassword' 	: $("input[name='repeatPassword']").val()
				'email'    			: $("input[name='email']").val()
			success: (resp) =>
				console.log resp
	
return Signup