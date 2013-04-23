Variables = require 'variables'
db = require 'database'

class MaterialHelper

	getThreeMaterialId: (object, idMapper) ->
		unless object.material and object.material.materials
			materials = []
			if object.material
				materials.push object.material
			object.material = new THREE.MeshFaceMaterial materials 	
		index = @getMaterialIndex object, idMapper 
		if index == -1
			material = db.get 'materials', idMapper.id 
			threeMaterial = @transformMaterial material, idMapper.materialId 
			object.material.materials.push threeMaterial
			index = object.material.materials.length - 1
		index


	updateMaterial: (object, idMapper) ->
		index = @getMaterialIndex object, idMapper.materialId
		if index > -1
			material = db.get 'materials', idMapper.id 
			threeMaterial = @transformMaterial material, idMapper.materialId
			object.material.materials[index] = threeMaterial
			if threeMaterial.map
						threeMaterial.map.needsUpdate = true
		index
					
	getMaterialIndex:(obj, idMapper) ->
		foundId = -1
		for value,key in obj.material.materials
			if value and value.name and value.name is 'matID' + idMapper.materialId
				foundId = key
				break
		foundId
	
	handleGeometryForSave:(backBoneGeometry, obj) ->
		backBoneGeometry.matIdMapper = []
		for child in 	obj.children
			if child.name != 'wireframe'
				if child.material.materials	
					for mat, geoMatIndex in child.material.materials
						index = @getGlobalMatIndexById(mat.name) #index of global materials list
						if index
							backBoneGeometry.materialsIndex.push materialId: index, materialIndex: geoMatIndex
		null
	
	getGlobalMatIndexById: (name) ->
		id = name.replace 'matID', ''
		id = parseInt id
		materials = db.get 'materials'
		for material, key in materials
			if material.id == id
				return key
		console.log "No Backbone material found!" 
		null
	
	#Transform own materials (backbone model) to THREE.materials model 
	# @see MaterialManagerMenu for all properties 
	transformMaterial:(material, materialId) ->
		result = new THREE.MeshBasicMaterial()
		result.name = 'matID' + materialId
		for key,value of material.attributes
			switch key
				when 'color'
					nocolor = $(material.attributes).attr('nocolor')
					unless nocolor and nocolor is true
						result.color = new THREE.Color value
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
				when 'id', 'materialId', 'name', 'nocolor'
					#ignore
				else
					console.log "Material Key #{key} is unknown" 
		result

return MaterialHelper