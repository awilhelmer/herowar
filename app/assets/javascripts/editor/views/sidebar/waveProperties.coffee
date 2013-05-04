EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class WaveProperties extends BasePropertiesView
	
	id: 'sidebar-properties-wave'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/waveProperties.tmpl'

	events:
		'change input[name="name"]'				: 'onChangedString'

	bindEvents: ->
		EditorEventbus.selectWaveUI.add @selectItem

	selectItem: (value) =>
		@model = db.get 'waves', value
		@render()

return WaveProperties