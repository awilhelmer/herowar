inputEvents = 
  
  registerInput: (element, type, func) ->
    element.addEventListener type, func

return inputEvents