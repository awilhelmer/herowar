SmallLogin = require 'views/login/small'
templates = require 'templates'

class LargeLogin extends SmallLogin
	
	template: templates.get 'login/large.tmpl'
	
return LargeLogin