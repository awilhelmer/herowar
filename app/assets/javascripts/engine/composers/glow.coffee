HorizontalBlurShader = require 'shaders/horizontalBlurShader'
VerticalBlurShader = require 'shaders/verticalBlurShader'
BaseComposer = require 'composers/baseComposer'
scenegraph = require 'scenegraph'

class GlowComposer extends BaseComposer
	
	initialize: (composer, width, height) ->
		model = new THREE.RenderPass scenegraph.scene('glow'), @view.get 'cameraScene'
		composer.addPass model
		blurHLevel = 0.003
		blurVLevel = 0.006
		effectHBlur = new THREE.ShaderPass new HorizontalBlurShader()
		effectHBlur.uniforms['h'].value = blurHLevel
		composer.addPass effectHBlur
		effectVBlur = new THREE.ShaderPass new VerticalBlurShader()
		effectVBlur.uniforms['v'].value = blurVLevel
		composer.addPass effectVBlur
		effectHBlur2 = new THREE.ShaderPass new HorizontalBlurShader()
		effectHBlur2.uniforms['h'].value = blurHLevel
		composer.addPass effectHBlur2
		effectVBlur2 = new THREE.ShaderPass new VerticalBlurShader()
		effectVBlur2.uniforms['v'].value = blurVLevel
		composer.addPass effectVBlur2

	setSize: (width, height) ->
		@composer.setSize Math.floor(width / 4), Math.floor(height / 4)

	_createRenderTarget: (width, height) ->
		return new THREE.WebGLRenderTarget Math.floor(width / 4), Math.floor(height / 4), @_createRenderParams()

return GlowComposer