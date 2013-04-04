class Scenegraph
	
	constructor: (@app) ->

	init: ->
		console.log 'Initialize editor scenegraph'
		@bindEvents()
		@selectElement $('.scenegraph-tree-world')

	bindEvents: ->
		body = $ 'body'
		body.on 'click', '.scenegraph-tree div', @selectElementClick		

	selectElementClick: (event) =>
		if event
			event.preventDefault()
			@selectElement $(event.currentTarget)

	selectElement: ($Target) =>
		$('.scenegraph-tree div').removeClass 'active'
		$Target.addClass 'active'
		type = $Target.data 'type'
		if type
			$('#sidebar-properties-world, #sidebar-properties-terrain, #sidebar-properties-object').addClass 'hidden'
			propWindow = $ '#sidebar-properties-' + type
			propWindow.removeClass 'hidden' if propWindow
	
return Scenegraph