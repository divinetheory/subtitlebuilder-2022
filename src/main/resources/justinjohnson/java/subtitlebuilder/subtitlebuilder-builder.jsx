#target "photoshop";
#include "subtitlebuilder-api.jsx";

var file;

try {
    var script = new File($.fileName);
    var directory = script.parent;
    file = new File(directory.absoluteURI + "/" + subtitlebuilder.title + "-configuration.xml");
    file.open("R");
    var xml = new XML(file.read());
    var document = app.open(new File(xml.attribute("templatePath").toString()));
    file.close();
    var artLayers = document.artLayers, artLayer, textLayer;
    for (var i = 0; i < artLayers.length; i++) {
        artLayer = artLayers[i];
        if (artLayer.kind == LayerKind.TEXT) {
            textLayer = artLayer;
            break;
        }
    }
    if (!textLayer) {
        throw "No text layers could be found in the template!";
    }
    var text = textLayer.textItem;
    text.contents = "";
    file = new File(directory.absoluteURI + "/" + subtitlebuilder.title + "-subtitle-cache.xml");
    file.open("R");
    xml = new XML(file.read());
    var scriptName = xml.attribute("title").toString();
    var subtitleList = xml.child("subtitle");
    var progressBarLength = subtitleList.length();
    ProgressWindow(subtitlebuilder.title + " - " + scriptName, progressBarLength);
    var photoshopSaveOptions = new PhotoshopSaveOptions();
    var subtitleIndex = 0;
    var documentBounds = document.width.as("px") * 0.95;
    for (var i = 0; i < subtitleList.length(); i++) {
        var subtitleWordArray = subtitleList[i].split(" ");
        for (var x = 0; x < subtitleWordArray.length; x++) {
            var subtitleWord = subtitleWordArray[x];
            subtitleWord = subtitleWord + " ";
            text.contents += subtitleWord;
            if (textLayer.bounds[2].as("px") >= documentBounds) {
                text.contents = text.contents.replace(new RegExp(subtitleWord + "$"), "");
                document.saveAs(new File(directory.absoluteURI + "/" + scriptName + "/" + subtitleIndex + ".psd"), photoshopSaveOptions);
                text.contents = subtitleWord;
                subtitleIndex++;
            }
        }
        if (text.contents) {
            text.contents = text.contents;
            document.saveAs(new File(directory.absoluteURI + "/" + scriptName + "/" + subtitleIndex + ".psd"), photoshopSaveOptions);
            text.contents = "";
            subtitleIndex++;
        }
        subtitlebuilder.gui.progressBar.value = i + 1;
        subtitlebuilder.gui.progressText.text = ((subtitlebuilder.gui.progressBar.value / subtitlebuilder.gui.progressBarLength) * 100).toFixed(2) + "%";
        subtitlebuilder.gui.progressWindow.update();
    }
} catch (e) {
    subtitlebuilder.error_stack.push(e);
    subtitlebuilder.error_lines.push(e.line);
} finally {
    try {
        file.close();
    } catch (e) {
        subtitlebuilder.error_stack.push(e);
        subtitlebuilder.error_lines.push(e.line);
    } finally {
        try {
            while (app.documents.length > 0) {
                app.activeDocument.close(SaveOptions.DONOTSAVECHANGES);
            }
        } catch (e) {
            subtitlebuilder.error_stack.push(e);
            subtitlebuilder.error_lines.push(e.line);
        } finally {
            try {
                subtitlebuilder.gui.progressWindow.close();
            } catch (e) {
                subtitlebuilder.error_stack.push(e);
                subtitlebuilder.error_lines.push(e.line);
            } finally {
                try {
                    app.executeAction(app.charIDToTypeID("quit"), undefined, DialogModes.NO);
                } catch (e) {
                    subtitlebuilder.error_stack.push(e);
                    subtitlebuilder.error_lines.push(e.line);
                } finally {
                    ErrorReport();
                }
            }
        }
    }
}