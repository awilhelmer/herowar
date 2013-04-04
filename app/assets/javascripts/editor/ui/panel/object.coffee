BasePanel = require 'ui/panel/basePanel'
	
class ObjectPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-world'

return ObjectPropertiesPanel