BaseView = require 'views/baseView'

class BaseModelView extends BaseView

	initialize: (options) ->
		@inputListeners = []
		super options

	afterRender: =>
		@_processFormData()
		super()	

	remove: ->
		@_removeListeners()
		super()

	_processFormData: =>
		isBound = @inputListeners.length isnt 0
		console.log 'Change input field values for ', @$el
		@$el.find('input,textarea,select').each (index, element) =>
			$element = $ element
			@_processField $element
			@_addListener $element unless isBound
		console.log 'Model', @model.attributes
		return

	_processField: ($element) ->
		name = $element.attr 'name'
		if @model.has name
			value = @model.get name
			console.log 'Set input', name, 'to', value
			@_setFieldValue $element, value
		return
		
	_setFieldValue: ($element, value) ->
		if $element.is ':checkbox'
			$element.attr 'checked', value 
		else if $element.is 'select'
			$element.val value
		# TODO: handle input type text ...
		return

	_setModelValue: ($element) ->
		name = $element.attr 'name'
		if $element.is ':checkbox'
			console.log 'Changed input', name, 'to', $element.is ':checked'
			@model.set name, $element.is ':checked'
		else if $element.is 'select'
			console.log 'Changed input', name, 'to', $element.find(':selected').attr 'value'
			@model.set name, $element.find(':selected').attr 'value'
		# TODO: handle input type text ...
		return

	_onChange: (event) =>
		return unless event
		$target = $ event.currentTarget
		@_setModelValue $target
		console.log 'Model', @model.attributes
		return true

	_addListener: ($element) ->
		selector = "#{$element.get(0).tagName}[name='#{$element.attr('name')}']".toString()
		console.log 'Bind change on ', selector
		@$el.on 'change', selector, @_onChange
		@inputListeners.push selector
		return
		
	_removeListeners: ->
		for selector in @inputListeners
			console.log 'Unbind change on ', selector
			@$el.off 'change', selector, @_onChange
		@inputListeners.length = 0
		return
	
return BaseModelView