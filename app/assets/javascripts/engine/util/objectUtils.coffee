_projector = new THREE.Projector()

objectUtils = 

	positionToScreen: (obj, viewportWidthHalf, viewportHeightHalf, camera) ->
		boundaryBox = obj.meshBody.geometry.boundingBox
		position = obj.getMainObject().position.clone()
		position.x -= Math.abs(boundaryBox.min.x) * obj.meshBody.scale.x
		position.y += (boundaryBox.max.y - boundaryBox.min.y) * obj.meshBody.scale.y
		_projector.projectVector position, camera
		return x: Math.round((position.x * viewportWidthHalf) + viewportWidthHalf), y: Math.round(- (position.y * viewportHeightHalf) + viewportHeightHalf)
		
return objectUtils
