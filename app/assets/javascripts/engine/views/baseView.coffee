log = require 'util/logger'
db = require 'database'

class BaseView extends Backbone.View

	# The name of the model that has to be pulled with the given model id from the db object
	entity: null

	initialize: (options) ->
		@views = {}
		# modelId is a optional parameter (the whole collection is returned otherwise)
		if @entity and not @model
			@model = db.get @entity, @options.modelId
		@bindEvents()
		@bindFetchEvents()
		#clean subview dom element
		@$el.removeAttr 'data-view data-model'
		#add body class if is set:
		$('body').addClass options.bodyClass if options?.bodyClass
 
	# OVERRIDE to listen just to a specific field or something else
	bindEvents: ->
		if @model
			@listenTo @model, 'change', @render if @model instanceof Backbone.Model
			@listenTo @model, 'add remove change reset', @render if @model instanceof Backbone.Collection

	bindFetchEvents: ->
		if @model
			@listenTo @model, 'fetching', => @$el.addClass 'loading'
			@listenTo @model, 'fetched', => @$el.removeClass 'loading'

	# if any custom event handlers have been added (the @listenTo will be removed by Backbone.View
	unbindEvents: ->
 
	render: ->
			@$el.empty()
			html = ''
			if @template
					data = @getTemplateData()
					html = if data then @template data else @template()
					html = @_stripWhitespaces html
			$html = $ html
			@_renderSubviews $html
			@postRender $html
			@$el.append $html
			setTimeout @afterRender, 1			

	# OVERRIDE to add some post render behavior
	postRender: ($html) ->

	# OVERRIDE to do stuff after view is rendered
	afterRender: ->

	# OVERRIDE allow view dependand modifications of the model
	getTemplateData: ->
		unless @model then return {}
		if _.isFunction @model.toJSON then @model.toJSON() else @model 
	
	_stripWhitespaces: (html) ->
			return html.replace(/>\s+</g, '><').replace(/^\s*/, '').replace /\s*$/, ''
	
	# subviews are saved for each view with "viewName-modelId-index" as key and the corresponding subview as view
	# with viewName the name of the view, modelId the id of the model required for that view (without entity name)
	# and index a counter if a view is used multiple times with the same 
	_renderSubviews: ($html) ->
		$list = $html.filter('[data-view]').add $html.find '[data-view]'
		# keyIndexes counts up if a view-model combination occurs multiple times
		keyIndexes = {}
		$list.each (i, el) =>
			$el = $ el
			view = $el.data 'view'
			model = $el.data 'model' || 'model'
			key = "#{view}-#{model}"
			keyIndexes[key] = if keyIndexes[key] then keyIndexes[key]++ else 1
			key += "-#{keyIndexes[key]}"
			if @views[key]
				@views[key].setElement el
			else
				@views[key] = new (require view) el: $el, modelId: model, parent: @
			@views[key].render()

	remove: ->
		@unbindEvents()
		#remove all subviews
		view.remove() for key,view of @views
		$('body').removeClass @options.bodyClass if @options?.bodyClass
		super()

	getEntry: (event, modul) ->
		$currentTarget = $ event.currentTarget
		property = $currentTarget.attr('name').replace modul, ""
		if $currentTarget.attr('type') is 'checkbox'
			val = $currentTarget.is(':checked')
		else 
			val = $currentTarget.val()		
		log.debug "Found \"#{val}\" for input field \"#{modul}property #{property}\""
		property: property, value:val

return BaseView