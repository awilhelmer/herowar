_projector = new THREE.Projector()

viewUtils = 

	positionToScreen: (position, viewportWidthHalf, viewportHeightHalf, camera) ->
		#if camera instanceof THREE.OrthographicCamera
		#	visibleWidth = camera.right - camera.left
		#	visibleHeight = camera.top - camera.bottom
		#	percX = (position.x - camera.left) / visibleWidth
		#	percY = (position.z - camera.bottom) / visibleHeight
		#	return x: Math.round(percX * (viewportWidthHalf * 2)), y: Math.round(percY * (viewportHeightHalf * 2))
		#else
		_projector.projectVector position, camera
		return x: Math.round((position.x * viewportWidthHalf) + viewportWidthHalf), y: Math.round(- (position.y * viewportHeightHalf) + viewportHeightHalf)

return viewUtils
