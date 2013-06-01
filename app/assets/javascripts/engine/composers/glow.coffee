HorizontalBlurShader = require 'shaders/horizontalBlurShader'
VerticalBlurShader = require 'shaders/verticalBlurShader'
BaseComposer = require 'composers/baseComposer'
scenegraph = require 'scenegraph'

class GlowComposer extends BaseComposer
	
	initialize: (composer, width, height) ->
		model = new THREE.RenderPass scenegraph.scene('glow'), @view.get 'cameraScene'
		effectHBlur = new THREE.ShaderPass new HorizontalBlurShader()
		effectVBlur = new THREE.ShaderPass new VerticalBlurShader()
		bluriness = 3
		effectHBlur.uniforms['h'].value = bluriness / width
		effectVBlur.uniforms['v'].value = bluriness / height
		effectVBlur.renderToScreen = true
		composer.addPass model
		composer.addPass effectHBlur
		composer.addPass effectVBlur

return GlowComposer