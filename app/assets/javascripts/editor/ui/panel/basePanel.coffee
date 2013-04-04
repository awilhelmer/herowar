class BasePanel

	constructor: (@app, @id) ->
		@$container = $ "##{@id}"
		throw "Dom element with id #{@id} not found" if @$container?.length is 0
		@container = @$container[0]
		@initialize()
		@bindEvents()

	initialize: ->

	bindEvents: ->

return BasePanel