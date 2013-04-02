BaseCollection = require 'models/baseCollection'
ObjectModel = require 'models/db/object'
app = require 'application'

class Objects extends BaseCollection

	url: ->
		"#{app.resourcePath()}object/#{@id}"

	model: ObjectModel

return Objects