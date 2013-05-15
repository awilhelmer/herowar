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
		aspectSize = size.width / size.height # aspect of terrain object
		aspectReal = Math.max aspect, aspectSize # get highest aspect
		if aspectReal > 1
			height = Math.round size.height / aspectReal
			camera.left = size.left
			camera.right = size.left + size.width
			camera.top = size.top + size.height + offset.top
			camera.bottom = size.top + (size.height - height)
		else
			width = Math.round size.width / aspectReal
			left = size.left + Math.round(width / 2)
			camera.left = left + offset.left
			camera.right = left + width
			camera.top = size.top + size.height
			camera.bottom = size.top
		console.log 'New Camera Position -> left=', camera.left, 'right=', camera.right, 'top=', camera.top, 'bottom=', camera.bottom 

return Viewport