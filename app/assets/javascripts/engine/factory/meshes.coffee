materialHelper = require 'helper/materialHelper'
db = require 'database'

meshes =

	create: (id, name) ->
		data = db.data().geometries[name]
		if data[0].morphTargets.length isnt 0
			mesh = materialHelper.createAnimMesh data[0], data[1], name, id: id
		else if data[0].bones
			mesh = materialHelper.createSkinnedMesh data[0], data[1], name, id: id
		else
			mesh = materialHelper.createMesh data[0], data[1], name, id: id
		if _.isObject data[2]
			mesh.scale.x = data[2].scale
			mesh.scale.y = data[2].scale
			mesh.scale.z = data[2].scale
		mesh.position.y -= mesh.scale.y * mesh.geometry.boundingBox.min.y
		mesh

return meshes
