SelectObject = require 'tools/selectObject'
TowerModel = require 'models/scene/tower'
events = require 'events'

class SelectTower extends SelectObject

	onMouseUp: (event) ->
		super event unless @tool.has 'currentObject'
		return

	isResultValid: (object) ->
		return object?.userData?.model instanceof TowerModel

	onNoResultFound: ->
		@currentSelected.selected false if @currentSelected
		events.trigger 'tower:deselect'
		return

	onResultFound: (object) ->
		@currentSelected.selected false if @currentSelected
		@currentSelected = object.userData.model
		@currentSelected.selected true
		events.trigger 'tower:select', @currentSelected
		console.log 'Tower selection detected', @currentSelected
		return

return SelectTower