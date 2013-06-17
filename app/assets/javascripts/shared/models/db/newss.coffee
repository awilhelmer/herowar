BaseCollection = require 'models/baseCollection'
NewsModel = require 'models/db/news'
app = require 'application'

class Newss extends BaseCollection

	url: ->
		"/news/#{@id}"

	model: NewsModel

return Newss