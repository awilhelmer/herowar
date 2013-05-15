class BaseControls
  
  constructor: (@view) ->
    @domElement = @view.get 'domElement'
    @addEventListener()
    
  addEventListener: ->
    console.log "Add input event listener for viewport #{view.get('domId')}"
    @domElement.addEventListener 'keyup', (event) => @onKeyUp event
    @domElement.addEventListener 'keydown', (event) => @onKeyDown event
    @domElement.addEventListener 'mouseup', (event) => @onMouseUp event
    @domElement.addEventListener 'mousedown', (event) => @onMouseDown event
    @domElement.addEventListener 'mousemove',(event) => @onMouseMove event
    @domElement.addEventListener 'mousewheel', (event) => @onMouseWheel event
    @domElement.addEventListener 'DOMMouseScroll', (event) => @onDOMMouseScroll event
    @domElement.addEventListener 'touchstart', (event) => @onTouchStart event
    @domElement.addEventListener 'touchend', (event) => @onTouchEnd event
    @domElement.addEventListener 'touchmove', (event) => @onTouchMove event 

  onKeyUp: (event) ->

  onKeyDown: (event) ->
    
  onMouseUp: (event) ->
    
  onMouseDown: (event) ->
    
  onMouseMove: (event) ->
  
  onMouseWheel: (event) ->
    
  onDOMMouseScroll: (event) ->

  onTouchStart: (event) ->

  onTouchEnd: (event) ->

  onTouchMove: (event) ->

return BaseControls