BaseView = require 'views/baseView'
templates = require 'templates'

class SceneView extends BaseView

	id: 'scene'
	
	entity: 'scene'
	
	template: templates.get 'scene.tmpl'
	
	getTemplateData: ->
		json = {}
		json.scenes = []
		for name, scene of @model.get 'scenes'
			json.scenes.push
				name: name
				quantity: scene.children.length
		return json
	
return SceneView