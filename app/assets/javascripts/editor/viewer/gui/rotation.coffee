BaseGUI = require 'viewer/gui/base'

class RotationGUI extends BaseGUI
	
	rotation:
		x: 0
		y: 0
		z: 0
	
	constructor: (@target) ->
		super 'Rotation'

	create: ->
		@root = @parent.addFolder @name
		@children['x'] = @root.add(@rotation, 'x', -360, 360).listen().onChange =>
			@target.meshBody.rotation.x = THREE.Math.degToRad @rotation.x if @target
		@children['y'] = @root.add(@rotation, 'y', -360, 360).listen().onChange =>
			@target.meshBody.rotation.y = THREE.Math.degToRad @rotation.y if @target		
		@children['z'] = @root.add(@rotation, 'z', -360, 360).listen().onChange =>
			@target.meshBody.rotation.z = THREE.Math.degToRad @rotation.z if @target
		return @root
	
return RotationGUI