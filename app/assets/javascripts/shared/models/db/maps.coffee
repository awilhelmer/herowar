BaseCollection = require 'models/baseCollection'
MapModel = require 'models/db/map'
app = require 'application'

class Maps extends BaseCollection

	url: ->
		"#{app.resourcePath()}map/#{@id}"

	model: MapModel

return Maps