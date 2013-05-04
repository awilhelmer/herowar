BaseView = require 'views/baseView'

class BasePropertiesView extends BaseView

	initialize: (options) ->
		@sliderCreated = false
		super options

	createSliders: ->
		# Should be overridden

	createSlider: (domElement, min, max, step, callbackFunc) ->
		fdSlider.createSlider
			inp: domElement
			min: min
			max: max
			step: step
			callbacks:
				update:
					[callbackFunc]

	render: ->
		super()
		unless @sliderCreated
			@createSliders()
			@sliderCreated = true

	onChangedString: (event) ->
		unless event then return
		event.preventDefault()
		entry = @getEntry event, ''
		@model.set entry.property, entry.value

	onChangedInteger: (event) ->
		unless event then return
		event.preventDefault()
		entry = @getEntry event, ''
		@model.set entry.property, parseInt(entry.value)

return BasePropertiesView