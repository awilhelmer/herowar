###
		The loader provides the define and require logic to load libraries, modules and templates.
		
		@author Sebastian Sachtleben
###
(->
		# Variable declaration
		modules={}
		loaded={}
		
		###
				Define a module with name and callback.

				@param {String} The name to define.
				@param {Function} The callback function.
		###
		@define = (name, callback) ->
				modules[name] = callback
		
		###
				Require a defined callback.

				@param {String} The defined name.
		###
		@require = (name) ->
				# create an array out of names if only one string
				if _.isArray name
						#.log('isArray')
						require element for element in name 
				else
						##console.log('string ', name, loadModule name)
						throw 'The module "' + name + '" could not be found' unless modules[name]
						return loaded[name] if loaded[name]
						loadModule name
		
		###
				Load module by name.

				@param {String} The module name.
		###
		loadModule = (name) ->
				loaded[name] ?= if typeof modules[name] is 'string' then globalEval modules[name] else modules[name].call window

		###
				Use global eval (IE needs execScript).

				@param {String} The data to eval.
		###
		globalEval = (data) ->
				if data
						data = "(#{data})" if data.indexOf 'function' is 0
						content = eval.call @, data
				if typeof content is 'function' then content() else true
				
).call this

###
		Initialize our application on document ready.
###
$ -> (require 'application').start()