meshesFactory = require 'factory/meshes'
scenegraph = require 'scenegraph'
Tower = require 'models/tower'
db = require 'database'

towers =

	create: (opts) ->
		dynObj = @_createModel opts
		scenegraph.addDynObject dynObj, opts.id
		return dynObj

	_createModel: (opts) ->
		mesh = @_createMesh opts.id, opts.name
		model = new Tower opts.id, opts.name, mesh
		model.getMainObject().position.set opts.position.x, opts.position.y, opts.position.z if opts.position
		return model
		
	_createMesh: (id, name) ->
		return meshesFactory.create id, name

return towers