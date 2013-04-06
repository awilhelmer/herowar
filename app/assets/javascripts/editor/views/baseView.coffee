db = require 'database'

### 
    Create some default behavior vor Backbone.View
    - create subviews contained in a template (each data-view element)
    - load the model if a modelId is set
    - listen to the models change event and trigger render()
    - remove all subviews if remove is called
    
    WARNING:
    - do not use the same subview with the same model multiple times

    @author Alexander Kong
###
class BaseView extends Backbone.View

    # The name of the model that has to be pulled with the given
    # model id from the db object
    entity: null

    initialize: (options) ->
        @views = {}
        # modelId is a optional parameter (the whole collection is returned otherwise)
        if @entity and not @model
            @model = db.get @entity, @options.modelId
        @bindEvents()
        #clean subview dom element
        @$el.removeAttr 'data-view data-model'
        #add body class if is set:
        $('body').addClass options.bodyClass if options?.bodyClass
   
   # OVERRIDE to listen just to a specific field or something else
    bindEvents: ->
        @listenTo @model, 'change', @render if @model

        ###
        # Prevent default behaviour for internal links
        @$el.on 'click', 'a', (event) ->
            if event
                href = $(event.currentTarget).attr 'href'
                event.preventDefault() if href.indexOf('/') is 0
                app.router.navigate href, true
        ###

    # if any custom event handlers have been added (the @listenTo will be removed by Backbone.View
    unbindEvents: ->

   
    render: ->
        @$el.empty()
        html = ''
        if @template
            html = if @model then @template @getTemplateData() else @template()
            html = @_stripWhitespaces html
        $html = $ html
        @_renderSubviews $html
        @postRender $html
        @$el.append $html

    # OVERRIDE to add some post render behavior
    postRender: ($html) ->

    # OVERRIDE to do stuff after view is rendered
    afterRender: (time) ->


    # OVERRIDE allow view dependand modifications of the model
    getTemplateData: ->
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
        # console.log "remove called for #{@entity}"
        @unbindEvents()
        #remove all subviews
        view.remove() for key,view of @views
        $('body').removeClass @options.bodyClass if @options?.bodyClass
        super()
        
return BaseView