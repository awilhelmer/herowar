BasePanel = require 'ui/panel/basePanel'
	
class WorldPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-world'

return WorldPropertiesPanel