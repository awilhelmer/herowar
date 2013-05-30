BaseShader = require 'shaders/baseShader'

class FinalShader extends BaseShader

	uniforms:
		tDiffuse: 
			type: 't' 
			value: 0
			texture: null
		tGlow: 
			type: 't' 
			value: 1
			texture: null

		
	fragmentShader: [
		'uniform sampler2D tDiffuse;'
		'uniform sampler2D tGlow;'
		'varying vec2 vUv;'
		'void main() {'
		'vec4 texel = texture2D( tDiffuse, vUv );'
		'vec4 glow = texture2D( tGlow, vUv );'
		'gl_FragColor = texel + vec4(0.5, 0.75, 1.0, 1.0) * glow * 2.0;'
		'}'
	].join '\n'
		
return FinalShader