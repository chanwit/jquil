class JquiTagLib {

    static namespace = "j"

    static all=['core','tree','layout']

    def using = { attrs, body ->
        if(!pageScope.variables.containsKey('jquery')) {
            out << g.javascript(library:"jquery")
            pageScope.jquery = true
        }

        def libs = attrs.remove("library")
        if(!libs) libs = attrs.remove("libraries")
        if(libs.trim() == "all" || libs == "*" ) {
            all.each { lib ->
                "using_${lib.trim()}"()
            }
        } else {
            libs?.split(",").each { lib ->
                "using_${lib.trim()}"()
            }
        }
    }

    /**
     Core
    **/
    private using_core() {
        out << "<script type=\"text/javascript\" src=\"${g.resource(dir:'js',file:'jquery/jquery-ui-1.7.2.min.js')}\"></script>\n"
        out << "<link rel=\"stylesheet\" href=\"${resource(dir:'css',file:'themes/base/ui.all.css')}\" />\n"
    }

    def groupbox = { attrs, body ->
        def id = attrs['id']
        def caption = attrs.remove('caption')
        def image   = attrs.remove('image')
        out << "<div ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << "<h3><a href=\"#\">${caption}</a></h3>"
        out << "<div><p>"
        out << body()
        out << "</p></div>"
        out << "</div>"
        out << """
        <script type="text/javascript">
            jQuery(document).ready(function(){
                jQuery("#${id}").accordion({
                    collapsible: true,
                    animated: false
                });
            });
        </script>"""
    }

    // TODO
    def tab = { attrs, body ->

    }

    /**
     Layout
    **/
    private using_layout() {
        out << "<script type=\"text/javascript\" src=\"${g.resource(dir:'js',file:'jquery/jquery.layout.min-1.2.0.js')}\"></script>\n"
    }

    def borderlayout = { attrs, body ->

    }
    def north = { attrs, body ->

    }
    def center = { attrs, body ->

    }
    def east = { attrs, body ->

    }
    def west = { attrs, body ->

    }
    def south = { attrs, body ->

    }

    /**
     Tree
     TODO: next implementation would use jsTree
    **/
    private using_tree() {
        out << "<script type=\"text/javascript\" src=\"${g.resource(dir:'js',file:'jquery/jquery.treeview.min.js')}\"></script>\n"
        out << "<link rel=\"stylesheet\" href=\"${resource(dir:'css',file:'jquery.treeview.css')}\" />\n"
    }

    def tree = { attrs, body ->
        def id = attrs['id']
        def persist = attrs.remove('persist')
        if(!persist) persist = "location"

        def collapsed = attrs.remove('collapsed')
        if(!collapsed) collapsed = "false"

        def unique = attrs.remove('unique')
        if(!unique) unique = "false"

        out << "<ul ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</ul>"
        out << """
        <script type="text/javascript">
            jQuery(document).ready(function(){
                jQuery("#${id}").treeview({
                    persist: "${persist}",
                    collapsed: ${collapsed},
                    unique: ${unique}
                });
            });
        </script>"""
    }

    def treeitem = { attrs, body ->
        out << "<li ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</li>"
    }
}