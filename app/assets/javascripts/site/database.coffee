_db = {}

###
    The database stores entries from our json api.

    @author Sebastian Sachtleben
    @author Alexander Kong
###
database =         

    ###
        Get list or specific object depending on the second parameter

        @param {String} The model name.
        @param {Object} The id inside of the collection.
        @return {Object} The collection entry.
    ###
    get: (type, obj) ->
        @create type unless _db[type]?
        if _.isArray obj
            return (_db[type].get element for element in obj)
        if _.isFunction obj
            return _db[type].filter obj
        if _.isObject(obj) or _.isNumber(obj) or _.isString obj
            return _db[type].get obj
        _db[type]

    ###
        Add new db collection or entry.

        @param {String} The model name.
        @param {Object} The object to add.
        @param {Object} The add to collection options.
    ###
    add: (type, obj, options) ->
        options = _.extend {merge:true}, options
        @create type unless _db[type]
        _db[type].add obj, options if obj
    
    ###
        Create new collection.

        @param {String} The collection name.
        @param {Object} The attributes if its a model.
    ###
    create: (type, attributes) ->
        _db[type] = new (require 'models/' + type)(attributes, name: type)

    ###
        Check if db type exists.

        @param {String} The model name.
    ###
    exists: (type) ->
        if _db[type] then true else false

return database