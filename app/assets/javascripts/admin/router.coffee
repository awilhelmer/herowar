###
    The Backbone.Router has been modified to instantiate the controller with the given name in the
    routes object.
    
    Each router gets 
    1. parameter defined in his route e.g. user/:id/tag/:tag named with the index number {0:id,1:tag}
    2. parameter as query parameter in the url with {name:value,name2:value2}

    author: Alexander Kong
    author: Sebastian Sachtleben
###
Router = Backbone.Router.extend

    ###
        The routes in our application.
    ###
    routes:
        "admin"            : "home"
        "admin/user/:id"   : "users/edit"
        "admin/user/all"   : "users/list"
        "admin/map/:id"    : "maps/edit"
        "admin/map/all"    : "maps/list"
        "admin/map/new"    : "maps/new"
        "admin/object/:id" : "objects/edit"
        "admin/object/all" : "objects/list"
        "admin/object/new" : "objects/new"
        "admin/news/:id"   : "news/edit"
        "admin/news/all"   : "news/list"
        "admin/news/new"   : "news/new"
	
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
                app.state.set "controller", value
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