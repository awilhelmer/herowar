BaseModel = require 'models/scene/basemodel'
scenegraph = require 'scenegraph'

class MeshModel extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	boundingBoxMesh: null
		
	constructor: (attributes) ->
		attributes = _.defaults {}, attributes,
			selected: false
			selectedColor: '#000000'
		@id = attributes.id
		@name = attributes.name
		@meshBody = attributes.meshBody
		@meshBody.userData.model = @ if @meshBody?.userData
		obj = null
		if @meshBody instanceof THREE.Mesh or @meshBody instanceof THREE.MorphAnimMesh
			@_calculateGeometry @meshBody.geometry
			obj = @_createThreeObject @meshBody
		else
			obj = @meshBody
			@meshBody = obj.children[0] # TODO: this is super cheap and needs improvement
			@_calculateGeometry @meshBody.geometry
		@_enableShadows()
		attributes.root = obj
		@glowIsActive = false
		super attributes

	update: (delta, now) ->
		super delta, now
		#@checkGroundCollision()

	selected: (value) ->
		unless _.isUndefined value
			unless @meshSelection
				scale = @meshBody.scale
				boundingBox = @meshBody.geometry.boundingBox
				innerRadius = Math.ceil Math.max((boundingBox.max.x - boundingBox.min.x) * scale.x, (boundingBox.max.z - boundingBox.min.z) * scale.z) / 2 + 2
				outerRadius = innerRadius + 2
				material = new THREE.MeshBasicMaterial color: @attributes.selectedColor, opacity: 0.3, transparent: true
				geometry = new THREE.RingGeometry innerRadius, outerRadius, outerRadius * 2, innerRadius * 2
				@meshSelection = new THREE.Mesh geometry, material
				@meshSelection.name = 'selection'
				@meshSelection.position.y += 1.5
				@meshSelection.rotation.x = THREE.Math.degToRad -90
				@getMainObject().add @meshSelection
			@meshSelection.visible = value
		return @attributes.selected

	showGlow: (time) ->
		return if @glowIsActive
		@glowIsActive = true
		@enableGlow()
		setTimeout =>
			@disableGlow()
			@glowIsActive = false
		, time

	showBoundingBox: ->
		@boundingBoxMesh = @_createBoundingBox() unless @boundingBoxMesh
		@boundingBoxMesh.visible = true unless @boundingBoxMesh.visible
		return
		
	hideBoundingBox: ->
		@boundingBoxMesh.visible = false if @boundingBoxMesh?.visible
		return

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
		intersects = @raycaster.intersectObject scenegraph.getMap().getMainObject(), true
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

	_createBoundingBox: ->
		scale = @meshBody.scale
		boundingBox = @meshBody.geometry.boundingBox
		x = (boundingBox.max.x - boundingBox.min.x) * scale.x
		y = (boundingBox.max.y - boundingBox.min.y) * scale.y
		z = (boundingBox.max.z - boundingBox.min.z) * scale.z
		geometry = new THREE.CubeGeometry x, y, z 
		material = new THREE.MeshBasicMaterial color: 0xff0000, wireframe: true 
		mesh = new THREE.Mesh geometry, material
		mesh.name = 'boundingBox'
		mesh.position.copy @meshBody.position
		mesh.rotation.copy @meshBody.rotation
		@getMainObject().add mesh
		return mesh

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