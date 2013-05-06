Router = require 'router'

class GameRouter extends Router

    routes:
        'game'  : 'gamePreloader'
        'game2' : 'game'

return GameRouter