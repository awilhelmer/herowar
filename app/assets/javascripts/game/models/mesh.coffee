BaseModel = require 'models/basemodel'

class MeshModel extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	constructor: (@id, @name, @meshBody) ->
		@_enableShadows()
		super @_createThreeObject()
	
	_enableShadows: ->
		if _.isArray @meshBody
			for mesh in @meshBody
				mesh.castShadow = true
				mesh.receiveShadow = true
		else
			@meshBody.castShadow = true
			@meshBody.receiveShadow = true	
		return

	_createThreeObject: ->
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		if _.isArray @meshBody
			for mesh in @meshBody
				obj.add mesh
		else
			obj.add @meshBody
		return obj

return MeshModel