class BaseCollection extends Backbone.Collection
	
	fetch: (options) ->
		opt = {}
		_.extend opt, options
		_.defaults opt,
			complete : =>
				@trigger 'fetched'
		@trigger 'fetching'
		super opt
		
return BaseCollection