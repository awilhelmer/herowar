app = require 'application'
db = require 'database'

###
    The Me model contains important informations about the current logged in user and will be fetched directly
    after initialize.

    @author Sebastian Sachtleben
###
class Me extends Backbone.Model  

	###
		Override initialize to fetch informations.

		@param {Object} The options parameter.
	###
	initialize: (options) ->
		@set 
			'isFetched' : false
			'isGuest' 	: true
			'isUser' 		: false
			'isAdmin'		: false
		@fetch()
		super options

	###
		Set url to resource path /me

		@return {String} The me resource url.
	###
	url: '/api/me'

	###
		Enable credentials to send user cookies.

		@param {Object} The fetch options.
	###
	fetch: (options) ->
		options = {} unless options?
		success = options.success
		options.success = (resp) =>
			success @, resp if success
			@validateResponse resp
			error = options.error
		options.error = (resp) =>
			error @, resp if error
			@validateResponse resp
		super options

	###
		Set isFetched to true and check if me resource give us response. If not we add hasBanner class to body.

		@param {Object} The response to parse.
	###
	validateResponse: (resp) ->
		@set 
			'isFetched' : true
			'isGuest' 	: !resp.id
			'isUser'		: !!resp.id

	###
		Parse response and add the current logged in user to the accounts list. It is needed for example to post new
		comments or do other stuff.

		@param {Object} The response to parse.
	###
	parse: (resp) ->
		db.add 'db/users', resp if resp
		return resp
	
	###
		Reseting me object. This is used to login the current user.
	###	
	reset: ->
		@clear()
		@set 
			'isFetched' : true
			'isGuest'		: true
			'isUser'		: false
			'isAdmin'		: false

return Me