EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
log = require 'util/logger'

AddMesh = require 'tools/addMesh'

class AddEnvironment extends AddMesh
	
	bindEvents: ->
		EditorEventbus.listSelectItem.add @onSelectItem
		EditorEventbus.changeStaticObject.add @changeStaticObject
	
	onSelectItem: (id, value, name) =>
		if id is 'sidebar-environment-geometries' and @tool.get('currentMeshId') isnt value
			log.debug 'Set Tool Build'
			@tool.set
				'active'					: Constants.TOOL_BUILD
				'currentMeshId' 	: value
				'currentMeshName'	: name
			unless @editor.engine.scenegraph.hasStaticObject @tool.get 'currentMeshId'
				log.debug "Loading Geometry from Server ... "
				now = new Date()
				@loader.load "/api/game/geometry/env/#{@tool.get('currentMeshId')}", @onLoadGeometry, 'assets/images/game/textures'
				log.debug "Loading Geometry from Server completed, time  #{new Date().getTime() - now.getTime()} ms"
			else
				mesh = @editor.engine.scenegraph.staticObjects[@tool.get('currentMeshId')][0]
				@onLoadGeometry mesh.geometry, mesh.material.materials

	changeStaticObject: (backboneModel) =>
		mesh = @editor.engine.scenegraph.getStaticObject backboneModel.get('dbId'), backboneModel.get('listIndex')
		attributes = _.pick _.clone(backboneModel.attributes), 'position', 'scale', 'rotation'
		_.extend mesh, attributes
		@editor.engine.render()

return AddEnvironment