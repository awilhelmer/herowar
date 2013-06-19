SelectObject = require 'tools/selectObject'
TowerModel = require 'models/scene/tower'

class SelectTower extends SelectObject

	onMouseUp: (event) ->
		super event unless @tool.has 'currentObject'
		return

	isResultValid: (object) ->
		return object?.userData?.model instanceof TowerModel

	onResultFound: (object) ->
		console.log 'Tower selection detected', object.userData.model
		return

return SelectTower