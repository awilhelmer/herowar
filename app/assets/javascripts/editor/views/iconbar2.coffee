BaseView = require 'views/baseView'
templates = require 'templates'

class IconbarView2 extends BaseView

	id: 'iconbar2'
	
	template: templates.get 'iconbar2.tmpl'

	events:
		'click [data-modal="true"]' : 'showModal'

	showModal: (event) ->
		unless event then return
		event?.preventDefault()
		$currentTarget = $ event.currentTarget
		id = $currentTarget.attr 'id'
		$("#modal#{id.charAt(0).toUpperCase()}#{id.slice(1)}").modal 'show'

return IconbarView2