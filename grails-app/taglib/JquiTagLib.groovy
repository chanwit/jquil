import java.util.UUID;

class JquiTagLib {

    static namespace = "j"

    static all=['core','tree','layout']

    def using = { attrs, body ->
        if(!pageScope.variables.containsKey('jquery')) {
            out << jquery()
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

    private jquery() {
        // return g.javascript(library:"jquery")
        return "<script type=\"text/javascript\" src=\"${g.resource(dir:'js',file:'jquery/jquery-1.4.min.js')}\"></script>\n"
    }

    /**
     Core
    **/
    private using_core() {
        out << "<script type=\"text/javascript\" src=\"${g.resource(dir:'js',file:'jquery/jquery-ui-1.7.2.min.js')}\"></script>\n"
        out << "<link rel=\"stylesheet\" href=\"${resource(dir:'css',file:'themes/base/ui.all.css')}\" />\n"
    }

    def accordion = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "A" + UUID.randomUUID()
        pageScope.parent = "accordion"
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</div>"
        out << """
        <script type="text/javascript">
            jQuery(document).ready(function(){
                jQuery("#${id}").accordion({
                    animated: false
                });
            });
        </script>"""
        pageScope.parent = null
    }

    def panel = { attrs, body ->
        "panel_${pageScope.parent}"(attrs, body)
    }

    private panel_accordion(attrs, body) {
        def caption = attrs['caption']
        def image   = attrs['image']
        out << "<h3><a href=\"#\">${caption}</a></h3>"
        out << "<div><p>"
        out << body()
        out << "</p></div>"
    }

    def groupbox = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "G" + UUID.randomUUID()

        def caption = attrs.remove('caption')
        def image   = attrs.remove('image')
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
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
        pageScope.parent = "borderlayout"
        pageScope.borderlayout_north  = null
        pageScope.borderlayout_east   = null
        pageScope.borderlayout_west   = null
        pageScope.borderlayout_south  = null
        pageScope.borderlayout_center = null
        out << body()
        out << """
        <script type="text/javascript">
            jQuery(document).ready(function () {
	            jQuery('body').layout({
	                applyDefaultStyles: true
	                ${paneSelector('north')}
	                ${paneSelector('west')}
	                ${paneSelector('east')}
	                ${paneSelector('south')}
	                ${paneSelector('center')}
	            });
            });
        </script>"""
        pageScope.parent = "null"
    }
    private paneSelector(pane) {
        if(pageScope["borderlayout_${pane}"])
            return ", ${pane}__paneSelector: '#${pageScope["borderlayout_${pane}"]}'"
        else
            return ""
    }
    def north = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "N" + UUID.randomUUID()
        pageScope.borderlayout_north = id
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</div>"
    }
    def center = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "C" + UUID.randomUUID()
        pageScope.borderlayout_center = id
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</div>"
    }
    def east = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "E" + UUID.randomUUID()
        pageScope.borderlayout_east = id
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</div>"
    }
    def west = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "W" + UUID.randomUUID()
        pageScope.borderlayout_west = id
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</div>"
    }
    def south = { attrs, body ->
        def id = attrs.remove('id')
        if(!id) id = "S" + UUID.randomUUID()
        pageScope.borderlayout_south = id
        out << "<div id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
        out << body()
        out << "</div>"
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
        def id = attrs.remove('id')
        if(!id) id = "T" + UUID.randomUUID()

        def persist = attrs.remove('persist')
        if(!persist) persist = "location"

        def collapsed = attrs.remove('collapsed')
        if(!collapsed) collapsed = "false"

        def unique = attrs.remove('unique')
        if(!unique) unique = "false"

        out << "<ul id=\"${id}\" ${attrs.collect{ k, v -> "$k=\"$v\""}.join(' ')}>"
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