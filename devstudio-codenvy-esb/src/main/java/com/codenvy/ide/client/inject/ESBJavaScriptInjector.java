package com.codenvy.ide.client.inject;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;


/**
 * Wrapper around functionality used to inject external Javascript code into
 * your application.
 * 
 * @author Sohani
 */
public class ESBJavaScriptInjector {

    private static HeadElement head;

    public static void inject(String javascript) {
        
        HeadElement head = getHead();
        new Timer() {
            
            public void run() {
               // goJsPlumb();
                goJS();
            }
        }.schedule(1);
        
   
        ScriptElement element = createScriptElement();
        element.setText(javascript);
        head.appendChild(element);
    }


public static native void goJsPlumb() /*-{
        
        var t1 = $doc.getElementById('state1');
        
        var t2 = $doc.getElementById('state2');
        var targetOption = {anchor:"TopCenter",
                                                    maxConnections:-1, 
                                                    isSource:false, 
                                                    isTarget:true, 
                                                    endpoint:["Dot", {radius:5}], 
                                                    paintStyle:{fillStyle:"#66FF00"},
                                                        setDragAllowedWhenFull:true}
                                                        
                var sourceOption = {anchor:"BottomCenter",
                                                        maxConnections:-1, 
                                                    isSource:true, 
                                                    isTarget:false, 
                                                    endpoint:["Dot", {radius:5}], 
                                                    paintStyle:{fillStyle:"#FFEF00"},
                                                        setDragAllowedWhenFull:true}
                
                $wnd.jsPlumb.bind("ready", function() {
                        
                        $wnd.jsPlumb.addEndpoint($doc.getElementById('state1'), targetOption);
                        $wnd.jsPlumb.addEndpoint($doc.getElementById('state1'), sourceOption);
                        
                        $wnd.jsPlumb.addEndpoint($doc.getElementById('state2'), targetOption);
                        $wnd.jsPlumb.addEndpoint($doc.getElementById('state2'), sourceOption);
                        
                        $wnd.jsPlumb.draggable($doc.getElementById('state1'));
                        $wnd.jsPlumb.draggable($doc.getElementById('state2'));
                });
   
       
    }-*/;

   public static native void goJS() /*-{
   
    $wnd.alert("starting...");
    $wnd.jsPlumb.ready(function() {
    // default settings for connectors
     
            var connectionParams = {
                connector: ["Flowchart", {cornerRadius:1}],
                paintStyle:{ 
                    lineWidth: 1,
                    outlineColor:"black",
                    outlineWidth: 0
                },
                detachable:false,
                endpointStyle: { radius:1 }
            };

            $wnd.jsPlumb.connect({
                source: $doc.getElementById('state1'), 
                target: $doc.getElementById('state2'),
                anchors: ["Right", "Bottom"]
            }, connectionParams);
            
            
             $wnd.jsPlumb.draggable($doc.getElementById('state1'));
             $wnd.jsPlumb.draggable($doc.getElementById('state2'));

        });

   
   }-*/;
   
    private static ScriptElement createScriptElement() {
        ScriptElement script = Document.get().createScriptElement();
        script.setAttribute("language", "javascript");
        return script;
        
    }

    private static HeadElement getHead() {
         if (head == null) {
               Element element = Document.get().getElementsByTagName("head").getItem(0);
               assert element != null : "HTML Head element required";
               HeadElement head = HeadElement.as(element);
               ESBJavaScriptInjector.head = head;
      }
         return ESBJavaScriptInjector.head;
         }
}
