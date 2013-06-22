SelectObject = require 'tools/selectObject'
UnitModel = require 'models/scene/enemy'
events = require 'events'

class SelectUnit extends SelectObject

	onMouseUp: (event) ->
		super event unless @tool.has 'currentObject'
		return

	isResultValid: (object) ->
		return object?.userData?.model instanceof UnitModel

	onNoResultFound: ->
		@currentSelected.selected false if @currentSelected
		events.trigger 'unit:deselect'
		return

	onResultFound: (object) ->
		@currentSelected.selected false if @currentSelected
		@currentSelected = object.userData.model
		@currentSelected.selected true
		events.trigger 'unit:select', @currentSelected
		console.log 'Unit selection detected', @currentSelected
		return

return SelectUnit