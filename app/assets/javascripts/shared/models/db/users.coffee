BaseCollection = require 'models/baseCollection'
UserModel = require 'models/db/user'
app = require 'application'

###
    Users provides a collection of users.
###
class Users extends BaseCollection

	url: ->
		"#{app.resourcePath()}user/#{@id}"

	model: UserModel

return Users