_popups = []
_popupView = undefined
$doc = $ 'html'

popup = 
    
    isOpen: ->
        _popups.length > 0
    
    open: (options)->
        throw "options.view is required to be opened in a popup" unless options.view
        @_createBackground() if _popups.length is 0
        # toggle old popup
        @_toggle()
        _popups.push options.view
        _popupView.$el.prepend options.view.$el.addClass 'content'
        options.view.render()
        
    close: ->
        console.log "close popup popup called"
        return if _popups.length is 0
        view = _popups.pop()
        view.remove()
        @_revertUrl()
        # toggle previous popup again
        @_toggle()
        @remove() if _popups.length is 0
        
    remove: ->
        #close all popups 
        view.remove() for view in _popups
        _popupView?.remove()
        app = require 'application'
        delete app.views.popup        
        _popups.length = 0
        _popupView = undefined
        
    
    _revertUrl: ->
        app = require 'application'
        app.history.pop()
        last = _.last app.history
        app.router.navigate last if last
           
    _toggle: ->
        _popups[_popups.length-1].$el.toggle() if _popups.length > 0
    
    _createBackground: ->
        _popupView = new (require 'views/popup')()
        app = require 'application'
        app.views.popup = @
        _popupView.render()
        $body = $ 'body'
        $body.append _popupView.el
        #use defer here for doing css animation on popup
        _.defer ->
            $doc.addClass 'popupIsOpen'
        
return popup