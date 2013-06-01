BaseShader = require 'shaders/baseShader'

class AdditiveBlendShader extends BaseShader

	uniforms:
		tDiffuse1: 
			type: 't' 
			value: null
		tDiffuse2: 
			type: 't' 
			value: null
		mixRatio:
			type: 'f'
			value: 0.5
		opacity:
			type: 'f'
			value: 2.0
		
	fragmentShader: [
		'uniform sampler2D tDiffuse1;'
		'uniform sampler2D tDiffuse2;'
		'uniform float opacity;'
		'uniform float mixRatio;'
		'varying vec2 vUv;'
		'void main() {'
		'vec4 texel1 = texture2D( tDiffuse1, vUv );'
		'vec4 texel2 = texture2D( tDiffuse2, vUv );'
		'gl_FragColor = opacity * mix( texel1, texel2, mixRatio );"'
		'}'
	].join '\n'
		
return AdditiveBlendShader