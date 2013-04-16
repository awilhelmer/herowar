BaseView = require 'views/baseView'

class BaseModalView extends BaseView

	hideModal: ->
		@$el.modal 'hide'

return BaseModalView