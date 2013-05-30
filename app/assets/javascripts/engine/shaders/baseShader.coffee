class BaseShader

	uniforms:
		tDiffuse: 
			type: 't' 
			value: null
	
	vertexShader: [
		'varying vec2 vUv;'
		'void main() {'
		'vUv = uv;'
		'gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );'
		'}'
	].join '\n'

return BaseShader