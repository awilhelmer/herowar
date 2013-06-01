BaseComposer = require 'composers/baseComposer'
scenegraph = require 'scenegraph'

class MainComposer extends BaseComposer
	
	initialize: (composer, width, height) ->
		model = new THREE.RenderPass scenegraph.scene(), @view.get 'cameraScene'
		effectFXAA = new THREE.ShaderPass THREE.FXAAShader
		effectFXAA.uniforms['resolution'].value.set 1 / width, 1 / height
		effectFXAA.renderToScreen = true
		composer.addPass model
		composer.addPass effectFXAA

return MainComposer