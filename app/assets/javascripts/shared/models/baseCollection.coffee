db = require 'database'

###
    BaseCollection provides basic functionality for our collections.

    @author Sebastian Sachtleben
###
class BaseCollection extends Backbone.Collection

	initialize: ->
		@set 'isFetched', false

	fetch: (options) ->
		options = {} unless options?
		@id = options.id if options.id
		@set 'isFetched', false
		super options

	parse: (resp) ->
		db.add "db/#{key}", value for own key, value of resp
		@set 'isFetched', true
		super resp

return BaseCollection