BaseView = require 'views/baseView'

class BaseModelView extends BaseView

	afterRender: =>
		@processFormData()
		super()	

	processFormData: ->
		console.log 'Change input field values for ', @$el
		@$el.find('input,textarea,select').each (index, element) =>
			$element = $ element
			@processField $element
		return

	processField: ($element) ->
		[ model, property ] = @analyseFieldName $element
		if @model.has property
			value = @model.get property
			console.log 'Process', model, property, 'Value=', value
			console.log 'Checkbox=', $element.is(':checkbox'), 'Select=', $element.is('select'), 'Input=', $element.is(':input')
			@setFieldValue $element, value
		return
		
	setFieldValue: ($element, value) ->
		if $element.is ':checkbox'
			if value then $element.attr('checked', 'checked') else $element.attr('checked', '')
		else if $element.is 'select'
			$element.find("option[value='#{value}']").attr 'selected', 'selected'
		return

	analyseFieldName: ($element) ->
		name = $element.attr 'name'
		split = name.split '-'
		property = split.pop()
		model = split.join '/'
		return [ model, property ]
	
return BaseModelView