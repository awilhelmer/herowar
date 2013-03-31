###
    The templates helps to load precompiled handlebars templates.

    @author Sebastian Sachtleben
###
templates = 

    ###
        Load template by name via our loader and return handlebars template function.

        @param {String} The template name.
        @return {Function} The precompiled handlebars template.
    ###
    get: (name) ->
        template = require name
        Handlebars.template template if template?
    
return templates