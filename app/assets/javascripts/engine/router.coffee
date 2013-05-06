Router = Backbone.Router.extend

    _bindRoutes: ->
        return unless @routes
        (do(key, value) =>
            app = require 'application'
            @route key, value, (=>
                options = _.extend {}, arguments, @getQueryVariables()
                new (require "controllers/#{value}")(options))
            ) for key, value of @routes
        null

    getQueryVariables: ->
        query = window.location.search.substring 1
        _.reduce query.split('&'), ((result,element)->
            [key,val]=(element.split '=')
            result[key] = unescape val
            result
            ),{}

return Router