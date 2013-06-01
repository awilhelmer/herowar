class EngineRenderer extends THREE.WebGLRenderer
		
	addEngineObject: (object, scene) ->
		addObject(object, scene)
		return
		
	removeEngineObject: (object, scene) ->
		removeObject(object, scene)
		return
	
return EngineRenderer
	


