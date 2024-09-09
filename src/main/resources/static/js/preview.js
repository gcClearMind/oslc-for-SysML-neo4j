
dojo.addOnLoad(addPreviewMouseOverHandlers);
var hostname ="localhost";

function addPreviewMouseOverHandlers() {
    dojo.query("a").forEach(function(elem) {
        elem.onmouseover = function() { showPreview(elem); };
    });
}

    /* show UI Preview for specified link element */
function showPreview(elem) { // (1)
    var previewURI = elem.getAttribute("href"); // (2)
    if (!previewURI) return;
    dojo.xhrGet({  // (3)
        url: "http://"+hostname+":8181/OSLC/proxy?uri=" + previewURI,
        handleAs:"xml",
        headers: {
        "Accept": "application/x-oslc-compact+xml", // (4)
    },
        load: function(data) {
            try {
                var previewData = parsePreview(data); // (5)
                var html = "<iframe src='" + previewData.uri + "' "; // (6)
                var w = previewData.width ? previewData.width : "30em";
                var h = previewData.height ? previewData.height : "10em";
                html += " style='border:0px; height:" + h + "; width:" + w + "'";
                html += "></iframe>";
                var tip = new dijit.Tooltip({label: html, connectId: elem}); // (7)
                tip.open(elem);
            } catch (e) { // (8)
                var tip = new dijit.Tooltip({label: "Error parsing", connectId: elem});
                tip.open(elem);
             }
        },
        error: function (error) {
            var tip = new dijit.Tooltip({label: "Preview not found", connectId: elem});
            tip.open(elem); // (10)
        }
    });
}

    /* parse OSLC UI Preview XML into JSON structure with uri, h and w */
function parsePreview(xml) { // (1)
    var ret = {};
    var compact = firstChild(firstChild(xml));
    var smallPrev = firstChildNamed(compact,'oslc:smallPreview'); //(2a)
    var largePrev = firstChildNamed(compact, 'oslc:largePreview'); //(2b)
    var preview;
    if(smallPrev != null){
        preview = firstChild(smallPrev);
    } else{
        preview = firstChild(largePrev);
    }
    if (preview) {
        var document = firstChildNamed(preview, 'oslc:document');
        if (document) ret.uri = document.getAttribute('rdf:resource');
        var height = firstChildNamed(preview, 'oslc:hintHeight');
        ret.height = height.textContent;
        var width = firstChildNamed(preview, 'oslc:hintWidth');
        ret.width = width.textContent;
    }
    return ret;
}

function firstChild(e) { // (3)
    for (i=0; i<e.childNodes.length; i++) {
        if (e.childNodes[i].nodeType == Node.ELEMENT_NODE) {
            return e.childNodes[i];
        }
    }
}

function firstChildNamed(e, nodeName) { // (4)
        for (i=0; i<e.childNodes.length; i++) {
            if (e.childNodes[i].nodeType == Node.ELEMENT_NODE
            && e.childNodes[i].nodeName == nodeName) {
                return e.childNodes[i];
        }
    }
}

    /* ==========================================================================
    Code for OSLC Delegated UI
    */

var createDialogURL = "http://" + hostname + ":8080/OSLC4JBugzilla/services/1/changeRequests/creator";
var selectDialogURL = "http:////" + hostname + ":8080/OSLC4JBugzilla/services/1/changeRequests/selector";
var returnURL       = "http:////" + hostname + ":8181/ninacrm/blank.html";

function selectDefect() {
    postMessageProtocol(selectDialogURL);
}

function createDefect() {
    postMessageProtocol(createDialogURL);
}



var iframe;
function postMessageProtocol(dialogURL) {
    // Step 1
    dialogURL += '#oslc-core-postMessage-1.0';

    // Step 2

    var listener = dojo.hitch(this, function (e) {
        var HEADER = "oslc-response:";
        if (e.source == iframe.contentWindow && e.data.indexOf(HEADER) === 0) {
        // Step 4
            window.removeEventListener('message', listener, false);
            destroyFrame(iframe);
            handleMessage(e.data.substr(HEADER.length));

        }
    });
    window.addEventListener('message', listener, false);

    // Step 3
    iframe = document.createElement('iframe');
    iframe.width = 500;
    iframe.height = 375;
    iframe.src = dialogURL;

    // Display the IFrame.
    displayFrame(iframe);
    container.appendChild(iframe);
}



var dialog;
function displayFrame(frame) {
    if (!dialog) dialog = new dijit.Dialog();
    dialog.setContent(frame);
    dialog.show();
}

function destroyFrame(frame) {
    dialog.hide();
}

function handleMessage(message) {
    var json = message.substring(message.indexOf("{"), message.length);
    var results = eval("(" + json + ")");
    var linkname = results["oslc:results"][0]["oslc:label"];
    var linkurl = results["oslc:results"][0]["rdf:resource"];
    addLink(linkname, linkurl);
}

function addLink(linkname, linkurl) {
    dojo.xhrPost( {
        url: "http://"+hostname+":8181/ninacrm/data",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        postData: "linkname=" + linkname + "&linkurl=" + linkurl,
        load: function(data) {
            status("Added link!");
            var li = document.createElement("li");
            li.innerHTML = "<a href='" + linkurl + "' onclick='showPreview()'>" + linkname + "</a>";
            var ul = dojo.byId("linkList").appendChild(li);
            addPreviewMouseOverHandlers();
        },
        error: function (error) {
            status("Error adding link!");
        }
    });
}



function status(msg) {
    document.getElementById("status").innerHTML = msg;
}
