app = require 'application'

###
    The BaseController provides basic functionality for our controllers.

    @author Sebastian Sachtleben
###
class BaseController

	# OVERRIDE to register views, for special behavior just add views to app.views yourself
    views: {}
    
	# options is the arguments array passed from the router, so convert it to an object
    constructor: (options) ->
        @options = _.extend {}, options
        @$body = $ 'body'
        @initialize @options
    
    initialize: (options) ->
        @_registerViews()

    _registerViews: ->
        activeViews = {}
        #destroy views not required anymore and keep still required ones
        for key, value of app.views
            if @views.hasOwnProperty key then activeViews[key] = value else value.remove()
        #create all views in @views that are not already existing in app.views
        activeViews[viewName] = @_createView viewName for viewName of @views unless _.contains app.views, key
        app.views = activeViews
    
    _createView: (viewName) ->
        return app.views[viewName] if app.views[viewName]
        options = @[@views[viewName]]() if @views[viewName]
        View = require viewName
        newView = new View options
        @$body.append newView.$el
        newView.render()
        newView

return BaseController