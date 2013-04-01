Router = require 'router'
db = require 'database'

###
	The Application start our application (will be called from loader) and contains important informations like
    host, resourcePath, current active views and our application state.

    author: Sebastian Sachtleben
    author: Alexander Kong
###
app =
	###
		Get resource path to our json server.
	###
	resourcePath: -> "http://localhost:9000/api/"	
	
	###
		Current active views.
	###
	views: {}
    
	###
		The model that contains which page is open.
	###
	state: db.get 'ui/appState'

	###
		Main entry point for our application.
	###
	start: ->
		app.router = new Router()
		Backbone.history.start pushState: true

	###
		Navigate to path (wrapper for router.navigate).

		@param {String} The path to navigate.
		@param {Object} The options object.
	###
	navigate: (path, options) ->
		@router.navigate path, options        
        
return app