geometryUtils = require 'util/geometryUtils'
viewUtils = require 'util/viewUtils'
db = require 'database'

_projector = new THREE.Projector()

objectUtils = 

	clone: (destObject, srcObject, opts) ->
		opts = _.extend {}, opts
		if srcObject instanceof THREE.Scene
			destObject = new THREE.Scene
			@_copyObjectData destObject, srcObject
		else if srcObject instanceof THREE.Mesh
			if _.isFunction opts.materialCallback
				material = opts.materialCallback srcObject
			else 
				material = srcObject.material.clone()
			if _.isFunction opts.geometryCallback
				geometry = opts.geometryCallback srcObject
			else
				geometry = srcObject.geometry.clone()
			if srcObject instanceof THREE.MorphAnimMesh 
				destObject	= new THREE.MorphAnimMesh geometry, material
				destObject.parseAnimations()
				@_copyObjectData destObject, srcObject
			else
				destObject	= new THREE.Mesh geometry, material
				@_copyObjectData destObject, srcObject
			opts.onMeshCreated destObject if _.isFunction opts.onMeshCreated
		else if srcObject instanceof THREE.Object3D
			destObject	= new THREE.Object3D()
			@_copyObjectData destObject, srcObject
			opts.onObjectCreated destObject if _.isFunction opts.onObjectCreated
		destObject.add @clone null, srcChild, opts for srcChild in srcObject.children if srcObject.children.length isnt 0
		return destObject
	
	copyGeometry: (name, sceneType, geometry) ->
			geo = db.geometry name, sceneType
			geo = geometryUtils.clone geometry unless geo
			return geo
	
	getGlowMaterials: (obj, color, force) ->
		if obj instanceof THREE.Mesh
			isAnimated = obj instanceof THREE.MorphAnimMesh
			glowMaterial = if obj.userData.glowing or force then @_getGlowOnMaterial isAnimated, color else @_getGlowOffMaterial isAnimated
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
	
	dispose: (obj) ->
		if obj instanceof THREE.Mesh
			obj.geometry.dispose()
			@_disposeMaterial obj.material
		else if obj instanceof THREE.Sprite
			@_disposeMaterial obj.material
		@dispose child for child in obj.children

	positionToScreen: (obj, viewportWidthHalf, viewportHeightHalf, camera) ->
		boundaryBox = obj.meshBody.geometry.boundingBox
		position = obj.getMainObject().position.clone()
		position.x -= Math.abs(boundaryBox.min.x) * obj.meshBody.scale.x
		position.y += (boundaryBox.max.y - boundaryBox.min.y) * obj.meshBody.scale.y
		return viewUtils.positionToScreen position, viewportWidthHalf, viewportHeightHalf, camera

	positionCenterToScreen: (obj, viewportWidthHalf, viewportHeightHalf, camera) ->
		position = obj.getMainObject().position.clone()
		return viewUtils.positionToScreen position, viewportWidthHalf, viewportHeightHalf, camera

	_disposeMaterial: (material) ->
		if _.isArray material.materials
			@_disposeMaterial child for child in material.materials
		else
			material.map.dispose() if material.map
			material.lightMap.dispose() if material.lightMap
			material.specularMap.dispose() if material.specularMap
			material.envMap.dispose() if material.envMap
			material.dispose()

	_getGlowOnMaterial: (isAnimated, glowColor) ->
		material = new THREE.MeshBasicMaterial color: glowColor
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

	_copyObjectData: (destObject, srcObject) ->
		destObject.position.copy srcObject.position
		destObject.rotation.copy srcObject.rotation
		destObject.scale.copy srcObject.scale
		destObject.userData = _.clone srcObject.userData
		if srcObject.useQuaternion
			destObject.quaternion.copy srcObject.quaternion
			destObject.useQuaternion = true		

return objectUtils
