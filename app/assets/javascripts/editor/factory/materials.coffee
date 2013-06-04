__id__ = 0
__materials__ = {}

materials =

	create: (type, opts) ->
		key = "#{type}"
		id = __id__++
		#__materials__[key] = {} unless _.has __materials__, key
		__materials__[id] = new type opts
		return [ id, __materials__[id] ]

	dispose: (id) ->
		__materials__[id].dispose()
		delete __materials__[id]
		return

return materials
