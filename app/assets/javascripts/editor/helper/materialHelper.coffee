Variables = require 'variables'
db = require 'database'

class MaterialHelper
	
	
	
	constructor: (@editor) ->
	

	getThreeMaterial: (object,  materialId) ->	
		for	key,value of object.material
			if value and value.name and value.name is materialId
				foundId = key
		if foundId
			object.material[foundId]
		else
			model = db.get 'materials', materialId
			#Transform to Three.Material!
			#Generate Key value? 
			#object.material[]
		
		
return MaterialHelper