BaseGUI = require 'viewer/gui/base'

class RootGUI extends BaseGUI

	removable: false
	
	constructor: ->
		@root = new dat.GUI()
		super()

return RootGUI