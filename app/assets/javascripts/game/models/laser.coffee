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
		mesh.position.y = @_getCenterPointY @owner.meshBody
		return mesh

	update: (delta) ->
		targetPosition = @target.root.position.clone()
		targetPosition.y = @meshBody.position.y
		distance = @root.position.distanceTo targetPosition
		if distance > @distanceToDispose + @meshBody.position.y
			@rotateTo targetPosition, delta
			@root.translateZ delta * @moveSpeed
		else
			@dispose()
			
	_getCenterPointY: (obj) ->
		return (Math.abs(obj.geometry.boundingBox.min.y) + obj.geometry.boundingBox.max.y) * obj.scale.y / 2

return LaserModel