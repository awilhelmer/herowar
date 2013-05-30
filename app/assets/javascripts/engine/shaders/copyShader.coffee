BaseShader = require 'shaders/baseShader'

class CopyShader extends BaseShader
		
	fragmentShader: [
		'uniform float opacity;'
		'uniform sampler2D tDiffuse;'
		'varying vec2 vUv;'
		'void main() {'
		'vec4 texel = texture2D( tDiffuse, vUv );'
		'gl_FragColor = opacity * texel;'
		'}'
	].join '\n'
		
return CopyShader