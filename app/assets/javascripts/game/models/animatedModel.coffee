BaseModel = require 'models/basemodel'

class AnimatedModel extends BaseModel

  id: null
  
  name: null
  
  meshBody: null
  
  activeAnimation = null
  
  animationFPS: 6  

  constructor: (@id, @name, @meshBody) ->
    # Enable shadows
    @meshBody.castShadow = true
    @meshBody.receiveShadow = true
    # Create Object3D
    obj = new THREE.Object3D()
    obj.name = @name
    obj.useQuaternion = true
    obj.add @meshBody
    @setAnimation @meshBody.geometry.firstAnimation if @meshBody.geometry.firstAnimation
    super obj

  update: (delta) ->
    @animate delta

  animate: (delta) ->
    @meshBody.updateAnimation 1000 * delta  if @meshBody and @activeAnimation
  
  setAnimation: (name) ->
    console.log "Model #{@name}-#{@id} set animation to #{name}"
    if @meshBody
      @meshBody.playAnimation name, @animationFPS 
      @meshBody.baseDuration = @meshBody.duration
    @activeAnimation = name

return AnimatedModel