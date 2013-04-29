Variables = require 'variables'
db = require 'database'

materialHelper =
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
		backBoneGeometry.userData.matIdMapper = []
		for child in 	obj.children
			if child.name != 'wireframe'
				if child.material.materials	
					for mat, geoMatIndex in child.material.materials
						materialId = mat.name.replace 'matID', ''
						materialId = parseInt materialId
						index = @getGlobalMatIndexById(materialId) #index of global materials list
						if index > -1
							backBoneGeometry.userData.matIdMapper.push materialId: index, materialIndex: geoMatIndex
		null
	
	loadGlobalMaterials:(mesh) ->
		materials = []
		globalMat = db.get 'materials'
		if mesh.geometry.userData and mesh.geometry.userData.matIdMapper
			mesh.geometry.userData.matIdMapper = _.sortBy mesh.geometry.userData.matIdMapper, (idMat) => return idMat.materialIndex
			for idMapper in mesh.geometry.userData.matIdMapper
				index = @getGlobalMatIndexById(idMapper.materialId)
				if index > -1
					mat = @transformMaterial globalMat.models[index], idMapper.materialId
					materials.push mat 
		mesh.material = new THREE.MeshFaceMaterial materials

	#For Geometries without global materials binding 
	loadGeometryMaterial: (geo) ->
		if geo.userData.matIdMapper
			materials = []
			geo.userData.matIdMapper = _.sortBy geo.userData.matIdMapper, (idMat) => return idMat.materialIndex
			for idMapper in geo.userData.matIdMapper
				index = @getMaterialIndex geo, idMapper
				if index > -1
					materials.push geo.material.materials[index]
				else
					console.error 'No material in index found ...'
					return null
			geo.material.materials = materials
		null
		
	getGlobalMatIndexById: (materialId) ->
		materials = db.get 'materials'
		for material, key in materials.models
			if material.attributes.materialId == materialId
				return key
		console.log "No Backbone material found!" 
		null
	
	
	createMesh: (geometry, materials, json, name) ->
		mesh = new THREE.Mesh geometry
		mesh.name = name
		mesh.userData.dbId = json.id
		mesh.material = new THREE.MeshFaceMaterial materials
		if json
			for matId in json.matIdMapper
				for threeMat in mesh.material.materials
					if matId.materialName is threeMat.name
						threeMat.name = "matID#{matId.materialId}" 
						break 
			
			mesh.userData.matIdMapper = json.matIdMapper
			@loadGeometryMaterial mesh
		mesh
	
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

return materialHelper