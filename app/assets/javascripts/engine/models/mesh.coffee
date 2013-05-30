BaseModel = require 'models/basemodel'
scenegraph = require 'scenegraph'

class MeshModel extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	constructor: (@id, @name, @meshBody) ->
		@_enableShadows()
		super @_createThreeObject()

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
			@root.translateY intersects[0].distance
	
	_enableShadows: ->
		if _.isArray @meshBody
			for mesh in @meshBody
				mesh.castShadow = true
				mesh.receiveShadow = true
		else
			@meshBody.castShadow = true
			@meshBody.receiveShadow = true	
		return

	_createThreeObject: ->
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		if _.isArray @meshBody
			for mesh in @meshBody
				obj.add mesh
		else
			obj.add @meshBody
		return obj

return MeshModel