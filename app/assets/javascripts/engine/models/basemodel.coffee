GeometryUtils = require 'util/geometryUtils'

class BaseModel

	rotationMultipler: null

	moveSpeed: 30

	constructor: (root) ->
		@root = {}
		@root.main = root
		@_cloneRoot()
	
	update: (delta) ->
		return

	getMainObject: ->
		return @root.main
		
	rotateTo: (position, delta) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @getMainObject().position, @getMainObject().up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		multipler = if @rotationMultipler then @rotationMultipler else @moveSpeed / 10
		@getMainObject().quaternion.slerp dq, delta * multipler
		obj.quaternion.copy @getMainObject().quaternion for scene, obj of @root when scene isnt 'main'
		return
	
	move: (delta) ->
		@getMainObject().translateZ delta * @moveSpeed
		obj.position.copy @getMainObject().position for scene, obj of @root when scene isnt 'main'
		return
	
	position: (position) ->
		if position
			for scene, obj of @root 
				obj.position.copy position 
				console.log 'New position:', obj
		return @getMainObject().position
	
	visible: (value) ->
		obj.visible = value for scene, obj of @root if value
		return @getMainObject().visible
	
	dispose: ->
		scenegraph = require 'scenegraph'
		scenegraph.removeDynObject @id
		return

	_cloneRoot: ->
		for scene in ['glow']
			@root[scene] = @_copyObject null, @root.main, scene
		#console.log 'Compare', @root.main.children[0].geometry, @root.glow.children[0].geometry
		return

	_copyObject: (destObject, srcObject, scene) ->
		if srcObject instanceof THREE.Scene
			destObject = new THREE.Scene
		else if srcObject instanceof THREE.MorphAnimMesh 
			material = if scene is 'glow' then @_getGlowMaterials srcObject else srcObject.material.clone()
			geometry = GeometryUtils.clone srcObject.geometry
			destObject	= new THREE.MorphAnimMesh geometry, material
			destObject.parseAnimations()
		else if srcObject instanceof THREE.Mesh
			material = if scene is 'glow' then @_getGlowMaterials srcObject else srcObject.material.clone()
			geometry = GeometryUtils.clone srcObject.geometry
			destObject	= new THREE.Mesh geometry, material
		else if srcObject instanceof THREE.Object3D
			destObject	= new THREE.Object3D()
		destObject.position.copy srcObject.position
		destObject.rotation.copy srcObject.rotation
		destObject.scale.copy srcObject.scale
		destObject.userData = _.clone srcObject.userData
		if srcObject.useQuaternion
			destObject.quaternion.copy srcObject.quaternion
			destObject.useQuaternion = true
		destObject.add @_copyObject null, srcChild, scene for srcChild in srcObject.children if srcObject.children.length isnt 0
		return destObject
	
	_copyUserData: (objDestination, objSource) ->
		for i in [0..objSource.children.length-1]
			objDestination.children[i].userData = _.clone objSource.children[i].userData if objSource.children[i] instanceof THREE.Mesh
			@_copyUserData objSource.children[i], objDestination.children[i] if objSource.children[i].children.length isnt 0
		return
	
	_getGlowMaterials: (obj) ->
		if obj instanceof THREE.Mesh
			isAnimated = obj instanceof THREE.MorphAnimMesh
			glowMaterial = if obj.userData.glowing then @_getGlowOnMaterial isAnimated else @_getGlowOffMaterial isAnimated
			if obj.material instanceof THREE.MeshFaceMaterial
				materials = []
				for mat in obj.material.materials
					if materials.length is 0 
						materials.push glowMaterial
					else
						materials.push glowMaterial.clone()
				return new THREE.MeshFaceMaterial materials
			else return glowMaterial
		return null
	
	_getGlowOnMaterial: (isAnimated) ->
		material = new THREE.MeshBasicMaterial color: 0x88ccff
		if isAnimated
			material.morphTargets = true
			material.morphNormals = true
		return material

	_getGlowOffMaterial: (isAnimated) ->
		material = new THREE.MeshBasicMaterial color: 'black'
		if isAnimated
			material.morphTargets = true
			material.morphNormals = true
		return material

return BaseModel 