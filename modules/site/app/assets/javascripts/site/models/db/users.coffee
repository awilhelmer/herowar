BaseCollection = require 'model/baseCollection'
UserModel = require 'models/db/user'
app = require 'application'

###
    Users provides a collection of users.
###
class Users extends BaseCollection

	url: ->
		"#{app.resourcePath()}user/#{@id}"

	model: UserModel

	parse: (resp) ->
		super resp
		return resp.users

return Users