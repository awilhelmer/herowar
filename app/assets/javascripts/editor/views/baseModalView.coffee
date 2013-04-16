BaseView = require 'views/baseView'

class BaseModalView extends BaseView

	bindEvents: ->
		@$el.on 'show', @onShow
		@$el.on 'shown', @onShown
		@$el.on 'hide', @onHide
		@$el.on 'hidden', @onHidden

	onShow: (event) ->

	onShown: (event) ->

	onHide: (event) ->

	onHidden: (event) ->

	toggle: ->
		@$el.modal 'toggle'

	show: ->
		@$el.modal 'show'

	hide: ->
		@$el.modal 'hide'

return BaseModalView