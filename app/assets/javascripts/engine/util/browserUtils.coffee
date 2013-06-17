browserUtils = 

    getDomainName: (url) ->
        if _.isString url
            url = "http://#{url}" unless url.indexOf('http://') is 0
            a = document.createElement 'a'
            a.href = url
            str = a.hostname
        else
            str = window.location.hostname
        if str.split('.').length > 2
            str = str.replace /^[\w\-]+\./, ''
        return str

return browserUtils
