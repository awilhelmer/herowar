class Textures extends Backbone.Collection

	initialize: (models, options) ->
		@model = require 'models/texture'
		super models, options

return Textures