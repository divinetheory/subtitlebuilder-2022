#target "photoshop";

var subtitlebuilder = { error_lines: [], error_stack: [], gui: {}, title: "subtitlebuilder" };

function ProgressWindow(title, progressBarLength) {
    subtitlebuilder.gui.progressWindow = new Window("window", (subtitlebuilder.gui.title = title));
    subtitlebuilder.gui.progressWindow.alignment = "center";
    subtitlebuilder.gui.progressWindow.margins = 10;
    subtitlebuilder.gui.progressWindow.orientation = "column";
    subtitlebuilder.gui.progressWindow.preferredSize = [640, 90];
    subtitlebuilder.gui.progressWindow.spacing = subtitlebuilder.gui.progressWindow.margins;
    subtitlebuilder.gui.progressBar = subtitlebuilder.gui.progressWindow.add("progressbar", undefined, 0, (subtitlebuilder.gui.progressBarLength = progressBarLength));
    subtitlebuilder.gui.progressBar.preferredSize = [620, 35];
    subtitlebuilder.gui.progressText = subtitlebuilder.gui.progressWindow.add("statictext", undefined, "0.00%");
    subtitlebuilder.gui.progressText.preferredSize = subtitlebuilder.gui.progressBar.preferredSize;
    subtitlebuilder.gui.progressWindow.show();
    return;
}

function ErrorReport() {
    for (var i = 0; i < subtitlebuilder.error_stack.length; i++) {
        Window.alert(subtitlebuilder.error_stack[i]);
    }
    if (subtitlebuilder.error_lines.length > 0) {
        Window.alert("Error line(s): " + subtitlebuilder.error_lines);
    }
    return;
}