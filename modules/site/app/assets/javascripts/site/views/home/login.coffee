BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Home shows our home start view.

    @author Sebastian Sachtleben
###
class Login extends BaseView

	id: 'login'
	
	template: templates.get 'home/login.tmpl'

	events:
		"submit .login-form": 'login'
	
	login: (event) ->
		event?.preventDefault()
		$.ajax
			dataType: 'json'
			type: 'POST'
			url: "http://localhost:9000/api/login"
			data:
				'username' : $("input[name='username']").val()
				'password' : $("input[name='password']").val()
			success: (resp) =>
				console.log resp
	
return Login