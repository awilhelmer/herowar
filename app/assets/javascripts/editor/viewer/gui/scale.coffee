BaseGUI = require 'viewer/gui/base'

class RotationGUI extends BaseGUI
	
	scale:
		x: 1.0
		y: 1.0
		z: 1.0
	
	multiplier:
		x: 1.0
		y: 1.0
		z: 1.0
		keepRatio: true
	
	constructor: (@target) ->
		super 'Scale'

	create: ->
		@root = @parent.addFolder @name
		@children['x'] = @root.add(@multiplier, 'x', 0, 100).listen().onChange =>
			if @target
				if @multiplier.keepRatio
					@multiplier.y = @multiplier.x
					@multiplier.z = @multiplier.x
				@updateScale()
		@children['y'] = @root.add(@multiplier, 'y', 0, 100).listen().onChange =>
			if @target
				if @multiplier.keepRatio
					@multiplier.x = @multiplier.y
					@multiplier.z = @multiplier.y
				@updateScale()
		@children['z'] = @root.add(@multiplier, 'z', 0, 100).listen().onChange =>
			if @target
				if @multiplier.keepRatio
					@multiplier.x = @multiplier.z
					@multiplier.y = @multiplier.z
				@updateScale()
		@children['keepRatio'] = @root.add(@multiplier, 'keepRatio').listen()
		return @root

	updateScale: ->
		@target.meshBody.scale.set THREE.Math.degToRad(@scale.x * @multiplier.x), THREE.Math.degToRad(@scale.y * @multiplier.y), THREE.Math.degToRad(@scale.z * @multiplier.z)
		return
	
return RotationGUI