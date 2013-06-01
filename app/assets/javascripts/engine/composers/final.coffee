#AdditiveBlendShader = require 'shaders/additiveBlendShader'
BaseComposer = require 'composers/baseComposer'
scenegraph = require 'scenegraph'

class FinalComposer extends BaseComposer
	
	initialize: (composer, width, height) ->
		model = new THREE.RenderPass scenegraph.scene(), @view.get 'cameraScene'
		#blendShader = new AdditiveBlendShader()
		blendPass = new THREE.ShaderPass THREE.BlendShader, 'tDiffuse1' #blendShader
		#blendPass.uniforms['tDiffuse1'].value = maincomposer.renderTarget1
		blendPass.uniforms['tDiffuse2'].value = glowcomposer.renderTarget1
		blendPass.uniforms['mixRatio'].value = 0.5
		blendPass.uniforms['opacity'].value = 2
		blendPass.renderToScreen = true
		composer.addPass model
		composer.addPass blendPass

return FinalComposer