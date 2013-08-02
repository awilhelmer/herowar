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
		index = @getMaterialIndex object, idMapper
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
	
	createAnimMesh: (geometry, materials, name, json) ->
		newMats = []
		if _.isArray(materials) and materials.length > 0
			for mat in materials
				newMat = mat.clone()
				newMat.morphTargets = true
				newMat.morphNormals = true
				newMat.transparent = true
				newMats.push newMat
		mesh = @updateMeshProperties new THREE.MorphAnimMesh(geometry), newMats, name, json
		mesh.parseAnimations()
		return mesh

	createSkinnedMesh: (geometry, materials, name, json) ->
		newMats = []
		if _.isArray(materials) and materials.length > 0
			for mat in materials
				newMat = mat.clone()
				newMat.skinning = true
				newMat.wrapAround = true
				newMat.needsUpdate = true
				newMats.push newMat
		return @updateMeshProperties new THREE.SkinnedMesh(geometry), newMats, name, json
	
	createMesh: (geometry, materials, name, json) ->
		newMats = []
		if _.isArray(materials) and materials.length > 0
			for mat in materials
				newMat = mat.clone()
				newMats.push newMat
		return @updateMeshProperties new THREE.Mesh(geometry), newMats, name, json
	
	updateMeshProperties: (mesh, materials, name, json) ->
		mesh.name = name
		mesh.userData.dbId = json.id
		mesh.material = new THREE.MeshFaceMaterial materials
		if json.matIdMapper
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
		if @isShader material.attributes
			result = new THREE.ShaderMaterial()
			result.side = THREE.DoubleSide
			#result.map = true
		else
			result = new THREE.MeshBasicMaterial()
		params = {}
		result.name = 'matID' + materialId
		for key,value of material.attributes when value
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
				when 'attributes'
					params.attributes = eval.call @, "(#{value})"
				when 'uniforms'
					params.uniforms = eval.call @, "(#{value})"
					for key, u of params.uniforms when u.value and u.type is "t"
						u.value = db.data().textures[u.value].clone()
						u.value.wrapS = u.value.wrapT = THREE.RepeatWrapping
						u.value.needsUpdate = true
				when 'vertexShader'
					params.vertexShader = value
				when 'fragmentShader'
					params.fragmentShader = value
				when 'id', 'materialId', 'name', 'nocolor'
					#ignore
				else
					console.log "Material Key #{key} is unknown" 
		result.setValues params
		result
	
	isShader:(attributes) ->
		for key,value of attributes
				if value and value isnt '' and (key is 'vertexShader' or key is 'fragmentShader')
					return true
		false

return materialHelper