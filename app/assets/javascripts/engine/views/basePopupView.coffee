BaseView = require 'views/baseView'

class BasePopupView extends BaseView

	postRender: ($html) ->
		console.log 'Adding popup class to: ', @$el
		@$el.addClass 'popup' if @$el
		super $html	
	
return BasePopupView