BaseCollection = require 'models/baseCollection'
ObjectModel = require 'models/db/object'
app = require 'application'

class Objects extends BaseCollection

	url: ->
		"/object/#{@id}"

	model: ObjectModel

return Objects