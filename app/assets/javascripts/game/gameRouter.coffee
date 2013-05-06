Router = require 'router'

class GameRouter extends Router

    routes:
        "game"  : "preloader"
        "game2" : "game"

return GameRouter