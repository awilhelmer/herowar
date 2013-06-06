BaseGUI = require 'viewer/gui/base'

class AnimationsGUI extends BaseGUI
	
	constructor: (@target) ->
		super 'Animations'

	create: ->
		@root = @parent.addFolder @name
		@addAnimation anim for anim in @target.meshBody.geometry.animations
		return @root

	addAnimation: (name) ->
			unless _.has @children, name
				cb = (name) =>
					return () => @target.setAnimation name
				@model[name] = cb
				@children[name] = @root.add(@model, name).name name

	isAllowed: ->
		return @target.meshBody instanceof THREE.MorphAnimMesh and _.isObject @target.meshBody.geometry.animations

return AnimationsGUI