EditorEventbus = require 'editorEventbus'

class ObjectHelper
	
	constructor: ->
		EditorEventbus.resetWireframe.add @refreshWireframe
		@wireFrameMaterials = new THREE.MeshFaceMaterial([new THREE.MeshBasicMaterial(color: 0xFFFF00, wireframe: true)])

	hasWireframe: (obj) ->
		found = false
		for mesh in obj.children
			found = true if mesh.name is 'wireframe'
		found

	addWireframe: (obj, color) ->
		#TODO hard coded children access ...
		if obj
			@wireFrameMaterials.materials[0].color.set color
			@wireFrameMaterials.materials[0].needsUpdate = true
			mesh = new THREE.Mesh obj.children[0].geometry.clone(), @wireFrameMaterials
			for face in mesh.geometry.faces
				face.materialIndex = 0
			mesh.rotation.copy obj.children[0].rotation.clone()
			mesh.name = 'wireframe'
			obj.add mesh

	removeWireframe: (obj) ->
		mesh = @getWireframe(obj)
		if mesh
			mesh.geometry.dispose()
			obj.remove mesh
	
	changeWireframeColor: (obj, color) ->
		if obj
			for mesh in obj.children
				if mesh.name is 'wireframe'	and mesh.material.materials
					mesh.material.materials[0].color.set color if mesh.material.materials[0].wireframe
					mesh.material.materials[0].needsUpdate = true

	getBaseObject: (obj) ->	
		unless obj then return
		obj = obj.parent while not _.isUndefined(obj.parent) and not (obj.parent instanceof THREE.Scene)
		obj
	
	getWireframe: (obj) ->
		if obj
			if obj.name is 'wireframe'
				mesh = obj
			else
				for child in obj.children
					if child.name is 'wireframe'
						mesh = child
						break
		mesh
	
	getModel: (obj) ->
		if obj
			for child in obj.children
					if child.name isnt 'wireframe'
						mesh = child
						break
			unless mesh
				mesh = obj
		mesh
	
	isTerrain: (obj) ->
		scenegraph = require 'scenegraph'
		if _.isUndefined obj or _.isUndefined scenegraph.getMap().getMainObject() then return false
		obj = @getBaseObject obj unless obj.parent instanceof THREE.Scene
		scenegraph.getMap().getMainObject().id is obj.id

#For updating all geometry data ...
	refreshWireframe: (obj) =>
		mesh = @getWireframe(obj)
		if mesh
			for child in obj.children
				if child isnt mesh
					mesh.geometry.vertices = child.geometry.vertices
					mesh.geometry.verticesNeedUpdate = true
					
return ObjectHelper