BaseView = require 'views/baseView'

class BasePropertiesView extends BaseView

	initialize: (options) ->
		@sliderCreated = false
		super options

	hidePanel: =>
		@$el.addClass 'hidden'

	showPanel: =>
		@$el.removeClass 'hidden'
		unless @sliderCreated
			@createSliders()
			@sliderCreated = true

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