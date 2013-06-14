scenegraph = require 'scenegraph'
Tower = require 'models/scene/tower'
db = require 'database'

towers =

	create: (opts) ->
		#console.log 'Create tower', opts
		dynObj = @_createModel opts
		scenegraph.addDynObject dynObj, opts.id
		return dynObj

	_createModel: (opts) ->
		model = new Tower opts
		model.position opts.position
		return model
		
return towers