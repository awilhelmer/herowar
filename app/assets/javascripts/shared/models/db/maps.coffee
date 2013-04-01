BaseCollection = require 'models/baseCollection'
MapModel = require 'models/db/map'
app = require 'application'

###
    Maps provides a collection of maps.
###
class Maps extends BaseCollection

	url: ->
		"#{app.resourcePath()}map/#{@id}"

	model: MapModel

return Maps