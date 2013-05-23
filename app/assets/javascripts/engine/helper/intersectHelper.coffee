Variables = require 'variables'
db = require 'database'

class IntersectHelper extends THREE.Raycaster

	constructor: ->
		@input = db.get 'input'
		super()
		@viewports = db.get 'ui/viewports'
		@projector = new THREE.Projector()
		@sphere = new THREE.Sphere()
		@localRay = new THREE.Ray()
		@facePlane = new THREE.Plane()
		@intersectPoint = new THREE.Vector3()
		@matrixPosition = new THREE.Vector3()
		@inverseMatrix = new THREE.Matrix4()

	mouseIntersects: (objects, faceRadius) ->
		camera = @viewports.at(0).get('cameraScene')	# TODO: find out from which viewport this click comes
		vector = new THREE.Vector3(
			((@input.get('mouse_position_x') - Variables.SCREEN_LEFT) / Variables.SCREEN_WIDTH) * 2 - 1
			-((@input.get('mouse_position_y') - Variables.SCREEN_TOP) / Variables.SCREEN_HEIGHT) * 2 + 1
			0.5)
		if camera instanceof THREE.OrthographicCamera
			ray = @projector.pickingRay vector, camera
			intersectList = ray.intersectObjects objects, true
		else
			@projector.unprojectVector vector, camera
			@set camera.position, vector.sub(camera.position).normalize()
			intersectList = @intersectObjects objects, true		
		intersectList = @handleFaceRadius intersectList, faceRadius
		intersectList

	getIntersectObject: (intersectList) ->
		for value, key in intersectList
			if value.object and value.object.name isnt 'wireframe'
				result = value
				break
		unless result
			result = intersectList[0]
		result

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
						@intersectPoint = @localRay.at planeDistance, @intersectPoint
						a = obj.geometry.vertices[face.a].clone()
						b = obj.geometry.vertices[face.b].clone()
						c = obj.geometry.vertices[face.c].clone()
						if face instanceof THREE.Face3
							tri1 =  @getTriangle(a,b,c, faceRadius)
							unless THREE.Triangle.containsPoint @intersectPoint, tri1.a, tri1.b, tri1.c
								continue
						else 
							d = obj.geometry.vertices[face.d].clone()
							tri1 =  @getTriangle(a,b,d, faceRadius)
							tri2 =  @getTriangle(b,c,d, faceRadius)
							if not THREE.Triangle.containsPoint(@intersectPoint, tri1.a, tri1.b, tri1.c) and not THREE.Triangle.containsPoint(@intersectPoint, tri1.a, tri1.b, tri1.c)
								continue
						intersect.faces.push face
		intersectList
				
	getTriangle: (a, b, c, faceRadius) ->
		tri1 = new THREE.Triangle a, b, c
		mid1 = tri1.midpoint()
		tri1.a = tri1.a.sub(mid1).multiplyScalar(faceRadius).add mid1
		tri1.b = tri1.b.sub(mid1).multiplyScalar(faceRadius).add mid1
		tri1.c = tri1.c.sub(mid1).multiplyScalar(faceRadius).add mid1
		tri1
			
return IntersectHelper