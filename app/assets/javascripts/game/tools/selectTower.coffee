SelectObject = require 'tools/selectObject'
TowerModel = require 'models/scene/tower'

class SelectTower extends SelectObject

	onMouseUp: (event) ->
		super event unless @tool.has 'currentObject'
		return

	isResultValid: (object) ->
		return object?.userData?.model instanceof TowerModel

	onNoResultFound: ->
		@currentSelected.selected false if @currentSelected
		return

	onResultFound: (object) ->
		@currentSelected.selected false if @currentSelected
		@currentSelected = object.userData.model
		@currentSelected.selected true
		console.log 'Tower selection detected', @currentSelected
		return

return SelectTower