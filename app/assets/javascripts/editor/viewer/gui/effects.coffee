BaseGUI = require 'viewer/gui/base'

class EffectsGUI extends BaseGUI
	
	constructor: (@target) ->
		super 'Effects'

	create: ->
		@root = @parent.addFolder @name
		@addShieldEffect() if @target.currentShield isnt 0
		return @root

	addShieldEffect: ->
			name = 'shield'
			unless _.has @children, name
				cb = (target) =>
					return () => target.showShield()
				@model[name] = cb @target
				@children[name] = @root.add(@model, name).name name
	
return EffectsGUI