objectUtils = require 'util/objectUtils'

class BaseModel

	rotationMultipler: null

	moveSpeed: 30
	
	glowColor: 0x88ccff
	
	constructor: (attributes) ->
		@attributes = _.defaults {}, attributes, 
			rotationSpeed : 0
			moveSpeed     : 0
			glowColor     : 0x88ccff
		@root = {}
		@root.main = attributes.root
		@glowMeshes = []
		@isDisposed = false
		@_cloneRoot()
	
	update: (delta, now) ->
		return

	getMainObject: ->
		return @root.main
		
	rotateTo: (position, delta) ->
		dq = @_getQuaternionRotationFromPosition position
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
		return @getMainObject().position
	
	visible: (value) ->
		obj.traverse((child) -> child.visible = value) for scene, obj of @root unless _.isUndefined value
		return @getMainObject().visible
	
	kill: ->
		@dispose()
		return
	
	dispose: ->
		unless @isDisposed
			@isDisposed = true
			scenegraph = require 'scenegraph'
			scenegraph.removeDynObject @id
			if _.isArray @root objectUtils.dispose obj for obj in @root	else objectUtils.dispose @root
		return

	enableGlow: ->
		mesh.material = objectUtils.getGlowMaterials mesh, @glowColor, true for mesh in @glowMeshes
		return
		
	disableGlow: ->
		mesh.material = objectUtils.getGlowMaterials mesh, 'black' for mesh in @glowMeshes
		return

	_getQuaternionRotationFromPosition: (position) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @getMainObject().position, @getMainObject().up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		return dq

	_cloneRoot: ->
		for scene in ['glow']
			@root[scene] = objectUtils.clone null, @root.main, 
				materialCallback: (srcObject) =>
					return if scene is 'glow' then objectUtils.getGlowMaterials srcObject, @glowColor else srcObject.material.clone()
				geometryCallback: (srcObject) ->
					return objectUtils.copyGeometry srcObject.name, scene, srcObject.geometry
				onMeshCreated: (mesh) =>
					if scene is 'glow'
						mesh.scale.multiplyScalar @scaleGlow if @scaleGlow
						@glowMeshes.push mesh 						
					return
		return

return BaseModel 