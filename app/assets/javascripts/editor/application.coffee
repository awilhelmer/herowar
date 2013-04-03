Engine = require 'engine'
###

###
app =

	
	###
		Main entry point for our application.
	###
	start: ->
		app.engine = new Engine()
		app.engine.start()
        
return app