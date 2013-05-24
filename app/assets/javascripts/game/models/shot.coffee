MeshModel = require 'models/mesh'

class ShotModel extends MeshModel
	
	rotationMultipler: 10
	
	moveSpeed: 50
	
	constructor: (@id, @owner, @target, @damage) ->
		geometry = new THREE.CubeGeometry 1, 10, 1
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		@meshBody = new THREE.Mesh geometry, material
		@meshBody.rotation.x = Math.PI/2
		super @id, "Shot-#{@id}", @meshBody
		@object3d.position.set @owner.object3d.position.x, @owner.object3d.position.y, @owner.object3d.position.z
		@object3d.quaternion.setFromRotationMatrix @owner.object3d.matrix

	update: (delta) ->
		distance = @object3d.position.distanceTo @target.object3d.position
		if distance > 2
			@rotateTo @target.object3d.position, delta
			@object3d.translateZ delta * @moveSpeed
		else
			@dispose()

return ShotModel