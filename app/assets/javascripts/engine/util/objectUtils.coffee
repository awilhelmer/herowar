_projector = new THREE.Projector()

objectUtils = 

	dispose: (obj) ->
		if obj instanceof THREE.Mesh
			obj.geometry.dispose()
			@_disposeMaterial obj.material
		@dispose child for child in obj.children

	positionToScreen: (obj, viewportWidthHalf, viewportHeightHalf, camera) ->
		boundaryBox = obj.meshBody.geometry.boundingBox
		position = obj.getMainObject().position.clone()
		position.x -= Math.abs(boundaryBox.min.x) * obj.meshBody.scale.x
		position.y += (boundaryBox.max.y - boundaryBox.min.y) * obj.meshBody.scale.y
		_projector.projectVector position, camera
		return x: Math.round((position.x * viewportWidthHalf) + viewportWidthHalf), y: Math.round(- (position.y * viewportHeightHalf) + viewportHeightHalf)

	_disposeMaterial: (material) ->
		if _.isArray material.materials
			@_disposeMaterial child for child in material.materials
		else
			material.map.dispose() if material.map
			material.lightMap.dispose() if material.lightMap
			material.specularMap.dispose() if material.specularMap
			material.envMap.dispose() if material.envMap
			material.dispose()

return objectUtils
