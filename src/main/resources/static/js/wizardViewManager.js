/**
 * This is a manager for wizard view for demo users.
 * To use it you have to add "/wizard" in the uri after the demo workspace.
 * Example of use: if demo name is SampleWorkspace, you uri should and like
 * "../demo/SampleWorkspace/wizard".
 * @author Felipe Vieira da Cunha Serafim <fvieiradacunha@us.es>
 */
var WizardViewManager = {
    REG_EXP: new RegExp("(.*)\/wizard$"),
    mayApply: function () {
        return !!localStorage && !!localStorage.getItem("isWizard") && localStorage.getItem("isWizard") === "true";
    },
    apply: function () {

        if (this.mayApply()) {

            // Hide css contents with jQuery
            // #appHeader
            ["#editorSidePanel", "#editorHeader", "#editorBottomPanel", ".ui-resizable-handle"].forEach(function (e) {
                $(e).hide();
            });

            setTimeout(function () {
                $("#editorMainPanel").css({
                    width: "100%"
                });

                $("#editorItself").css({
                    height: "100%"
                });

                $("#appBody").css({
                    top: "33px"
                });

                $("#editorWrapper").css({
                    top: "0"
                });
            }, 2000);

        }
    },
    loading: function (flag) {
        
        if (this.mayApply()) {

            var blockerView = $("#appLoaderBlocker");

            if (flag) {
                
                blockerView.css({
                    opacity: "0.8",
                    visibility: "visible"
                });
                
                $("#editor").hide();

            } else {

                blockerView.css({
                    opacity: "0",
                    visibility: "hidden"
                });

            }

        }

    },
    save: function (isWizard) {
        return localStorage.setItem('isWizard', isWizard);
    }

};