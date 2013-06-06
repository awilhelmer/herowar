BaseGUI = require 'viewer/gui/base'

class AnimationsGUI extends BaseGUI
	
	constructor: (@target) ->
		super 'Animations'

	create: ->
		return @parent.addFolder @name

	isAllowed: ->
		return @target.meshBody instanceof THREE.MorphAnimMesh and _.isObject @target.meshBody.geometry.animations

return AnimationsGUI