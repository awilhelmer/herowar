BaseView = require 'views/baseView'
db = require 'database'

class BasePropertiesView extends BaseView

	initialize: (options) ->
		@sidebar = db.get 'ui/sidebar'
		@sliderCreated = false
		super options

	bindEvents: ->
		@listenTo @sidebar, 'change:active', @changeActiveView
		super()

	changeActiveView: ->
		setTimeout =>
			if not @sliderCreated and @sidebar.get('active') is @$el.attr('id') 
				@createSliders()
				@sliderCreated = true
		, 100

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