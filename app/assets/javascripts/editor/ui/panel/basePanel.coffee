BaseElement = require 'ui/baseElement'
	
class BasePanel extends BaseElement

	constructor: (@editor, @id) ->
		super @editor, @id

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