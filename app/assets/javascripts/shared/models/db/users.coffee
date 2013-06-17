BaseCollection = require 'models/baseCollection'
UserModel = require 'models/db/user'
app = require 'application'

class Users extends BaseCollection

	url: ->
		"/api/user/#{@id}"

	model: UserModel

return Users