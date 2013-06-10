scenegraph = require 'scenegraph'
Tower = require 'models/scene/tower'
db = require 'database'

towers =

	create: (opts) ->
		dynObj = @_createModel opts
		scenegraph.addDynObject dynObj, opts.id
		return dynObj

	_createModel: (opts) ->
		model = new Tower opts
		model.getMainObject().position.set opts.position.x, opts.position.y, opts.position.z if opts.position
		return model
		
return towers