BaseTool = require 'tools/baseTool'
scenegraph = require 'scenegraph'
db = require 'database'

class SelectObject extends BaseTool

	constructor: (@intersectHelper) ->
		@input = db.get 'input'
		@tool = db.get 'ui/tool'
		super()
	
	onMouseUp: (event) ->
		@_searchForObject() unless event.which isnt 1 and @input.get 'mouse_moved'	
		return

	_searchForObject: ->
		return @_handleSearchResult @intersectHelper.mouseIntersects scenegraph.scene().children

	_handleSearchResult: (resultList) ->
		return @onResultFound result.object for result in resultList when @isResultValid result.object
		return @onNoResultFound() 
		
return SelectObject