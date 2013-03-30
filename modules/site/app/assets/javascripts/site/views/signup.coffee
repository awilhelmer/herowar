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
					
	onSuccess: (data, textStatus, jqXHR) ->
		console.log 'Success'
		console.log data
		app.navigate '', true
			
	onError: (jqXHR, textStatus, errorThrown) ->
		console.log 'Error'
		console.log $.parseJSON(jqXHR.responseText)
	
return Signup