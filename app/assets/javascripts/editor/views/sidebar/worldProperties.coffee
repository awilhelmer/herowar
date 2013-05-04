EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class WorldProperties extends BasePropertiesView

	id: 'sidebar-properties-world'

	className: 'sidebar-panel'

	entity: 'world'

	template: templates.get 'sidebar/worldProperties.tmpl'

	events:
		'change input[name="name"]'						: 'onChangedString'
		'change textarea[name="description"]'	: 'onChangedString'
		'change select[name="skybox"]'				: 'onChangedString'
		'change select[name="teamSize"]'			: 'onChangedInteger'
		'change input[name="prepareTime"]'		: 'onChangedInteger'
		'change input[name="lives"]'					: 'onChangedInteger'
		'change input[name="goldStart"]'			: 'onChangedInteger'
		'change input[name="goldPerTick"]'		: 'onChangedInteger'

return WorldProperties