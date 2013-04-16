BaseView = require 'views/baseView'

class BaseModalView extends BaseView

	initialize: (options) ->
		@state = 'hidden'
		super options

	bindEvents: ->
		@$el.on 'show', @onShow
		@$el.on 'shown', @onShown
		@$el.on 'hide', @onHide
		@$el.on 'hidden', @onHidden

	onShow: (event) =>
		@state = 'show'

	onShown: (event) =>
		@state = 'shown'

	onHide: (event) =>
		@state = 'hide'

	onHidden: (event) =>
		@state = 'hidden'

	toggle: =>
		@$el.modal 'toggle'

	show: =>
		@$el.modal 'show'

	hide: =>
		@$el.modal 'hide'

return BaseModalView