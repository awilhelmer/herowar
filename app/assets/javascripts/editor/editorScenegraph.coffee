class EditorScenegraph
	
	constructor: (app) ->
		@app = app

	init: ->
		console.log 'Initialize editor scenegraph'
		body = $ 'body'
		body.on 'click', '.scenegraph-tree div', @selectElement

	selectElement: (event) =>
		if event
			event.preventDefault()
			$('.scenegraph-tree div').removeClass 'active'
			$CurrentTarget = $ event.currentTarget
			$CurrentTarget.addClass 'active'
	
return EditorScenegraph