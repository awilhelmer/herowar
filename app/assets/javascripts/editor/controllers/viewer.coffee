ApplicationController = require 'controllers/application'
Variables = require 'variables'
scenegraph = require 'scenegraph'
engine = require 'engine'
log = require 'util/logger'

class ViewerController extends ApplicationController

	views:
		'views/viewer'   : ''
		'views/viewerUI' : ''

	viewports: [
		domId: '#viewer',
		type: Variables.VIEWPORT_TYPE_VIEWER
		camera:
			pov: 40
			type: Variables.CAMERA_TYPE_PERSPECTIVE
			position: [ 0, 50, 100 ]
			rotation: [ 0, 0, 0 ]	
	]

	initialize: (options) ->
		log.info 'Initialize viewer...'
		super options
		engine.start()

	initCore: ->
		scenegraph.scene().fog = new THREE.Fog 0x050505, 400, 1000
		@addGround()
		for view in engine.views.viewports.models
			view.get('renderer').setClearColor scenegraph.scene().fog.color, 1
			view.get('cameraScene').lookAt scenegraph.scene().position
		
	addGround: ->		
		grid = new THREE.GridHelper 500, 50
		scenegraph.scene().add grid
		###
		groundGeometry = new THREE.PlaneGeometry 1000, 1000
		groundMaterial = new THREE.MeshPhongMaterial color: 0xFFFFFF
		groundMesh = new THREE.Mesh groundGeometry, groundMaterial
		groundMesh.rotation.x = THREE.Math.degToRad -90
		groundMesh.receiveShadow = true
		scenegraph.scene().add groundMesh
		###
		
return ViewerController