objectUtils = require 'util/objectUtils'

class BaseModel

	rotationMultipler: null

	moveSpeed: 30
	
	glowColor: 0x88ccff
	
	constructor: (root) ->
		@root = {}
		@root.main = root
		@glowMeshes = []
		@isDisposed = false
		@_cloneRoot()
	
	update: (delta, now) ->
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

	_cloneRoot: ->
		for scene in ['glow']
			@root[scene] = objectUtils.clone null, @root.main, 
				materialCallback: (srcObject) =>
					return if scene is 'glow' then objectUtils.getGlowMaterials srcObject, @glowColor else srcObject.material.clone()
				geometryCallback: (srcObject) ->
					return objectUtils.copyGeometry srcObject.name, scene, srcObject.geometry
				onMeshCreated: (mesh) =>
					@glowMeshes.push mesh if scene is 'glow'						
					return
		return

return BaseModel 