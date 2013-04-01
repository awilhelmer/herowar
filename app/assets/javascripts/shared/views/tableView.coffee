BaseView = require 'views/baseView'

###
    The TableView provides basic functionalities to edit entries in a table view.

    @author Sebastian Sachtleben
###
class TableView extends BaseView
	
	initialize: (options) ->
		super options
		@model.fetch()
		
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

return TableView