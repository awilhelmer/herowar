###
    author: Sebastian Sachtleben
###
app =
	###
		Main entry point for our application.
	###
	start: ->
		app.router = new Router();
		Backbone.history.start pushState: true
        
return app