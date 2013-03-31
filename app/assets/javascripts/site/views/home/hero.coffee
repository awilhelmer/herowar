BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Hero shows our hero part in home view.

    @author Sebastian Sachtleben
###
class Hero extends BaseView

	entity: 'ui/me'
	
	template: templates.get 'home/hero.tmpl'
		
	events:
		'click .signup-link': 'signup'
		'click .play-link': 'play'
		
	bindEvents: ->
		@listenTo @model, 'change:isGuest change:isUser', @render if @model
		
	signup: (event) ->
		event?.preventDefault()
		app.navigate 'signup', true 

	play: (event) ->
		event?.preventDefault()
		app.navigate 'play', true 
	
return Hero