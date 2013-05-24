MeshModel = require 'models/mesh'

class LaserModel extends MeshModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 10
	
	constructor: (@id, @owner, @target, @damage) ->
		geometry = new THREE.CubeGeometry 1, 10, 1
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		@meshBody = @createMeshBody()
		super @id, "Shot-#{@id}", @meshBody
		@object3d.position.set @owner.object3d.position.x, @owner.object3d.position.y, @owner.object3d.position.z
		@object3d.quaternion.setFromRotationMatrix @owner.object3d.matrix

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 0.5, 7.5, 0.5
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		body = new THREE.Object3D()
		shot = new THREE.Mesh geometry, material
		body.add shot
		body.rotation.x = Math.PI/2
		return body
		

	update: (delta) ->
		distance = @object3d.position.distanceTo @target.object3d.position
		if distance > @distanceToDispose
			@rotateTo @target.object3d.position, delta
			@object3d.translateZ delta * @moveSpeed
		else
			@dispose()

return LaserModel