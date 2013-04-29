BaseView = require 'views/baseView'
templates = require 'templates'

class IconbarView extends BaseView

	id: 'iconbar'
	
	template: templates.get 'iconbar.tmpl'

	events:
		'click [data-modal="true"]' : 'showModal'

	showModal: (event) ->
		unless event then return
		event?.preventDefault()
		$currentTarget = $ event.currentTarget
		id = $currentTarget.attr 'id'
		$("#modal#{id.charAt(0).toUpperCase()}#{id.slice(1)}").modal 'show'

return IconbarView