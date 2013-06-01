MeshModel = require 'models/mesh'

class LaserModel extends MeshModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 10
		
	constructor: (@id, @owner, @target, @damage) ->
		@meshBody = @createMeshBody()
		super @id, "Shot-#{@id}", @meshBody
		@getMainObject().position.copy @owner.getMainObject().position
		@getMainObject().quaternion.setFromRotationMatrix @owner.getMainObject().matrix

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 1, 1, 7.5
		material = new THREE.MeshPhongMaterial color: 0x0066CC, ambient: 0xffffff
		mesh = new THREE.Mesh geometry, material
		mesh.position.y = @_getCenterPointY @owner.meshBody
		mesh.overdraw = true
		return mesh

	update: (delta) ->
		targetPosition = @target.getMainObject().position.clone()
		targetPosition.y = @meshBody.position.y
		distance = @getMainObject().position.distanceTo targetPosition
		if distance > @distanceToDispose + @meshBody.position.y
			@rotateTo targetPosition, delta
			@move delta
		else
			@dispose()
			
	_getCenterPointY: (obj) ->
		return (Math.abs(obj.geometry.boundingBox.min.y) + obj.geometry.boundingBox.max.y) * obj.scale.y / 2

return LaserModel