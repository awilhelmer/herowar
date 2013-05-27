Router = require 'router'

class EditorRouter extends Router

	routes:
		'viewer'  : 'viewer'
		'editor'  : 'editorPreloader'
		'editor2' : 'editor'

return EditorRouter