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
				a = obj.geometry.vertices[intersect.face.a].clone().addScalar(faceRadius * 20)
				b = obj.geometry.vertices[intersect.face.b].clone().addScalar(faceRadius * 20)
				c = obj.geometry.vertices[intersect.face.c].clone().addScalar(faceRadius * 20)
				if intersect.face instanceof THREE.Face4
					d = obj.geometry.vertices[intersect.face.d]
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
						@intersectPoint = @localRay.at planeDistance-faceRadius, @intersectPoint
						if key + 1 is faceIndex 
							console.log "DEBUG INtersection Point <x=#{@intersectPoint.x} y=#{@intersectPoint.y} z=#{@intersectPoint.z}>"
						if face instanceof THREE.Face3
							unless THREE.Triangle.containsPoint @intersectPoint, a, b, c
								continue
						else 
							if not THREE.Triangle.containsPoint(@intersectPoint, a, b, d) and not THREE.Triangle.containsPoint(@intersectPoint, b, c, d)
								if key + 1 is faceIndex 
									console.log "no intersection..."
								continue
						intersect.faces.push face
					else
						console.log "DEBUG Triangles: A <x=#{a.x} y=#{a.y} z=#{a.z}> B <x=#{b.x} y=#{b.y} z=#{b.z}> C <x=#{c.x} y=#{c.y} z=#{c.z}> D <x=#{d.x} y=#{d.y} z=#{d.z}> "
						
						
		intersectList
				
return IntersectHelper