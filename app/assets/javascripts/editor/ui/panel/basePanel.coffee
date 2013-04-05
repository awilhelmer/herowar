class BasePanel

	constructor: (@app, @id) ->
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
		sliders = @$container.find 'input.slider'
		$.each sliders, (index, value) ->
			$slider = $ value
			fdSlider.createSlider
				inp: value
				min: $slider.attr 'min'
				max: $slider.attr 'max'
				step: $slider.attr 'step'

return BasePanel