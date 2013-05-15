class Viewport extends Backbone.Model

	updateSize: ->
		$domElement = $ @get 'domElement'
		renderer = @get 'renderer'
		camera = @get 'camera'
		cameraScene = @get 'cameraScene'
		cameraSkybox = @get 'cameraSkybox'
		width = $domElement.width()
		height = $domElement.height()
		renderer.setSize width, height
		aspect =  width / height
		if cameraScene instanceof THREE.OrthographicCamera and camera.size
			@calculateOrthographicCameraPosition cameraScene, camera.size, camera.offset, aspect
		cameraScene.aspect = aspect
		cameraScene.updateProjectionMatrix()
		cameraSkybox.aspect = cameraScene.aspect
		cameraSkybox.updateProjectionMatrix()
		return
		
	calculateOrthographicCameraPosition: (camera, size, offset, aspect) ->
		aspectSize = size.width / size.height
		aspectReal = Math.max aspect, aspectSize
		if aspectReal > 1
			height = Math.round size.height / aspectReal
			scrollable = size.height - height
			offset.left = 0 if offset.left isnt 0
			offset.top = scrollable if offset.top > scrollable
			camera.left = size.left
			camera.right = size.left + size.width
			camera.top = size.top + size.height - offset.top
			camera.bottom = size.top + (size.height - height) - offset.top
		else
			# TODO: here is something wrong, just make your browser higher than smaller ...
			width = Math.round size.width / aspectReal
			left = size.left + Math.round(width / 2)
			scrollable = size.width - width
			offset.left = scrollable if offset.left > scrollable
			offset.top = 0 if offset.top isnt 0
			camera.left = left + offset.left
			camera.right = left + width + offset.left
			camera.top = size.top + size.height
			camera.bottom = size.top
		console.log 'New Camera Position -> left=', camera.left, 'right=', camera.right, 'top=', camera.top, 'bottom=', camera.bottom, 'offset', offset

return Viewport