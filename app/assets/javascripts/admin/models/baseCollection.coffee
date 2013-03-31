db = require 'database'

###
    BaseCollection provides basic functionality for our collections.

    @author Sebastian Sachtleben
###
class BaseCollection extends Backbone.Collection

    ###
        Fetch content from server and set global id if options contains a id. The credentials will be added before
        the fetch process starts.

        @param {Object} The fetch options.
    ###
    fetch: (options) ->
        options = {} unless options?
        @id = options.id if options.id
        super options

    ###
        Parse response and set each attribute of response in the responding db collection.

        @param {Object} The response to parse.
    ###
    parse: (resp) ->
        db.add "db/#{key}", value for own key, value of resp
        super resp

return BaseCollection