geometryUtils = 

	clone: (srcGeometry) ->
		destGeometry = srcGeometry.clone()
		###
		unless srcGeometry.morphTargets.length is 0
			for target in srcGeometry.morphTargets 
				newTarget = {}
				newTarget.name = target.name
				newTarget.vertices = []
				newTarget.vertices.push vertex.clone()  for vertex in target.vertices
				destGeometry.morphTargets.push newTarget
		###
		destGeometry.morphTargets = srcGeometry.morphTargets
		destGeometry.morphNormals = srcGeometry.morphNormals
		destGeometry.boundingBox = new THREE.Box3()
		destGeometry.boundingBox.copy srcGeometry.boundingBox
		destGeometry.boundingSphere = new THREE.Sphere()
		destGeometry.boundingSphere.copy srcGeometry.boundingSphere
		return destGeometry
		
return geometryUtils
