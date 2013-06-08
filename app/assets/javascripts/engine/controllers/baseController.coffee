app = require 'application'
popup = require 'popup'

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
		popup.remove()
		activeViews = {}
		_.keys(app.views)
		refreshAll = @_isArrayEqual _.keys(app.views), _.keys(@views)
		for key, value of app.views
			if not refreshAll and @views.hasOwnProperty key then activeViews[key] = value else value.remove()
		activeViews[viewName] = @_appendView @_createView(viewName),@$body for viewName of @views when activeViews[viewName] is undefined
		app.views = activeViews
		return

	_appendView: (view, el) ->
		el.append view.$el
		view.render()
		return view

	_createView: (viewName) ->
		options = @[@views[viewName]]() if @views[viewName]
		new (require viewName) options

	_isArrayEqual: (a1, a2) ->
		return false unless _.isArray(a1) and _.isArray(a2) and a1.length is a2.length
		return true if _.difference(a1,a2).length is 0
		return false

return BaseController