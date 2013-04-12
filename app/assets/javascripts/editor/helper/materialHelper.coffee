Variables = require 'variables'
db = require 'database'

class MaterialHelper
	
	
	
	constructor: (@editor) ->
	

	getThreeMaterialId: (object,  materialId) ->
		unless object.material and object.material.materials
			materials = []
			if object.material
				materials.push object.material
			object.material = new THREE.MeshFaceMaterial(materials)
		for	value,key in object.material.materials
			if value and value.name and value.name is materialId
				foundId = key
				break
		unless foundId
			material = db.get 'materials', materialId
			threeMaterial = @transformMaterial(material, materialId) 
			object.material.materials.push threeMaterial
			foundId = object.material.materials.length - 1
		foundId
		

#Transform own materials (backbone model) to THREE.materials model
#TODO more mapping here
	transformMaterial:(material, materialId) ->
		result = new THREE.MeshBasicMaterial()
		result.name = materialId
		for key,value of material.attributes
			if key is "color"
				result.color = new THREE.Color(value)
		result
		
return MaterialHelper