Variables = require 'variables'
db = require 'database'

class MaterialHelper

	constructor: (@editor) ->


	getThreeMaterialId: (object, materialId) ->
		unless object.material and object.material.materials
			materials = []
			if object.material
				materials.push object.material
			object.material = new THREE.MeshFaceMaterial(materials) 	
		for value,key in object.material.materials
			if value and value.name and value.name is 'matID' + materialId
				foundId = key
				break
		unless foundId
			material = db.get 'materials', materialId #TODO id<-> materialid
			threeMaterial = @transformMaterial(material, materialId) 
			object.material.materials.push threeMaterial
			foundId = object.material.materials.length - 1
		foundId

#Transform own materials (backbone model) to THREE.materials model 
# @see MaterialManagerMenu for all properties 
	transformMaterial:(material, materialId) ->
		result = new THREE.MeshBasicMaterial()
		result.name = 'matID' + materialId
		for key,value of material.attributes
			switch key
				when 'color'
					result.color = new THREE.Color(value)
				when 'transparent'
					result.transparent = value
				when 'map'
					result.map = value
				when 'opacity'
					result.opacity = value
				when 'vertexColors'
					result.vertexColors = value
				when 'side'
					result.side = value
				when 'shading'
					result.shading = value
				when 'wireframe'
					result.wireframe = value
				when 'wireframeLinewidth'
					result.wireframeLinewidth = value
				when 'id', 'materialId', 'name'
					#ignore
				else
					console.log "Material Key #{key} is unknown" 
		result

return MaterialHelper