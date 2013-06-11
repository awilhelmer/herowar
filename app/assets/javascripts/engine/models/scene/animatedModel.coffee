MeshModel = require 'models/scene/mesh'
Explosion = require 'effects/explosion'

class AnimatedModel extends MeshModel

	activeAnimation: null
	
	playOnce: false
	
	animationFPS: 6	

	constructor: (attributes) ->
		super attributes
		@effects = []
		meshes = @_findAnimatedMeshes()
		if meshes.length isnt 0
			for mesh in meshes
				@setAnimation mesh.geometry.firstAnimation if mesh.geometry.firstAnimation

	update: (delta, now) ->
		super delta
		@animate delta
		@effects.forEach (effect) =>
			if effect.active
				effect.update delta, now
			else
				@effects.splice @effects.indexOf(effect), 1 unless effect.active
		return

	animate: (delta) ->
		if @activeAnimation
			meshes = @_findAnimatedMeshes()
			if meshes.length isnt 0
				for mesh in meshes
					if @playOnce and mesh.currentKeyframe is mesh.endKeyframe
						@activeAnimation = null
					else 
						mesh.updateAnimation 1000 * delta
		return
	
	setAnimation: (name, playOnce) ->
		return if @activeAnimation is name
		meshes = @_findAnimatedMeshes()
		if meshes.length isnt 0
			if _.isBoolean playOnce then @playOnce = playOnce else @playOnce = false
			for mesh in meshes
				mesh.playAnimation name, @animationFPS 
				mesh.baseDuration = mesh.duration
			@activeAnimation = name
		return

	showExplosion: (removeTime) ->
		removeTime = 1 unless removeTime
		@effects.push new Explosion @ 
		setTimeout =>
			scenegraph = require 'scenegraph'
			scenegraph.removeFromScenes @
		, removeTime
		return

	dispose: ->
		effect.dispose() for effect in @effects
		@effects.length = 0
		super()

	_findAnimatedMeshes: ->
		meshes = []
		for scene, obj of @root
			foundMeshes = @_findAnimatedMeshesRecursive obj
			meshes.push mesh for mesh in foundMeshes
		return meshes

	_findAnimatedMeshesRecursive: (obj) ->
		meshes = []
		meshes.push obj if obj instanceof THREE.MorphAnimMesh
		if obj.children.length isnt 0
			for child in obj.children 
				foundMeshes = @_findAnimatedMeshesRecursive child
				meshes.push mesh for mesh in foundMeshes
		return meshes

return AnimatedModel