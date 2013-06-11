class Bolt extends THREE.Mesh
	
	branchSegments: 5
	
	totalLinks: 100
	
	linkDist: 1
		
	basePoint: new THREE.Vector3()
	branchPoint: new THREE.Object3D()
	
	constructor: (materials, @radius) ->
		super()
		@geometry = new THREE.Geometry()
		@material = materials
		@branchPoint.position = new THREE.Vector3()
		@radiusStep = @radius / @totalLinks
		@segmentAngle = Math.PI * 2 / @branchSegments
		@numCurrentPos = 0
		@offsetPoints = new Array @totalLinks
		@ring = new Array @totalLinks
		@ringOrigin = new Array @totalLinks

	build: ->
		for i in [0..@totalLinks - 1]
			@basePoint.copy @branchPoint.position
			@branchPoint.translateZ @linkDist
			diffVector = new THREE.Vector3()
			diffVector.subVectors @branchPoint.position, @basePoint
			transformPoint = new THREE.Vector3()
			transformPoint.addVectors diffVector, new THREE.Vector3 0, 10, 0
			@R = new THREE.Vector3()
			@R.crossVectors transformPoint, diffVector
			@S = new THREE.Vector3()
			@S.crossVectors @R, diffVector
			@R.normalize()
			@S.normalize()
			@buildNode()
			@branchPoint.updateMatrix()
			@numCurrentPos++
		@geometry.computeCentroids()
		@geometry.computeFaceNormals()
		@geometry.computeVertexNormals()
		return
	
	buildNode: ->
		bFirstNode = @numCurrentPos is 0
		@offsetPoints[@numCurrentPos] = new THREE.Vector3()
		@ring[@numCurrentPos] = new Array()
		@ringOrigin[@numCurrentPos] = new Array()
		for i in [0..@branchSegments - 1]
			transformedRadius = if @radius < 0.3 then 0.3 else @radius
			pX = @basePoint.x + transformedRadius * Math.cos(i * @segmentAngle) * @R.x + transformedRadius * Math.sin(i * @segmentAngle) * @S.x
			pY = @basePoint.y + transformedRadius * Math.cos(i * @segmentAngle) * @R.y + transformedRadius * Math.sin(i * @segmentAngle) * @S.y
			pZ = @basePoint.z + transformedRadius * Math.cos(i * @segmentAngle) * @R.z + transformedRadius * Math.sin(i * @segmentAngle) * @S.z
			@geometry.vertices.push new THREE.Vector3 pX, pY, pZ
			@geometry.colors.push new THREE.Color 0xffffff
			@ring[@numCurrentPos].push new THREE.Vector3 pX, pY, pZ
			@ringOrigin[@numCurrentPos].push new THREE.Vector3 pX, pY, pZ
		return if bFirstNode
		for i in [0..@branchSegments - 1]
			if i < @branchSegments - 1
				p1 = @geometry.vertices.length - @branchSegments + i + 1
				p4 = @geometry.vertices.length - @branchSegments + i
				p2 = @geometry.vertices.length - @branchSegments * 2 + i + 1
				p3 = @geometry.vertices.length - @branchSegments * 2 + i
			else
				p1 = @geometry.vertices.length - @branchSegments
				p4 = @geometry.vertices.length - @branchSegments + i
				p2 = @geometry.vertices.length - @branchSegments * 2
				p3 = @geometry.vertices.length - @branchSegments * 2 + i
			@geometry.faces.push new THREE.Face4 p1, p2, p3, p4
			startX = 1 / @branchSegments * (i + 1)
			endX = startX - 1 / @branchSegments
			startY = @numCurrentPos / @totalLinks
			endY = startY + 1 / @totalLinks
			@geometry.faceVertexUvs[0].push [
				new THREE.Vector2 startX, endY
				new THREE.Vector2 startX,startY
				new THREE.Vector2 endX ,startY
				new THREE.Vector2 endX, endY
			]
		return

return Bolt