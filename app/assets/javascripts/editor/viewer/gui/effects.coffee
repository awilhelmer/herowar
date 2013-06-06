BaseGUI = require 'viewer/gui/base'

class EffectsGUI extends BaseGUI
	
	constructor: (@target) ->
		super 'Effects'

	create: ->
		return @parent.addFolder @name
	
return EffectsGUI