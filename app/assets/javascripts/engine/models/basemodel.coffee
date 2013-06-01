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
		object.quaternion.setFromRotationMatrix @getMainObject().matrix for scene, object of @root when scene isnt 'main'
		return
	
	move: (delta) ->
		@getMainObject().translateZ delta * @moveSpeed
		object.position.clone @getMainObject().position for scene, object of @root when scene isnt 'main'
	
	dispose: ->
		scenegraph = require 'scenegraph'
		scenegraph.removeDynObject @id
		return

	_cloneRoot: ->
		for scene in ['glow']
			@root[scene] = @root.main.clone()
			@_convertGlowMaterials @root[scene] if scene is 'glow'
		return
	
	_convertGlowMaterials: (obj) ->
		if obj instanceof THREE.Mesh
			glowMaterial = if obj.userData.glowing then @_getGlowOnMaterial() else @_getGlowOffMaterial()
			if obj.material instanceof THREE.MeshFaceMaterial
				materials = []
				materials.push new THREE.MeshBasicMaterial glowMaterial for material in obj.material.materials
				obj.material = new THREE.MeshFaceMaterial materials
			else obj.material = glowMaterial
		@_convertGlowMaterials child for child in obj.children if obj.children
		return

	_getGlowOnMaterial: ->
		return new THREE.MeshBasicMaterial color: 0x88ccff

	_getGlowOffMaterial: ->
		return new THREE.MeshBasicMaterial color: 'black'

return BaseModel 