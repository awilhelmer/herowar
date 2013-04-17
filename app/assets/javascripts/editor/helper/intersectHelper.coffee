Variables = require 'variables'

class IntersectHelper extends THREE.Raycaster

	constructor: (@editor) ->
		super()
		@projector = new THREE.Projector()
		@sphere = new THREE.Sphere()
		@localRay = new THREE.Ray()
		@facePlane = new THREE.Plane()
		@intersectPoint = new THREE.Vector3()
		@matrixPosition = new THREE.Vector3()
		@inverseMatrix = new THREE.Matrix4()

	mouseIntersects: (objects, faceRadius) ->
		vector = new THREE.Vector3(
			((Variables.MOUSE_POSITION_X - Variables.SCREEN_LEFT) / Variables.SCREEN_WIDTH) * 2 - 1
			-((Variables.MOUSE_POSITION_Y - Variables.SCREEN_TOP) / Variables.SCREEN_HEIGHT) * 2 + 1
			0.5)
		camera = @editor.engine.viewhandler.views[0].camera	# TODO: find out from which viewport this click comes
		@projector.unprojectVector vector, camera
		@set camera.position, vector.sub(camera.position).normalize()
		intersectList = @intersectObjects objects, true		
		intersectList = @handleFaceRadius(intersectList, faceRadius)
		intersectList


	handleFaceRadius: (intersectList, faceRadius) ->
		for intersect, idx in intersectList
			obj = intersect.object
			intersect.faces = [intersect.face]
			if faceRadius and faceRadius > 1 and obj.name isnt 'wireframe'
				faceIndex = intersect.faceIndex
				@inverseMatrix.getInverse obj.matrixWorld
				@localRay.copy(@ray).applyMatrix4 @inverseMatrix 
				for face,key in obj.geometry.faces
					if key != faceIndex
						material = obj.material.materials[face.materialIndex]
						@facePlane.setFromNormalAndCoplanarPoint face.normal, obj.geometry.vertices[face.a]
						planeDistance = @localRay.distanceToPlane @facePlane 
						if Math.abs planeDistance + faceRadius < @precision 
							continue
						if planeDistance  + faceRadius < 0 
							continue
						side = material.side
						if side != THREE.DoubleSide
							planeSign = @localRay.direction.dot @facePlane.normal
							unless side is THREE.FrontSide and (planeSign  < 0 or planeSign > faceRadius )
								continue
						if planeDistance + faceRadius   < this.near or planeDistance  > this.far + faceRadius
							continue
						##TODO intersectpoint with radius
						@intersectPoint = @localRay.at planeDistance + faceRadius, @intersectPoint
						a = obj.geometry.vertices[face.a].clone()
						b = obj.geometry.vertices[face.b].clone()
						c = obj.geometry.vertices[face.c].clone()
						if face instanceof THREE.Face3
							unless THREE.Triangle.containsPoint @intersectPoint, a, b, c
								continue
						else 
							d = obj.geometry.vertices[face.d].clone()
							if not THREE.Triangle.containsPoint(@intersectPoint, a, b, d) and not THREE.Triangle.containsPoint(@intersectPoint, b, c, d)
								continue
						intersect.faces.push face
		intersectList
				
return IntersectHelper