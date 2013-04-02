BaseCollection = require 'models/baseCollection'
NewsModel = require 'models/db/news'
app = require 'application'

class Newss extends BaseCollection

	url: ->
		"#{app.resourcePath()}news/#{@id}"

	model: NewsModel

return Newss