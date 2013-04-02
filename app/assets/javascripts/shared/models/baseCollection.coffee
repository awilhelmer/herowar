###
    BaseCollection provides basic functionality for our collections.

    @author Sebastian Sachtleben
###
class BaseCollection extends Backbone.Collection

	fetch: (options) ->
		options = {} unless options?
		@id = options.id if options.id
		super options

return BaseCollection