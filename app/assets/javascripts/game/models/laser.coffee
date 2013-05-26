MeshModel = require 'models/mesh'

class LaserModel extends MeshModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 10
	
	constructor: (@id, @owner, @target, @damage) ->
		@meshBody = @createMeshBody()
		super @id, "Shot-#{@id}", @meshBody
		@root.position.set @owner.root.position.x, @owner.root.position.y, @owner.root.position.z
		@root.quaternion.setFromRotationMatrix @owner.root.matrix

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 1, 1, 7.5
		material = new THREE.MeshBasicMaterial color: 0x0066CC
		mesh = new THREE.Mesh geometry, material
		mesh.position.y = @_getOwnerCenterPointY()
		return mesh

	update: (delta) ->
		distance = @root.position.distanceTo @target.root.position
		if distance > @distanceToDispose
			@rotateTo @target.root.position, delta
			@root.translateZ delta * @moveSpeed
		else
			@dispose()
			
	_getOwnerCenterPointY: ->
		return (Math.abs(@owner.meshBody.geometry.boundingBox.min.y) + @owner.meshBody.geometry.boundingBox.max.y) * @owner.meshBody.scale.y / 2

return LaserModel