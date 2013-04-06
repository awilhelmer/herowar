Router = Backbone.Router.extend

    routes:
        "editor"  : "preloader"
        "editor2" : "editor"
	
    ###
        Overridden Backbone method to dynamically instanciate the controller with the given name
        the arguments given to the route are forwarded.
    ###
    _bindRoutes: ->
        return unless @routes
        # do is used to get a closure scope of value in the for loop
        (do(key, value) =>
            app = require 'application'
            @route key, value, (=>
                options = _.extend {}, arguments, @getQueryVariables()
                new (require "controllers/#{value}")(options))
            ) for key, value of @routes
        null

    ###
        Parse query parameter from current url.

        @return {Object} Object with all parameters as key and value pair.
    ###
    getQueryVariables: ->
        query = window.location.search.substring 1
        _.reduce query.split('&'), ((result,element)->
            [key,val]=(element.split '=')
            result[key] = unescape val
            result
            ),{}

return Router