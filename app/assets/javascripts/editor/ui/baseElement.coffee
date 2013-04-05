Toolbar = require 'ui/toolbar'
	
class BaseElement

	constructor: (@editor, @id) ->
		@$container = $ "##{@id}"
		throw "Dom element with id #{@id} not found" if @$container?.length is 0
		@container = @$container[0]
		@showFirstTime = true
		@initialize()
		@bindEvents()

	initialize: ->

	bindEvents: ->

return BaseElement