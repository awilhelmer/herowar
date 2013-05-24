BaseModel = require 'models/basemodel'

class MeshModel extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	constructor: (@id, @name, @meshBody) ->
		# Enable shadows
		@meshBody.castShadow = true
		@meshBody.receiveShadow = true
		# Create Object3D
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		obj.add @meshBody
		super obj
	
return MeshModel