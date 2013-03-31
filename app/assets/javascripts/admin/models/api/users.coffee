app = require 'application'

###
	Users provides a collection of user fetched by '/api/users/all'.

	@author Sebastian Sachtleben
###
class Users extends Backbone.Collection

    initialize: (models, options) ->
        @model = require "models/db/user"
        super models, options

    url: ->
        "#{app.resourcePath()}user/all"

    parse: (resp) ->
        return resp
    
return Users