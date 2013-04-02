AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

class Home extends AdminAuthView

	id: 'home'
	
	template: templates.get 'home.tmpl'
	
	events:
		'click .user-link'		: 'user'
		'click .map-link'			: 'map'
		'click .object-link'	: 'object'
		'click .news-link'		: 'news'
		
	user: (event) ->
		event?.preventDefault()
		app.navigate 'admin/user/all', true

	map: (event) ->
		event?.preventDefault()
		app.navigate 'admin/map/all', true

	object: (event) ->
		event?.preventDefault()
		app.navigate 'admin/object/all', true

	news: (event) ->
		event?.preventDefault()
		app.navigate 'admin/news/all', true

return Home