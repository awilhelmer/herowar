class Material extends Backbone.Model

	constructor: (id, materialId, name, color) ->
		super()
		@set 'id', id if id
		@set 'materialId', materialId if materialId
		@set 'name', name if name
		@set 'color', color if color
		@set
			'transparent' : false
			'opacity'			: 1
			'map'					: undefined

return Material