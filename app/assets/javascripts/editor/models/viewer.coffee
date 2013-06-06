materialHelper = require 'helper/materialHelper'
AnimatedModel = require 'models/animatedModel'
enemiesFactory = require 'factory/enemies'
JSONLoader = require 'util/threeloader'
scenegraph = require 'scenegraph'
db = require 'database'

class Viewer extends Backbone.Model

	id: 1

	jsonLoader: new JSONLoader()
	
	sceneObject: null

	load: (id, type, name) ->
		return if @has 'loading'
		scenegraph.removeDynObject @id if @id
		if db.data().geometries[name]
			@placeModel id, name, type
			@trigger 'fetched:data', @sceneObject
			return
		url = null
		if type is 1
			url = "api/game/geometry/tower/#{id}"
		else if type is 2
			url = "api/game/geometry/unit/#{id}"
		return unless url
		@set 'loading', true
		@trigger 'fetching:data'
		@jsonLoader.load url, 
			(geometry, materials, json) =>
				geometry.name = name
				geometry.computeBoundingBox()
				geometry.computeBoundingSphere()
				geometry.computeMorphNormals()
				db.data().geometries[name] = [geometry, materials, json]
				@placeModel id, name, type
				@unset 'loading'
				@trigger 'fetched:data', @sceneObject
			, 'assets/images/game/textures'
		return

	placeModel: (id, name, type) ->
		if type is 2
			units = db.get 'db/units'
			@sceneObject = enemiesFactory.create units.get(id).attributes
		else
			data = db.data().geometries[name]
			mesh = @createMesh data[0], data[1], name, data[2]
			@sceneObject = new AnimatedModel id, name, mesh
			scenegraph.addDynObject @sceneObject, id
		@id = @sceneObject.id
		#@resetGUI()
		console.log 'Show model', @sceneObject, 'Set previous id', @id
		return 

	createMesh: (geometry, materials, name, json) ->
		mesh = materialHelper.createMesh geometry, materials, name, json if json.morphTargets.length is 0
		mesh = materialHelper.createAnimMesh geometry, materials, name, json if json.morphTargets.length isnt 0
		if _.isObject json
			mesh.scale.x = json.scale
			mesh.scale.y = json.scale
			mesh.scale.z = json.scale
		mesh.geometry.computeBoundingBox()
		mesh.position.y = - json.scale * mesh.geometry.boundingBox.min.y
		return mesh

return Viewer