db = require 'database'

worldToObjectConverter =
	
	convert: ->
		world = db.get 'world'
		obj = _.clone world.attributes
		@handleMaterials()
		@fillMaterialArray obj
		@convertGeometryVertices obj
		@convertGeometryFaces obj
		return obj

	handleMaterials: ->
		EditorEventbus = require 'editorEventbus'
		EditorEventbus.handleWorldMaterials.dispatch()

	fillMaterialArray: (obj) ->
		materials = []
		for index in obj.materials
			materials.push db.get 'materials', index 
		obj.materials = materials

	convertGeometryVertices: (obj) ->
		vertices = []
		for vector in obj.terrain.geometry.vertices
			vertices.push vector.x
			vertices.push vector.y
			vertices.push vector.z
		obj.terrain.geometry.vertices = vertices

	convertGeometryFaces: (obj) ->
		# TODO: convert faces (reverse JSON loader) ...

return worldToObjectConverter