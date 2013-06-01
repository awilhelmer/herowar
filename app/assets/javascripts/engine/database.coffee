_db = {}
_data = {}
_geometries = {}

database =

	get: (type, obj) ->
		@create type unless _db[type]?
		if _.isArray obj
			return (_db[type].get element for element in obj)
		if _.isFunction obj
			return _db[type].filter obj
		if _.isObject(obj) or _.isNumber(obj) or _.isString obj
			return _db[type].get obj
		_db[type]
	
	
	find: (type, attr) ->
		@create type unless _db[type]?
		_db[type].findWhere attr
		
		
	add: (type, obj, options) ->
    options = _.extend {merge:true}, options
    @create type unless _db[type]
    _db[type].add obj, options if obj
	
	create: (type, attributes) ->
    _db[type] = new (require 'models/' + type)(attributes, name: type)
	
	exists: (type) ->
    if _db[type] then true else false

	data: (data) ->
		_data = data if data
		_data
	
	geometry: (name, scene, geometry) ->
		if geometry
			_geometries[name] = {} unless _geometries[name]
			_geometries[name][scene] = geometry
		return if _geometries[name] then _geometries[name][scene] else null

return database