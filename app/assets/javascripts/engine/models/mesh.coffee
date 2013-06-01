BaseModel = require 'models/basemodel'
scenegraph = require 'scenegraph'

class MeshModel extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	constructor: (@id, @name, @meshBody) ->
		obj = null
		if @meshBody instanceof THREE.Mesh or @meshBody instanceof THREE.MorphAnimMesh
			@_calculateGeometry @meshBody.geometry
			obj = @_createThreeObject @meshBody
		else
			obj = @meshBody
			@meshBody = obj.children[0] # TODO: this is super cheap and needs improvement
			@_calculateGeometry @meshBody.geometry
		@_enableShadows()
		super obj

	update: (delta) ->
		super delta
		#@checkGroundCollision()

	checkGroundCollision: ->
		unless @groundDirection
			@groundDirection = new THREE.Vector3 0, -1, 0
		position = new THREE.Vector3()
		position.getPositionFromMatrix @meshBody.matrixWorld
		position.y += @meshBody.geometry.boundingBox.min.y * -1
		unless @groundArrowHelper
			console.log 'BoundingBox', @meshBody.geometry.boundingBox
			@groundArrowHelper = new THREE.ArrowHelper @groundDirection, position, @meshBody.geometry.boundingBox.max.y - @meshBody.geometry.boundingBox.min.y
			scenegraph.scene().add @groundArrowHelper
		@groundArrowHelper.position.copy position
		if @raycaster
			@raycaster.set position, @groundDirection
		else
			@raycaster = new THREE.Raycaster position, @groundDirection
		intersects = @raycaster.intersectObject scenegraph.getMap(), true
		if intersects.length isnt 0 and intersects[0].distance < @meshBody.geometry.boundingBox.max.y
			console.log 'Ground Collision detected', intersects[0].distance, intersects[0]
			@getMainObject().translateY intersects[0].distance

	_calculateGeometry: (geometry) ->
		unless geometry.boundingBox
			geometry.computeBoundingBox()
			geometry.computeBoundingSphere()
			geometry.computeMorphNormals()
	
	_enableShadows: ->
		if _.isArray @meshBody
			for mesh in @meshBody
				mesh.castShadow = true
				mesh.receiveShadow = true
		else
			@meshBody.castShadow = true
			@meshBody.receiveShadow = true	
		return

	_createThreeObject: (mesh) ->
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		if _.isArray mesh
			for mesh in mesh
				obj.add mesh
		else
			obj.add mesh
		return obj

return MeshModel