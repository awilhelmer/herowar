BlendShader = require 'shaders/blendShader'
BaseComposer = require 'composers/baseComposer'
MainComposer = require 'composers/main'
GlowComposer = require 'composers/glow'
scenegraph = require 'scenegraph'

class FinalComposer extends BaseComposer
	
	constructor: (@view, @glowcomposer, @renderTarget) ->
		@glowcomposer = new GlowComposer @view
		super @view, @renderTarget
	
	initialize: (composer, width, height) ->
		model = new THREE.RenderPass scenegraph.scene(), @view.get 'cameraScene'
		composer.addPass model
		blendPass = new THREE.ShaderPass THREE.BlendShader, 'tDiffuse1' #blendShader
		#blendPass = new THREE.ShaderPass new BlendShader()
		blendPass.uniforms['tDiffuse2'].value = @glowcomposer.renderTarget
		blendPass.uniforms['mixRatio'].value = 0.5
		blendPass.uniforms['opacity'].value = 2
		composer.addPass blendPass
		return

	render: (delta) ->
		@glowcomposer.render delta
		@composer.render delta
		return

return FinalComposer