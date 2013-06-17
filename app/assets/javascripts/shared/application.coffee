Router = require 'router'
db = require 'database'

app =

	views: {}
    
	state: db.get 'ui/appState'

	start: ->
		app.router = new Router()
		Backbone.history.start pushState: true

	navigate: (path, options) ->
		@router.navigate path, options        
        
return app