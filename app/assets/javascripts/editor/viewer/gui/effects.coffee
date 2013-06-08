Explosion = require 'effects/explosion'
BaseGUI = require 'viewer/gui/base'

class EffectsGUI extends BaseGUI
	
	constructor: (@target) ->
		super 'Effects'

	create: ->
		@root = @parent.addFolder @name
		@addExplosionEffect() if @target.explode
		@addShieldEffect() if @target.currentShield isnt 0
		return @root

	addShieldEffect: ->
			name = 'Shield'
			unless _.has @children, name
				cb = (target) =>
					return () => target.showShield()
				@model[name] = cb @target
				@children[name] = @root.add(@model, name).name name

	addExplosionEffect: ->
			name = 'Explosion'
			unless _.has @children, name
				cb = (target) =>
					return () => target.effects.push new Explosion target
				@model[name] = cb @target
				@children[name] = @root.add(@model, name).name name

return EffectsGUI