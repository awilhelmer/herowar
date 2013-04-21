BaseView = require 'views/baseView'
templates = require 'templates'

class EnvironmentCategories extends BaseView
	
	template: templates.get 'sidebar/environmentCategories.tmpl'

	events:
		'click .tree .item'	: 'selectElement'

	selectElement: (event) =>
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		console.log "Selected Item #{$currentTarget.data('value')}"

return EnvironmentCategories