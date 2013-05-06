Router = require 'router'

class EditorRouter extends Router

    routes:
        'editor'  : 'editorPreloader'
        'editor2' : 'editor'

return EditorRouter