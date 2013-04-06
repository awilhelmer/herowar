templates = 

    get: (name) ->
        template = require name
        Handlebars.template template if template?
    
return templates