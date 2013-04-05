class BasePanel

	constructor: (@editor, @id) ->
		@$container = $ "##{@id}"
		throw "Dom element with id #{@id} not found" if @$container?.length is 0
		@container = @$container[0]
		@showFirstTime = true
		@initialize()
		@bindEvents()

	initialize: ->

	bindEvents: ->

	hide: ->
		@$container.addClass 'hidden'

	show: ->
		@$container.removeClass 'hidden'
		if @showFirstTime
			@createSliders()
			@showFirstTime = false
		
	createSliders: ->

	createSlider: (domElement, min, max, step, callbackFunc) ->
		fdSlider.createSlider
			inp: domElement
			min: min
			max: max
			step: step
			callbacks:
				update:
					[callbackFunc]

return BasePanel