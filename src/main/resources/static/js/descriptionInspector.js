var descriptionCheckerTimer;

/**
 * Contiene todos los métodos necesarios para el funcionamiento de
 * IDEAS-Binding. Se ha estructurado el código de la siguiente forma: - en la
 * raíz están los métodos comunes a las distintas funcionalidades de
 * IDEAS-Binding. - los métodos "privados" empiezan con "_", y servirán para
 * recordar al desarrollador que dicho método es llamado a partir de un otro, y
 * nunca forma directa. - dividiéndolo en 4 bloques: - progressBar (menú de
 * "add"): este a su vez se subdivide en los distintos pasos (steps) de la barra
 * de progreso. Hay 3 steps. - removeBindingInspector (menú de "remove"). -
 * loaders: necesario para darle funcionamiento a las llamadas del usuario
 * ("onClick", "onFocus", ...). - ace: simplifican la llamadas los métodos del
 * editor web. - inspectorContent: centraliza el funcionamiento y método de
 * gesitón del contenido del inspector de descripción.
 *
 * @namespace DescriptionInspector
 */
var DescriptionInspector = {
    /**
     * Variables globales del DescriptionInspector
     *
     * @memberof DescriptionInspector
     * @namespace DescriptionInspector.vars
     */
    vars: {
        /**
         * Variable del entorno que indica en qué modo estamos actualmente.
         * Actualmente cuenta con una única variable.
         *
         * @memberof DescriptionInspector.vars
         */
        mode: "editor",
        /**
         * Variable usada para almacenar el contenido del inspector cuando haga
         * falta.
         *
         * @memberof DescriptionInspector.vars
         */
        inspectorContent: "",
        /**
         * Variable que contiene los selectores más usados del
         * DescriptionInspector.
         *
         * @memberof DescriptionInspector.vars
         * @namespace DescriptionInspector.vars.selectors
         */
        selectors: {
            /**
             * @memberof DescriptionInspector.vars.selectors
             * @type String
             */
            inspector: "#editorInspector",
            /**
             * @memberof DescriptionInspector.vars.selectors
             * @type String
             */
            inspectorLoader: "#editorInspectorLoader",
            /**
             * Description inspector content tab.
             * @memberof DescriptionInspector.vars.selectors
             * @type String
             */
            inspectorDescriptionContent: "#inspectorContent",
            /**
             * Description inspector content tab.
             * @memberof DescriptionInspector.vars.selectors
             * @type String
             */
            inspectorDescriptionTab: "#editorTabs > .descriptionInspectorTab",
            /**
             * Model inspector content tab.
             * @memberof DescriptionInspector.vars.selectors
             * @type String
             */
            inspectorModelContent: "#inspectorModelContent",
            /**
             * Model inspector tab.
             * @memberof DescriptionInspector.vars.selectors
             * @type String
             */
            inspectorModelTab: "#editorTabs > .modelInspectorTab",
            /**
             *
             * @type String
             */
            inspectorTabs: "#editorTabs.inspectorTabs"

        },
        /**
         * Variables para el funcionamiento del progressBar. Almacenan la
         * información conseguida en cada step.
         *
         * @memberof DescriptionInspector.vars
         * @namespace DescriptionInspector.vars.progressBar
         */
        progressBar: {
            step1: null,
            step2: null,
            step3: null
        },
        /**
         * Controla si la modificación realizada sobre el editor es realizada
         * desde un método o desde el mismo editor. 0 indica que se realiza
         * sobre el editor 1 indica que se realiza fuera del editor, por ejemplo
         * la llamada al método "DescriptionInspector.modifyBindings".
         *
         * @memberof DescriptionInspector.vars
         */
        modificationFlag: 0

    },
    /**
     * Método de inicio de DescriptionInspector. Busca un fichero descriptor
     * asociado al fichero actualmente abierto. Si existe un fichero descriptor,
     * carga todos los métodos loaders de inicio. En caso contrario, muestra
     * botón de construcción del fichero descriptor con una plantilla.
     *
     * @this window
     * @memberof DescriptionInspector
     * @param {jQuery}
     *            inspectorLoader - Contenedor jQuery del inspector
     */
    loader: function (inspectorLoader) {
        var _this = DescriptionInspector;

        if (_this.existCurrentDescriptionFile()) {
            _this.setInspectorDescriptionContent(inspectorLoader, _this
                .getDescriptionFileContent());
            _this.loaders.bindings(inspectorLoader);
            _this.loaders.allowSelection(inspectorLoader);
            _this.loaders.expandableMenu(inspectorLoader);
            _this.loaders.resizable(inspectorLoader);
            _this.loaders.contextMenu(inspectorLoader);
            _this.loaders.modifyBinding(inspectorLoader);

            _this.manageExpandableMenuVisibility(inspectorLoader);

            $bindingMap.fetchBindings();

            _this.loaders.onEditorAnnotationChange();
            _this.loaders.onEditorSelectionShowCursors();
            _this.loaders.descriptionFormatView();
            // _this.loaders.customEvents();

            _this.showDescriptionTab(); //TODO: activate first available tab
            //			_this.preventBackspaceKeyPress();

        } else {
            // nothing
        }

        if (_this.existCurrentAngularFile()) {
            _this.setInspectorModelContent(
                $(DescriptionInspector.vars.selectors.inspectorModelContent),
                function () {
                    DescriptionInspector.tabs.angularCompileModelInspector();
                }
            );
            _this.loaders.angularFormatView();

        } else {
            _this.loaders.buildExampleFormCreator();
        }
    },
    /**
     * Comprueba si el fichero de descripción existe.
     *
     * @memberof DescriptionInspector
     * @returns {boolean}
     */
    existCurrentDescriptionFile: function () {
        var fileUriWithoutExt = EditorManager.getCurrentUri().replace(/\.[^/.]+$/, "");
        return getNodeByFileUri(fileUriWithoutExt + ".html") ? true : false;
    },
    existCurrentAngularFile: function () {
        var fileUriWithoutExt = EditorManager.getCurrentUri().replace(/\.[^/.]+$/, "");
        return (getNodeByFileUri(fileUriWithoutExt + ".ang") ? true : false);
    },
    existCurrentCtrlFile: function () {
        var fileUriWithoutExt = EditorManager.getCurrentUri().replace(/\.[^/.]+$/, "");
        return getNodeByFileUri(fileUriWithoutExt + ".ctl") ? true : false;
    },
    /**
     * Check if current opened file is a description file.
     *
     * @memberof DescriptionInspector
     * @returns {boolean}
     */
    isCurrentDescriptionFile: function () {
        return DescriptionInspector.getCurrentDescriptionFileUri() === EditorManager.getCurrentUri();
    },
    /**
     * Comprueba si DescriptionInspector se encuentra en modo edición.
     *
     * @memberof DescriptionInspector
     * @returns {boolean}
     */
    isEditorMode: function () {
        return this.vars.mode === "editor";
    },
    /**
     * Habilita el modo de lectura en la visualización del inspector.
     *
     * @memberof DescriptionInspector
     */
    setReaderMode: function () {
        // Oculta botones de "add" y "remove"
        var expandableMenu = $("#expandableMenu");

        // Crear botón de recarga
        var inspectorReloadBtn = expandableMenu.find(".inspectorButtonReload");
        if (inspectorReloadBtn.length == 0) {
            expandableMenu
                .append($('\
				<div id="" class="inspectorButton inspectorButtonReload" style="background:rgba(0,0,0,0.02);"> \
					<div class="btn-group"> \
					  <div class="btn btn-primary dropdown-toggle" \
						   style="color:#428bca;background:#ffffff;border-bottom: 1px solid #EFEFEF;"><span class="glyphicon glyphicon-repeat" style="font-size: 16px;"></span></div> \
					</div> \
				</div> \
			'));
        } else {
            inspectorReloadBtn.show();
        }

        expandableMenu.find(".inspectorButtonReload").show();
        expandableMenu.find(".inspectorButtonAdd").hide();
        expandableMenu.find(".inspectorButtonRemove").hide();

        // Da funcionalidad al botón de recarga
        var _this = DescriptionInspector;
        expandableMenu.find(".inspectorButtonReload").unbind("click").click(
            function () {
                // Reload inspectorContent
                var content = _this.getDescriptionFileContent();
                try {
                    $("#inspectorContent").html(content);
                } catch (err) {
                    console.error(err);
                }
            });

        expandableMenu.find(".inspectorButtonReload").click();

        DescriptionInspector.vars.mode = "reader";
    },
    /**
     * Habilita el modo de edición en la visualización del inspector.
     *
     * @memberof DescriptionInspector
     */
    setEditorMode: function () {
        var expandableMenu = $("#expandableMenu");

        expandableMenu.find(".inspectorButtonReload").hide();
        expandableMenu.find(".inspectorButtonAdd").show();
        expandableMenu.find(".inspectorButtonRemove").show();

        DescriptionInspector.vars.mode = "editor";
    },
    /**
     * Elimina toda la estructura del inspector dejándolo en su aspecto inicial
     * al cargar la aplicación. Se utiliza cuando ya no queda ningún fichero que
     * mostrar.
     *
     * @memberof DescriptionInspector
     */
    empty: function () {
        if (!this.ace.hasOpenTab()) {
            // console.log("Erase inspector content");
            $("#editorInspectorLoader").empty();

            // FormatView
            DescriptionInspector.descriptionFormatView.destroy();
            DescriptionInspector.angularFormatView.destroy();
        }
    },
    /**
     * Muestra u oculta el menú expandible en función de la extensión del
     * fileUri pasado como parámetro. Si se trata de un fichero descriptor,
     * oculta el menú. En caso contrario, lo muestra.
     *
     * @memberof DescriptionInspector
     */
    manageExpandableMenuVisibility: function (inspectorLoader) {
        // console.log("Managing expandable menu visibility");

        var currentUri = EditorManager.getCurrentUri();

        if (ModeManager.calculateExtFromFileUri(currentUri) === "html") {
            DescriptionInspector.setReaderMode();
            // DescriptionInspector.setExpandableMenuVisibility(0);
        } else {
            DescriptionInspector.setEditorMode();
            // DescriptionInspector.setExpandableMenuVisibility(1);
        }

        DescriptionInspector.inspectorContent.resize();
    },
    /**
     * Almacena el contenido del htmlObj pasado por parámetro en el fichero
     * descriptor asociado.
     *
     * @memberof DescriptionInspector
     */
    saveContentToDescriptionFile: function (content) {

        if (content) {
            var fileUri = DescriptionInspector.getCurrentDescriptionFileUri();

            EditorManager.saveFile(fileUri, content, function () {
            });
            // Session content
            if (fileUri in EditorManager.sessionsMap)
                EditorManager.sessionsMap[fileUri].getBaseSession().setValue(
                    content);

            //console.log("Content saved on description file.");
        } else {
            //console.log("Unable to save content into description file");
        }

    },
    /**
     * Almacena en servidor el contenido actual del inspector de descripción. Es
     * necesario comprobar que el fichero descriptor en cuestión se encuentra
     * cerrado antes de intentar guardarlo.
     *
     * @memberof DescriptionInspector
     */
    saveInspectorContentToDescriptionFile: function () {

        var preparedContent = this.prepareDescriptionContentToSave($("#inspectorContent").html());

        if (preparedContent !== null) {
            this.saveContentToDescriptionFile(preparedContent);
        } else {
            console.log("Unable to prepare description file content to save.");
        }

    },
    /**
     * Prepara el contenido a almacenar en el fichero de descripción
     * correspondiente.
     *
     * @memberof DescriptionInspector
     */
    prepareDescriptionContentToSave: function (content) {

        var ret = null;

        if (content.replace) {
            ret = content.replace(/to-remove/g, "ideas-binding")
        }
        return ret;

    },
    /**
     * Calcula la uri del fichero descriptor que hace referencia al fichero
     * actualmenente abierto.
     *
     * @memberof DescriptionInspector
     * @returns {String} Uri del fichero descriptor.
     */
    getCurrentDescriptionFileUri: function () {
        var fileUriWithoutExt = EditorManager.getCurrentUri().replace(/\.[^/.]+$/, "");
        return fileUriWithoutExt + ".html";
    },
    getCurrentModelFileUri: function () {
        var fileUriWithoutExt = EditorManager.getCurrentUri().replace(/\.[^/.]+$/, "");
        return fileUriWithoutExt + ".ang";
    },
    getCurrentModelControllerFileUri: function () {
        var fileUriWithoutExt = EditorManager.getCurrentUri().replace(/\.[^/.]+$/, "");
        return fileUriWithoutExt + ".ctl";
    },
    /**
     * Obtiene el contenido del fichero descriptor en cuestión.
     *
     * @memberof DescriptionInspector
     * @returns {String} Contenido del fichero descriptor
     */
    getDescriptionFileContent: function () {
        var urlReq = 'files/content?fileUri='
            + this.getCurrentDescriptionFileUri(), descriptionData = "";

        $.ajax({
            url: urlReq,
            async: false,
            success: function (content) {
                if (content) {
                    descriptionData = content;
                } else {
                    console.log("No description file content");
                }
            }
        });

        return descriptionData;
    },
    /**
     * Objeto inspectorContent centralizador de operaciones comúnes sobre esta
     * sección de IDEAS. Dicha sección se refiere al contenido del inspector de
     * descripción.
     *
     * @memberof DescriptionInspector
     * @namespace DescriptionInspector.inspectorContent
     */
    inspectorContent: {
        /**
         * Obtiene el contenedor de inspectorContent en formaro jQuery.
         *
         * @memberof DescriptionInspector.inspectorContent
         * @return {jQuery} Contenedor de inspectorContent.
         */
        getSelector: function () {
            return $("#inspectorContent");
        },
        /**
         * Calcula y obtiene la altura del inspectorContent.
         *
         * @memberof DescriptionInspector.inspectorContent
         * @returns {number} Valor de altura.
         */
        height: function () {
            var inspectorHeight = $("#editorInspector").height(),
                tabHeight = $("#editorHeader").height(),
                expandMenuHeight = $("#expandableMenu").css("display") === "none" ? 0 : $("#expandableMenu").height();

            return inspectorHeight - tabHeight - expandMenuHeight;
        },
        /**
         * Calcula y obtiene la anchura del inspectorContent.
         *
         * @memberof DescriptionInspector.inspectorContent
         * @returns {number} Valor de anchura.
         */
        width: function () {
            return $("#editorInspector").width();
        },
        /**
         * Redimensiona el tamaño del inspectorContent teniendo en cuenta el
         * zoom actual.
         *
         * @memberof DescriptionInspector.inspectorContent
         * @returns {boolean} Devuelve true si se han cambiado los valores, y
         *          false si no.
         */
        resize: function () {
            var _this = DescriptionInspector.inspectorContent, $inspector = _this
                .getSelector(), zoom = 1 / $inspector.css("zoom"), width = _this
                    .width(), height = _this.height();

            if (zoom > 0 && width > 0 && height > 0) {
                $inspector.css("width", width * zoom + "px");
                $inspector.css("height", height * zoom + "px");
                return true;
            } else {
                return false;
            }
        },
        /**
         * Almacena en una variable "inspectorContent" el contenido actual del
         * inspector de descripción. Sirve para que tengamos un respaldo del
         * contenido si hemos modificado algo y queremos cancelar el cambio.
         *
         * @memberof DescriptionInspector.inspectorContent
         */
        saveCurrentLocalData: function () {
            DescriptionInspector.vars.inspectorContent = this.getSelector()
                .html();
        },
        /**
         * Carga sobre el contenedor el valor guardado en "saveCurrentLocalData"
         *
         * @memberof DescriptionInspector.inspectorContent
         */
        loadLocalContent: function () {
            if (DescriptionInspector.vars.progressBar.step3 !== null)
                this.getSelector().html(
                    DescriptionInspector.vars.progressBar.step3);
        }

    },
    expandConsole: function (menu, callback) {
        var editor = $("#editorItself");
        var console = $("#editorBottomPanel");
        var expandButton = $("#expandConsole");
        if (menu == 'first') {

            editor.animate({ height: "65%" }, function () {

                expandButton.off('click').click(function () {
                    DescriptionInspector.expandConsole('second');
                });
                fitEditor();
            });
        } else if (menu == 'second') {
            editor.animate({ height: "18%" }, function () {
                expandButton.children('span').toggleClass("glyphicon-chevron-up glyphicon-chevron-down");
                //expandButton.children('span').toggleClass("glyphicon-triangle-bottom");
                expandButton.off('click').click(function () {
                    DescriptionInspector.expandConsole('contract');
                });
                fitEditor();
            });
        } else if (menu == 'contract') {
            editor.animate({ height: "65%" }, function () {
                expandButton.off('click').click(function () {
                    DescriptionInspector.expandConsole('hide');
                });
                fitEditor();
                if (callback) {
                    callback();
                }
            });
        } else if (menu == 'hide') {
            editor.animate({
                height: ($(window).height() - $("#appHeader").height()
                    - $("#appFooter").height() - 52) / $("#editorMainPanel").height() * 100 + "%"
            }, function () {
                expandButton.children('span').toggleClass("glyphicon-chevron-up glyphicon-chevron-down");
                //expandButton.children('span').toggleClass("glyphicon-triangle-bottom");
                expandButton.off('click').click(function () {
                    DescriptionInspector.expandConsole('first');
                });
                fitEditor();
            });
        }
        console.css("height", "100%");

    },
    /**
     * Limpia todo el texto de la consola.
     * @memberof DescriptionInspector
     * */
    clearConsole: function () {
        $(".gcli-row-out").remove();
        $(".gcli-row-in").remove();
    },
    /**
     * Expande el menú desplegable independientemente del tipo que sea (add o
     * remove). Define la animación de despliegue.
     *
     * @memberof DescriptionInspector
     */
    expandInspectorMenu: function (menu) {
        // console.log("expanding inspector menu");

        var expandableMenu = $("#expandableMenu"), contentObjToExpand = null;

        menu === "remove" ? contentObjToExpand = $("#descriptionInspectorHeaderRemove")
            : contentObjToExpand = $("#descriptionInspectorHeaderAdd");

        $(".inspectorButton").fadeOut(100);
        expandableMenu.animate({
            height: "0px"
        }, 100, function () {

            expandableMenu.css("background-color", "#fafafa");

            expandableMenu.animate({
                height: contentObjToExpand.height() + "px"
            }, 100, function () {
                expandableMenu.css("height", "auto");

                contentObjToExpand.fadeIn(100);
                contentObjToExpand.addClass("expanded");

                DescriptionInspector.inspectorContent.resize();

                if (menu == "remove")
                    DescriptionInspector.removeBindingInspector.start();
                else
                    DescriptionInspector.progressBar.start();

            });
        });

        DescriptionInspector.ace.getEditor().setReadOnly(true);

    },
    /**
     * Contrae el menú desplegable independientemente del tipo que sea (add o
     * remove). Define la animación de contracción.
     *
     * @memberof DescriptionInspector
     */
    unexpandInspectorMenu: function (callback) {
        // console.log("unexpanding inspector menu");
        var expandableMenu = $("#expandableMenu");

        expandableMenu.animate({
            height: "0px"
        }, 200, function () {
            expandableMenu.css("background-color", "#fafafa");
            $(".expanded").removeClass("expanded").css("display", "none");

            expandableMenu.animate({
                height: "33px"
            }, 500);
            $(".inspectorButton").fadeIn(200);

            DescriptionInspector.inspectorContent.resize();

            DescriptionInspector.loaders.modifyBinding($("#editorInspector"));

        });

        expandableMenu.find("footer .btn-primary").unbind("click");

        if (typeof callback == "function")
            callback();

        DescriptionInspector.highlightAllBindings();
        DescriptionInspector.ace.getEditor().setReadOnly(false);
    },
    /**
     * Muestra el contenido del inspector de descripción haciendo clic en su
     * pestaña. Además trata de redimensionar el contenedor "inspectorContent"
     * para visualizarlo bien.
     *
     * @memberof DescriptionInspector
     */
    showDescriptionTab: function () {

        $(".descriptionInspectorTab").click();
        DescriptionInspector.inspectorContent.resize();

    },
    // ---------------------------------------------------------------

    /**
     * Obtiene el texto de anotación a partir de un objeto HTML. Si el objeto no
     * cuenta con un atributo data-text, se coge como anotación el texto/valor
     * de dicho objeto HTML.
     *
     * @param {HTMLObject}
     * @memberof DescriptionInspector
     * @returns {HTML} htmlObj - Representación de Binding en HTML.
     */
    getBindingText: function (htmlObj) {
        if (typeof htmlObj.data('text') == 'undefined') {
            return htmlObj.val() == '' ? htmlObj.text() : htmlObj.val();
        } else {
            return htmlObj.data('text');
        }
    },
    /**
     * Selecciona un fragmento de texto en ace a partir de un objeto Binding.
     *
     * @param {Binding}
     * @memberof DescriptionInspector
     */
    selectBindingText: function (binding) {
        // console.log("Selecting binding...", binding);

        var ranges = binding.getRanges(), ace = DescriptionInspector.ace;

        ace.getSelection().clearSelection();
        ace.getEditor().scrollToRow(binding.startRows[0]);

        for (var i = 0; i < ranges.length; i++) {
            ace.getSelection().addRange(ranges[i], false);
        }

        ace.getEditor().setHighlightSelectedWord(true);

    },
    /**
     * Prevent going to the previous page
     * @memberof DescriptionInspector
     */
    preventBackspaceKeyPress: function () {

        var inspectorSelector = $("#editorInspectorLoader");

        inspectorSelector
            .attr("tabindex", "-1")
            .unbind("focus")
            .focus(function () {
                $(this).unbind("keydown").keydown(function (e) {
                    if (e.keyCode == 8) {
                        e.preventDefault();
                    }
                });
            });

    },
    /**
     * Carga sobre el inspector de descripción el contenido pasado por
     * parámetro, cargando antes la plantilla html.
     *
     * @param {jQuery}
     *            loader
     * @param {string}
     *            content - Cadena del texto html.
     * @memberof DescriptionInspector
     */
    setInspectorDescriptionContent: function (loader, content) {
        if (loader && content) {
            loader.append(this.getHTMLTemplate());
            try {
                $("#inspectorContent").html(content);
            } catch (err) {
                console.error(err);
            }
            if (window.setTimeout) {
                setTimeout(function () {
                    DescriptionInspector.inspectorContent.resize();
                }, 100);
            }
        } else {
            console.log("undefined params", loader, content);
        }
    },
    setInspectorModelContent: function (htmlObj, callback) {

        var fileUriForm = DescriptionInspector.getCurrentModelFileUri();

        FileApi.loadFileContents(fileUriForm, function (content) {

            htmlObj.html(content);

            if (DescriptionInspector.existCurrentCtrlFile()) {

                var fileUriCtl = DescriptionInspector.getCurrentModelControllerFileUri();

                FileApi.loadFileContents(fileUriCtl, function (content) {
                    if (content !== "") {
                        htmlObj.append(
                            '<script>' +
                            '  var $scope = angular.element(document.getElementById("editorWrapper")).scope();' +
                            '  $scope.$apply(function () {' + content + '});' +
                            '</script>');
                    }
                    if (callback)
                        callback();
                });

            } else
                if (callback)
                    callback();

        });

    },
    /**
     * Obtiene la plantilla html de lo que se verá en el inspector.
     *
     * @memberof DescriptionInspector
     * @returns {String} Plantilla html
     */
    getHTMLTemplate: function () {
        return '<!-- DescriptionInspector --> \
				<div id="expandableMenu" \
					 style="background:#fafafa;height:auto;min-height:33px;border-bottom:1px solid #EEEEEE;"> \
				    <!-- Botones del Menú --> \
				    <div id="" class="inspectorButton inspectorButtonRemove" style="background:rgba(0,0,0,0.02);"> \
				    	<div class="btn-group"> \
				    	  <div class="btn btn-primary dropdown-toggle" \
				    	       style="color:#428bca;background:#ffffff;border-bottom: 1px solid #EFEFEF;">-</div> \
				    	</div> \
				    </div> \
				    <div id="" class="inspectorButton inspectorButtonAdd" style="background:rgba(0,0,0,0.02);"> \
				    	<div class="btn-group"> \
				    	  <div class="btn btn-primary dropdown-toggle" \
				    	       style="color:#428bca;background:#ffffff;border-bottom: 1px solid #EFEFEF;">+</div> \
				    	</div> \
				    </div> \
					<!-- Menú ADD Binding --> \
				    <section id="descriptionInspectorHeaderAdd" style="display:none;position:relative;"> \
				        <header id="descriptionInspectorHeader"> \
				        	<h4 style="margin-top:0;padding-top:20px;text-align:center;/* display:none; */">Binding adder</h4> \
				            <!-- Progress bar --> \
				            <div class="container" style="width:70%;"> \
				            	<div class="row" style="margin-right:0;"><br /> \
				            		<div class="col-md-12" style="margin:0;padding:0;"> \
				                    	<div class="progress" style="height:10px;"> <!-- ok: <i class="glyphicon glyphicon-ok" style="color:#f5f5f5;"></i> --> \
				                            <div class="one progress-number active"><span style="color:#F5F5F5;">1</span></div> \
				                            <div class="two no-color progress-number">2</div> \
				                            <div class="three no-color progress-number">3</div> \
				                            <!-- Modify progress bar --> \
				                  			<div class="progress-bar" style="width: 0%;background-color:#222;"></div> \
				                		</div> \
				                	</div> \
				            	</div> \
				            </div> \
				        </header> \
				        <!-- Contenido del paso a paso --> \
				        <div class="contentWrapper"> \
				            <p class="progressContent"></p> \
				        </div> \
				        <!-- Botones navegadores --> \
				        <footer style=""> \
				            <a class="btn cancelExpandedMenu" style="color:#428bca;">Cancel</a> \
				            <button class="btn btn-primary proceedAction inactive" style="float:right;">Next</button> \
							<div class="shadowCurvedBottom1" style=""></div> \
				        </footer> \
				    </section> \
				    <!-- Menú REMOVE binding --> \
				    <section id="descriptionInspectorHeaderRemove" style="display:none;"> \
				    	<h4 style="margin-top:0;padding-top:20px;text-align:center;/* display:none; */">Binding remover</h4> \
				        <div class="contentWrapper" style="text-align:center;display:block;"> \
				            <p class="content">Please select a binding on the text below or press "<u>Bulk delete</u>" to remove more than one binding at one time.</p> \
				        </div> \
				        <!-- Botones navegadores --> \
				        <footer style=""> \
				            <a class="btn cancelExpandedMenu" style="color:#428bca;">Cancel</a> \
				            <a class="btn proceedAction" style="float:right;color:#428bca;">Bulk delete</a> \
							<div class="shadowCurvedBottom1" style=""></div> \
				        </footer> \
				    </section> \
				</div> \
				<!-- Description file content (*.html) --> \
				<article id="inspectorContent"></article>';
    },
    /**
     * @namespace DescriptionInspector.ace
     */
    ace: {
        /**
         * Obtiene objeto "Editor" de ace
         *
         * @memberof DescriptionInspector.ace
         * @returns {Editor} Objeto de ace
         */
        getEditor: function () {
            return document.editor;
        },
        /**
         * Obtiene objeto "Session" de ace
         *
         * @memberof DescriptionInspector.ace
         * @returns {Session} Objeto de ace
         */
        getSession: function () {
            return (document.editor) ? this.getEditor().getSession() : null;
        },
        /**
         * Obtiene objeto "Selection" de ace
         *
         * @memberof DescriptionInspector.ace
         * @returns {Selection} Objeto de ace
         */
        getSelection: function () {
            return (document.editor) ? this.getEditor().getSelection() : null;
        },
        /**
         * Comprueba si el editor tiene pestañas abiertas
         *
         * @memberof DescriptionInspector.ace
         * @returns {boolean}
         */
        hasOpenTab: function () {
            return Object.keys(EditorManager.tabsMap).length > 0
        }
    },
    tabs: {
        /**
         * This method creates "DESCRIPTION" inspector tab when you open a new file.
         * @memberof DescriptionInspector.tabs
         */
        buildDescriptionTab: function () {

            var inspectorTabs = $(DescriptionInspector.vars.selectors.inspectorTabs),
                title = "DESCRIPTION";

            if (DescriptionInspector.existCurrentDescriptionFile()) {
                inspectorTabs.append('\
                    <li class="descriptionInspectorTab" style="bottom: 0px; max-width: 100%;"> \
                        <a class="editorTab" style="padding:4px 10px;">' + title + '</a> \
                    </li>');

                var descriptionTab = $(DescriptionInspector.vars.selectors.inspectorDescriptionTab);

                descriptionTab.click(function () {
                    // Hide others tabs content
                    $(".moduleInspectorContent").hide();
                    $(".moduleInspectorTab")
                        .removeClass("active")
                        .find("a")
                        .css({
                            "background": "#fff",
                            "border-bottom": "none"
                        });

                    $(".modelInspectorContent").hide();
                    $(".modelInspectorTab")
                        .removeClass("active")
                        .find("a")
                        .css({
                            "background": "#fff",
                            "border-bottom": "none"
                        });

                    $(".descriptionInspectorContent").show();
                    $(".descriptionInspectorTab")
                        .addClass("active")
                        .find("a")
                        .css("background", "rgba(0,0,0,0.02)");

                });
            }

            return this;

        },
        /**
         * This method creates "FORM" inspector tab when you open a new file.
         * @memberof DescriptionInspector.tabs
         */
        buildFormTab: function () {

            var formatsSessions = EditorManager.sessionsMap[EditorManager.currentUri].getFormatsSessions(),
                currentFormat = EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();

            if ("json" in formatsSessions || "yaml" in formatsSessions) {

                var inspectorTabs = $(DescriptionInspector.vars.selectors.inspectorTabs),
                    title = "FORM";

                // Tab structure
                inspectorTabs.append('\
                    <li class="modelInspectorTab" style="bottom: 0px; max-width: 100%;"> \
                        <a class="editorTab" style="padding:4px 10px;">' + title + '</a> \
                    </li>');

                // Click tab functionality
                $(DescriptionInspector.vars.selectors.inspectorModelTab).click(function () {
                    // Hide others tabs content
                    $(".moduleInspectorContent").hide();
                    $(".moduleInspectorTab")
                        .removeClass("active")
                        .find("a")
                        .css({
                            "background": "#fff",
                            "border-bottom": "none"
                        });

                    $(".descriptionInspectorContent").hide();
                    $(".descriptionInspectorTab")
                        .removeClass("active")
                        .find("a")
                        .css({
                            "background": "#fff",
                            "border-bottom": "none"
                        });

                    $(".modelInspectorContent").show();
                    $("#inspectorModelContent").show().height(
                        $("#editorInspectorLoader").height() - $("#appFooter").height() - $("ul#editorTabs.inspectorTabs").height() - 12
                    );
                    $(".modelInspectorTab")
                        .addClass("active")
                        .find("a")
                        .css("background", "rgba(0,0,0,0.02)");

                });

                // Default structure for tab content container
                if (DescriptionInspector.existCurrentAngularFile()) {
                    $("#editorInspectorLoader .modelInspectorContent")
                        .html('<article id="inspectorModelContent" style="display:none;" />');
                }

                return this;

            }
        },
        activateDefaultTab: function () {

            // Activate description tab by default
            var descriptionTab = $(DescriptionInspector.vars.selectors.inspectorDescriptionTab),
                formInspectorTab = $(DescriptionInspector.vars.selectors.inspectorModelTab);

            // Wait for tab build
            setTimeout(function () {
                descriptionTab.length ? descriptionTab.click() : formInspectorTab.click();
            }, 100);

            return this;

        },
        angularCompileModelInspector: function () {

            $("#compileModel").val(0);
            angular.element($("#compileModel")).triggerHandler('input');

            $("#compileModel").val(1);
            angular.element($("#compileModel")).triggerHandler('input');

            return this;

        },
        angularCompileModelInspectorFormatView: function () {

            $("#compileModelFormatView").val(0);
            angular.element($("#compileModelFormatView")).triggerHandler('input');

            $("#compileModelFormatView").val(1);
            angular.element($("#compileModelFormatView")).triggerHandler('input');

            return this;

        },
        /**
         * Save file by editing it from a non base format.
         * @param {type} currentFormat
         * @returns {undefined}
         */
        saveFileFromOtherFormat: function (currentFormat) {

            var originFormat = currentFormat || EditorManager.sessionsMap[EditorManager.currentUri].getCurrentFormat();
            var baseFormat = EditorManager.sessionsMap[EditorManager.currentUri].getBaseFormat();
            var currentUri = EditorManager.currentUri;

            var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(EditorManager.currentUri));
            var model = ModeManager.getMode(modelId);
            var converterUri = ModeManager.getConverter(modelId);
            if (model.apiVersion >= 2) {
                converterUri = converterUri.replace("$srcSyntaxId", originFormat).replace("$destSyntaxId", baseFormat);
            }
            var content = document.editor.getValue();

            if (originFormat !== baseFormat) {
                CommandApi.callConverter(model, originFormat, baseFormat, currentUri, content, converterUri, function (result) {
                    var convertedContent = result.data;
                    // Save content
                    EditorManager.saveFile(currentUri, convertedContent);
                    // Upload session
                    EditorManager.sessionsMap[currentUri].getBaseSession().setValue(convertedContent);

                    return DescriptionInspector.tabs;
                }
                );
            }
        },
        /**
         * Check if FORM tab content has the 'Create a form file' default button.
         * @returns {Boolean}
         */
        hasFormTabDefaultContent: function () {
            return $("#inspectorModelContent").find(".emptyMsg").text() === "Create a form file";
        }

    },
    /**
     * Update angular model from ace editor.
     */
    editorContentToModel: function () {

        var session = EditorManager.sessionsMap[EditorManager.currentUri],
            formatsSessions = session.getFormatsSessions(),
            currentFormat = session.getCurrentFormat();

        if (!EditorManager.currentDocumentHasProblems() &&
            document.editor && ("json" in formatsSessions || "yaml" in formatsSessions)) {

            // Update modelString model value
            var angScope = angular.element(document.getElementById("editorWrapper")).scope();
            angScope.modelString = document.editor.getValue();

            // Check if editor is on focus
            if ($(":focus").attr("class") === "ace_text-input") {
                angScope.editorContentToModel();
            }

            angScope.$apply();

            if (currentFormat !== "json") {
                DescriptionInspector.tabs.saveFileFromOtherFormat();
            }

        }
    },
    /**
     * Reset model content
     * @returns {undefined}
     */
    resetModel: function () {
        var scope = angular.element(document.getElementById("editorWrapper")).scope();
        scope.$apply(function () {
            scope.editorContentToModel();
        });
    },
    expandableMenu: {
        /**
         * Devuelve el selector del menú expandible.
         *
         * @returns {jQuery}
         */
        getSelector: function () {
            return $("#expandableMenu .expanded");
        },
        /**
         * Comprueba si hay un menú expandido.
         *
         * @returns {boolean}
         */
        isVisible: function () {
            return this.getSelector().length == 1;
        },
        /**
         * Modifica el texto de título del menú expandible.
         *
         * @param {string}
         *            text - Cadena de texto del título.
         * @returns {text}
         */
        setTitle: function (text) {
            return this.getSelector().find("h4").first().html(text);
        },
        /**
         * Modifica el texto de descripción del menú expandible.
         *
         * @param {string}
         *            text - Cadena de texto de la descripción.
         * @returns {boolean}
         */
        setDescription: function (text) {
            return this.getSelector().find(".contentWrapper > p").first().html(
                text);
        }
    },
    // ------------------------------------------------
    // BINDING ADDER - progressBar

    /**
     * @namespace DescriptionInspector.progressBar
     */
    progressBar: {
        /**
         * (deprecated) Elimina el binding a realizarse.
         *
         * @memberof DescriptionInspector.progressBar
         */
        unWrapBindings: function () {
            $(".to-bind").each(function () {
                $(this).replaceWith($(this).html());
            });
        },
        /**
         * Elimina todos las variables de progreso.
         *
         * @memberof DescriptionInspector.progressBar
         */
        eraseData: function () {

            DescriptionInspector.vars.progressBar.step1 = null;
            DescriptionInspector.vars.progressBar.step2 = null;
            DescriptionInspector.vars.progressBar.step3 = null;

        },
        /**
         * Devuelve el objeto step en función del número pasado por parámetro.
         *
         * @param {number}
         *            progressNumber
         * @returns {progressStep}
         */
        getProgressStep: function (progressNumber) {
            var stepObj = {
                '1': this._selectAceText,
                '2': this._selectInspectorText,
                '3': this._selectBindingTextNote
            }
            return (progressNumber in stepObj) ? stepObj[progressNumber] : null;
        },
        /**
         * Obtiene el selector de campo descripción del menú de progressBar.
         *
         * @returns {jQuery}
         */
        getDescriptionSelector: function () {
            return $("#descriptionInspectorHeaderAdd .contentWrapper p.progressContent");
        },
        /**
         * Modifica el título del progressBar por un valor pasado por parámetro.
         *
         * @memberof DescriptionInspector.progressBar
         * @param {string}
         *            Título del progressBar
         */
        setTitle: function (textHTML) {
            return $("#descriptionInspectorHeaderAdd .expanded header h3")
                .html(textHTML);
        },
        /**
         * Modifica la descripción del porgressBar por un valor pasado por
         * parámetro.
         *
         * @memberof DescriptionInspector.progressBar
         * @param {string}
         *            Descripción del progressBar
         */
        setDescription: function (textHTML) {
            return this.getDescriptionSelector().html(textHTML);
        },
        /**
         * Añade referencia de texto del fragmento seleccionado pasado como
         * parámetro.
         *
         * @memberof DescriptionInspector.progressBar
         * @param {string}
         *            fragmentText
         *
         */
        appendSelectedFragment: function (fragmentText) {
            var $progressContent = $("#descriptionInspectorHeaderAdd .contentWrapper p.progressContent"), prevContent = $progressContent
                .html(), labelName = ($progressContent.find("label").length > 0) ? "Desc.: "
                    : "Code: ", escFragText = fragmentText.replace(/"/g,
                        "&quot;").replace(/\t/g, " ");
            var actualContent = prevContent
                + '<div class="container-fluid"><div class="row" style="margin-bottom:5px;">'
                + '<div class="col-xs-2"><label class="selectedFragment" for="">'
                + labelName
                + '</label></div>'
                + '<div class="col-xs-10"><div class="dropdown" style="float:right;">'
                + '<button style="height: 21px;line-height: 0;padding-bottom: 13px;margin-left: 4px;" class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">'
                + '<span>...</span>'
                + '</button>'
                + '<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1" style="overflow-x: auto;width:'
                + $("#descriptionInspectorHeaderAdd .progressContent")
                    .width()
                + 'px;padding: 5px;">'
                + '<li role="presentation"><pre role="menuitem" tabindex="-1" style="pointer-events: none; cursor: default;border:0;background-color:white;color:#6C6C6C;/*word-break: normal;*/word-wrap: normal;font-size:11px;width:100%;">'
                + fragmentText
                + '</pre></li>'
                + '</ul>'
                + '</div>'
                + '<input class="selectedFragment" type="text" '
                + 'value="'
                + escFragText
                + '" style="pointer-events: none; cursor: default;" disabled /></div>'
                + '</div></div>';

            this.setDescription(actualContent);

        },
        /**
         * Comprime el texto del fragmento para que se visualice correctamente
         * en el menú de progressBar.
         *
         * @memberof DescriptionInspector.progressBar
         */
        compressFragmentTexts: function () {

            $("input.selectedFragment")
                .each(
                    function () {
                        var fragText = $(this).val(), fragSize = fragText.length, inputSize = $(
                            this).width() / 8, exceedsView = fragSize > inputSize;

                        if (exceedsView) {
                            $(this).val(
                                fragText.substr(0, inputSize)
                                + "...");
                            $(this).addClass("compressed").attr(
                                "maxlength", inputSize);
                        }
                    });
        },
        /**
         * Redimensiona el tamaño de los visualizadores de fragmento.
         *
         * @memberof DescriptionInspector.progressBar
         */
        resize: function () {
            this.compressFragmentTexts();
        },
        /**
         * Establece el contenido por defecto del progressBar
         *
         * @memberof DescriptionInspector.progressBar
         */
        setDefaultContent: function () {
            var progress = DescriptionInspector.progressBar,
                ace = DescriptionInspector.ace,
                editorInspector = $("#editorInspector");

            $(document).unbind("keyup");
            $(".ace_content").unbind("click");
            $("#inspectorContent").unbind("click");

            editorInspector.find(".expanded footer .btn-primary").addClass(
                "inactive");
            editorInspector.find(".highlighted-fragment").removeClass(
                "highlighted-fragment");

            progress.unWrapBindings();
            progress.getProgressStep("1").setStepDefaultContent();
            progress.eraseData();

            ace.getSession().clearAnnotations();
            ace.getSelection().clearSelection();

            DescriptionInspector.highlightAllBindings();
            this.disableStepFowardBtn();
        },
        /**
         * Da comienzo al funcionamiento del progressBar y almacena el contenido
         * del inspector antes de modificarlo.
         *
         * @memberof DescriptionInspector.progressBar
         */
        start: function () {
            DescriptionInspector.ace.getEditor().exitMultiSelectMode();
            DescriptionInspector.editorSelectedFragmentToRangeSelection();
            DescriptionInspector.inspectorContent.saveCurrentLocalData();
            DescriptionInspector.progressBar.getProgressStep("1").run();

        },
        /**
         * Cancela el progressBar y establece los valores por defectos para la
         * próxima vez que se vaya a ejecutar.
         *
         * @memberof DescriptionInspector.progressBar
         * @param {function}
         *            Función opcional a ejecutarse una vez se cancele el
         *            progressBar
         */
        cancel: function (callback) {
            var progress = this;

            $(".cancelExpandedMenu").unbind("click").click(function () {
                DescriptionInspector.unexpandInspectorMenu();
                progress.setDefaultContent();

                if (typeof callback == "function")
                    callback();
            });
        },
        /**
         * Termina satisfactoriamente la ejecución del progressBar, guardando el
         * contenido final del "inspectorContent".
         *
         * @memberof DescriptionInspector.progressBar
         */
        finish: function () {
            DescriptionInspector.loaders.bindings();
            DescriptionInspector.saveInspectorContentToDescriptionFile();

            $bindingMap.addBinding(DescriptionInspector.vars.progressBar.step1);

            DescriptionInspector.unexpandInspectorMenu();
            DescriptionInspector.progressBar.setDefaultContent();
        },
        disableStepFowardBtn: function () {
            var btnSelector = $("#editorInspector .expanded footer .btn-primary");
            btnSelector.prop("disabled", true)
            return btnSelector.addClass("inactive");
        },
        enableStepFowardBtn: function () {
            var btnSelector = $("#editorInspector .expanded footer .btn-primary");
            btnSelector.prop("disabled", false)
            return btnSelector.removeClass("inactive");
        },
        // --------------------------------------------------
        // STEP 1

        /**
         * @namespace DescriptionInspector.progressBar.step1
         */
        _selectAceText: {
            /**
             * Pone a funcionar el primer paso del progressBar.
             *
             * @memberof DescriptionInspector.progressBar.step1
             */
            run: function () {
                var step1 = this;

                this.setDefaultHeaderText();

                $(".ace_content").bind("click", function (event) {
                    if (step1.isValid()) {
                        // console.log("selected text:",
                        // DescriptionInspector.ace.getEditor().getSelectedText());
                        step1.stepFowardManager(event, $(this));
                    } else {
                        // console.log("nothing selected");
                    }

                    step1.onCancel(event, $(this));
                });

                $(".ace_content").click();

                $(document).bind("keyup", function () {
                    $(".ace_content").click();
                });
            },
            /**
             * Comprueba si el step 1 se encuentra activado.
             *
             * @memberof DescriptionInspector.progressBar.step1
             * @returns {boolean} True si step 1 activo, false en caso contrario
             */
            _isActive: function () {
                return ($(".progress-number.active").length == 1
                    && $(".one.active").length == 1 ? true : false);
            },
            /**
             * Almacena contenido relevante al step 1. En este caso sólo se
             * almacena "DescriptionInspector.vars.progressBar.step1" como
             * variable global.
             *
             * @memberof DescriptionInspector.progressBar.step1
             */
            _saveSelectedContent: function () {
                DescriptionInspector.inspectorContent.resize();
                DescriptionInspector.ace.getEditor().exitMultiSelectMode();

                var $editor = DescriptionInspector.ace.getEditor(), currentSelectionRange = $editor
                    .getSelection().getRange();

                DescriptionInspector.vars.progressBar.step1 = new Binding(
                    $bindingMap.calculateNewId(),
                    $editor.getSelectedText(),
                    currentSelectionRange.start.row,
                    currentSelectionRange.end.row,
                    currentSelectionRange.start.column,
                    currentSelectionRange.end.column);

                // Show selected fragment
                var progress = DescriptionInspector.progressBar;
                progress.setDescription("");
                progress
                    .appendSelectedFragment(DescriptionInspector.vars.progressBar.step1.textNote);
                progress.compressFragmentTexts();

            },
            /**
             * Comprueba si el step 1 es válido para seguir al siguiente step
             *
             * @memberof DescriptionInspector.progressBar.step1
             * @returns {boolean}
             */
            isValid: function () {
                return this._isActive()
                    && DescriptionInspector.ace.getEditor()
                        .getSelectedText().replace(/\s+/g, "") != "";
            },
            /**
             * Establece el contenido por defecto del step 1. Siempre que se
             * cargue este step, se cargará esta configuración.
             *
             * @memberof DescriptionInspector.progressBar.step1
             */
            setStepDefaultContent: function () {
                // Estilo del menú
                $(
                    "#editorInspector .expanded #descriptionInspectorHeader .progress-bar")
                    .css("width", "0");

                $("#editorInspector .expanded .one") // Desactiva numero de
                    // progreso step 1 y
                    // márcalo como exitoso
                    .addClass("active").removeClass("no-color").html(
                        '<span style="color:#F5F5F5;">1</span>');

                $("#editorInspector .expanded .two").addClass("no-color")
                    .removeClass("active").html('2');

                $("#editorInspector .expanded .three").addClass("no-color")
                    .removeClass("active").html('3');

                DescriptionInspector.progressBar.disableStepFowardBtn().text(
                    "Next");

                // Contenido de Texto
                this.setDefaultHeaderText();
            },
            /**
             * Establece el contenido de texto predeterminado del step actual.
             *
             * @memberof DescriptionInspector.progressBar.step1
             */
            setDefaultHeaderText: function () {
                // DescriptionInspector.progressBar.setTitle("Description
                // select");
                DescriptionInspector.progressBar
                    .setDescription("Please select code fragment in the editor on the left and press next");
            },
            /**
             * Cancela el progressBar estando en el step 1.
             *
             * @memberof DescriptionInspector.progressBar.step1
             * @param {Event}
             *            event
             * @param {jQuery}
             *            $obj
             */
            onCancel: function (event, $obj) {
                DescriptionInspector.progressBar.cancel(function () {

                    DescriptionInspector.vars.progressBar.step1 = null;
                    $(".ace_content").unbind("click");
                    $(document).unbind("keyup");

                });
            },
            /**
             * Activa el step 2 del progressBar.
             *
             * @memberof DescriptionInspector.progressBar.step1
             * @param {Event}
             *            event
             * @param {jQuery}
             *            $obj
             */
            stepFowardManager: function (event, $obj) {
                // console.log("Waiting to go to step 2...");
                DescriptionInspector.progressBar.enableStepFowardBtn();
                this._saveSelectedContent();

                $("#editorInspector .expanded footer .btn-primary").unbind(
                    "click").click(
                        function () {
                            var targetStep = DescriptionInspector.progressBar
                                .getProgressStep('2');

                            targetStep.setStepDefaultContent();
                            targetStep.run();

                            // $obj.unbind("click keyup");
                            $("div.ace_content").unbind("click");
                            $(document).unbind("keyup");

                            DescriptionInspector.progressBar
                                .disableStepFowardBtn();
                        });
            }

        },
        // --------------------------------------------------
        // STEP 2

        /**
         * @namespace DescriptionInspector.progressBar.step2
         */
        _selectInspectorText: {
            /**
             * Pone para funcionar el primer paso del progressBar.
             *
             * @memberof DescriptionInspector.progressBar.step2
             */
            run: function () {
                var step2 = this;

                if (step2.isValid()) {
                    step2.stepFowardManager();
                } else {
                    // console.log("No description fragment text selected...");
                }

                this.onCancel();
            },
            /**
             * Comprueba si el step 2 se encuentra activado.
             *
             * @memberof DescriptionInspector.progressBar.step2
             * @returns {boolean}
             */
            _isActive: function () {
                return ($(".progress-number.active").length == 1
                    && $(".two.active").length == 1 ? true : false)
            },
            /**
             * Almacena contenido relavante al step 2. En este caso sólo se
             * alamacena "DescriptionInspector.vars.progressBar.step2".
             *
             * @memberof DescriptionInspector.progressBar.step2
             */
            _saveSelectedContent: function () {

                if (window.getSelection) {
                    var range = window.getSelection().getRangeAt(0), progress = DescriptionInspector.progressBar;

                    progress.setDescription("");
                    progress
                        .appendSelectedFragment(DescriptionInspector.vars.progressBar.step1.textNote);

                    $(".selectedFragment").addClass("prevFragment");

                    progress.appendSelectedFragment(this.getSelectedText());
                    progress.compressFragmentTexts();

                    DescriptionInspector.vars.progressBar.step2 = range;

                    // console.log("DescriptionInspector.vars.progressBar.step2
                    // saved:", DescriptionInspector.vars.progressBar.step2);
                } else if (document.selection && document.selection.createRange) {
                    // TODO: msie
                    console.log("Unable to save step2 content");
                } else {
                    console.log("Unable to save step2 content");
                }

            },
            /**
             * Obtiene el texto seleccionado sobre el inspector.
             *
             * @memberof DescriptionInspector.progressBar.step2
             * @returns {string} Texto seleccionado.
             */
            getSelectedText: function () {
                return window.getSelection ? window.getSelection().toString()
                    : "";
            },
            /**
             * Comprueba si el step 2 es válido para seguir al siguiente step.
             *
             * @memberof DescriptionInspector.progressBar.step2
             * @returns {boolean}
             */
            isValid: function () {
                return DescriptionInspector.vars.progressBar.step1
                    && this._isActive();
            },
            /**
             * Establece el contenido por defecto del step 2. Siempre que se
             * cargue el step se cargará esta configuración.
             *
             * @memberof DescriptionInspector.progressBar.step2
             */
            setStepDefaultContent: function () {
                // console.log("Setting step 2 default content and style");

                var expandedMenu = $("#editorInspector .expanded");

                DescriptionInspector.ace.getSelection().clearSelection();

                // Style
                expandedMenu.find("#descriptionInspectorHeader .progress-bar") // Modifica
                    // barra
                    // de
                    // progreso
                    // a
                    // step
                    // 2
                    .css("width", "51%");

                expandedMenu
                    .find(".one")
                    // Desactiva numero de progreso step 1 y márcalo como
                    // exitoso
                    .removeClass("active")
                    .html(
                        '<span class="glyphicon glyphicon-ok" style="color:#F5F5F5;margin-left:-4px;"></span>');

                expandedMenu.find(".two") // Activa el numero de progreso step
                    // 2
                    .removeClass("no-color").addClass("active").html(
                        '<span style="color:#F5F5F5;">2</span>');

                expandedMenu.find(".three") // Asegura que step 3 está
                    // desactivado
                    .removeClass("active").html('3');

                DescriptionInspector.progressBar.disableStepFowardBtn().text(
                    "Next");

                // Functionality
                this.setDefaultHeaderText();
            },
            /**
             * Establece el contenido de texto predeterminado del step actual.
             *
             * @memberof DescriptionInspector.progressBar.step1
             */
            setDefaultHeaderText: function () {
                // DescriptionInspector.progressBar.setTitle("Inspector
                // select");
                var progress = DescriptionInspector.progressBar;

                progress.setDescription("");
                progress
                    .appendSelectedFragment(DescriptionInspector.vars.progressBar.step1.textNote);
                progress
                    .setDescription(progress.getDescriptionSelector()
                        .html()
                        + "Please select the description fragment on the text below and press next");
                progress.compressFragmentTexts();

                $(".selectedFragment").addClass("prevFragment");
            },
            /**
             * Cancela el progressBar estando en el step 2.
             *
             * @memberof DescriptionInspector.progressBar.step2
             */
            onCancel: function () {
                DescriptionInspector.progressBar.cancel(function () {
                    DescriptionInspector.vars.progressBar.step2 = null;

                    $("#inspectorContent").unbind("click");
                    $(document).unbind("keyup");
                });
            },
            /**
             * Activa el step 3 del progressBar
             *
             * @memberof DescriptionInspector.progressBar.step2
             */
            stepFowardManager: function () {
                var step2 = this;

                function updateDescriptionSelectedFragmentText() {
                    if (step2.getSelectedText() != "") {
                        step2._saveSelectedContent();

                        DescriptionInspector.progressBar.enableStepFowardBtn();

                        $("#editorInspector .expanded footer .btn-primary")
                            .unbind("click")
                            .click(
                                function () {
                                    var step3 = DescriptionInspector.progressBar
                                        .getProgressStep(3);
                                    step3.setStepDefaultContent();
                                    step3.run();

                                    $("#inspectorContent").unbind(
                                        "click");
                                    $(document).unbind("keyup");

                                });
                    } else {
                        // console.log("no text select");
                    }
                }

                $("#inspectorContent").bind("click", function () {
                    updateDescriptionSelectedFragmentText();

                    $(document).unbind("keyup").bind("keyup", function () {
                        updateDescriptionSelectedFragmentText()
                    });
                });
                $("#inspectorContent").click();
            }
        },
        // --------------------------------------------------
        // STEP 3

        /**
         * @namespace DescriptionInspector.progressBar.step3
         */
        _selectBindingTextNote: {
            /**
             * Pone para funcionar el primer paso del progressBar.
             *
             * @memberof DescriptionInspector.progressBar.step3
             */
            run: function () {
                var $this = this;

                $("#editorInspector .expanded footer .btn-primary").unbind(
                    "click").click(function () {
                        if ($this.isValid()) {
                            $this.stepForwardManager();
                        }
                    });

                this.onCancel();
            },
            /**
             * Comprueba si el step 3 se encuentra activado.
             *
             * @memberof DescriptionInspector.progressBar.step3
             * @returns {boolean}
             */
            _isActive: function () {
                return ($(".progress-number.active").length == 1
                    && $(".three.active").length == 1 ? true : false);
            },
            /**
             * Devuelve el html del texto seleccionado en el inspector.
             *
             * @memberof DescriptionInspector.progressBar.step3
             * @returns {string} HTML del texto seleccionado
             */
            getSelectedHtml: function (range) {
                var content = range.cloneContents(), span = document
                    .createElement('span');

                span.appendChild(content);

                return span.innerHTML;
            },
            /**
             * Comprueba si el step 3 es válido para seguir al siguiente step.
             *
             * @memberof DescriptionInspector.progressBar.step3
             * @returns {boolean}
             */
            isValid: function () {
                return DescriptionInspector.vars.progressBar.step1
                    && DescriptionInspector.vars.progressBar.step2
                    && this._isActive();
            },
            /**
             * Cancela el progressBar estando en el step 3
             *
             * @memberof DescriptionInspector.progressBar.step3
             */
            onCancel: function () {
                DescriptionInspector.progressBar.cancel();
            },
            /**
             * Establece el contenido por defecto del step 3. Siempre que se
             * cargue el step se cargará esta configuración.
             *
             * @memberof DescriptionInspector.progressBar.step3
             */
            setStepDefaultContent: function () {
                // console.log("Setting step 3 default content and style");

                // if (window.getSelection) window.getSelection().empty();
                window.getSelection().removeAllRanges(); // works with all
                // browsers
                DescriptionInspector.ace.getSelection().clearSelection();

                // Style
                $(
                    "#editorInspector .expanded #descriptionInspectorHeader .progress-bar")
                    .css("width", "100%");

                $("#editorInspector .expanded .one")
                    .removeClass("active")
                    .html(
                        '<span class="glyphicon glyphicon-ok" style="color:#F5F5F5;margin-left:-4px;"></span>');

                $("#editorInspector .expanded .two")
                    .removeClass("active")
                    .html(
                        '<span class="glyphicon glyphicon-ok" style="color:#F5F5F5;margin-left:-4px;"></span>');

                $("#editorInspector .expanded .three").removeClass("no-color")
                    .addClass("active").html(
                        '<span style="color:#F5F5F5;">3</span>');

                $("#editorInspector .selectedFragment")
                    .addClass("prevFragment");

                $("#editorInspector .expanded footer .btn-primary").addClass(
                    "inactive").text("Done")

                // Functionality
                var progressBar = DescriptionInspector.progressBar, descContent = progressBar
                    .getDescriptionSelector().html();

                progressBar.disableStepFowardBtn();

                descContent += '<hr style="'
                    + 'margin: 15px 0;'
                    + '">'
                    +
                    // RDFa yes/no
                    '<div style="margin-bottom:5px;" id="rdfa-property"><label for="selectBindingRDFa">RDFa:</label><div style="float: right;"><input type="radio" name="rdfa" id="rdfaYes" style="margin: 0 5px 0 15px;" checked=""><label for="rdfaYes" style="margin-right: 6px;">yes</label><input type="radio" name="rdfa" id="rdfaNo" style="margin: 0 5px 0 15px;"><label for="rdfaNo" style="margin-right: 31px;">no</label></div></div>'
                    +
                    // Element
                    '<div class="rdfaContent" style="margin-bottom: 5px; position: relative; display: block;" id="element-property"><label for="selectBindingElement">Element:</label><div style="/* float: right; */position: absolute;right: 0;top: 0;"><input type="radio" name="rdfaElement" id="newElement" style="margin: 0 5px 0 15px;" checked=""><label for="newElement">new</label><input type="radio" name="rdfaElement" id="currentElement" style="margin: 0 5px 0 15px;"><label for="currentElement">current</label></div></div>'
                    +
                    // Type
                    '<div style="margin-left: 25px;margin-bottom: 10px;position: relative;"><label for="selectBindingType" style="/* float: left; */ font-size: 12px;">Type:</label><div style="margin-left: 25px;position: absolute;right: 0;top: 0;"><select id="selectBindingType" class="selectedFragment" style="float: right;"><option value="span">span</option><option value="div">div</option><option value="a">a</option><option value="button">button</option></select></div></div>';

                // Attribute
                descContent += '<div class="rdfaContent" style="position: relative; margin-bottom: 15px; margin-left: 25px; display: block;" id="attribute-property"> <label for="selectBindingAttributeType" style="font-size:12px;float: left;position: relative;">Attribute:</label> <div style="position: relative;right: 0;"> <select id="selectBindingAttributeType" class="selectedFragment" style="margin-bottom: 5px;"> <option value="vocab">vocab</option> <option value="typeof">typeof</option> <option value="property">property</option> <option value="prefix">prefix</option> <option value="resource">resource</option> </select> <input id="attributeTypeValue" style="font-size: 12px;width: 100%;position: relative;" type="text" value=""> </div> </div>';

                // Note
                descContent += '<div id="typeContainer" style="margin-bottom: 5px; position: relative; margin-left: 25px;"><label for="bindingNote" style="font-size: 12px;/* margin-left: 25px; */">Note:</label><input id="bindingNote" class="" style="right: 0;width: 60%;font-size: 12px;position: absolute;" type="text" value=""></div>';

                progressBar.setDescription(descContent);
                progressBar.compressFragmentTexts();

                this.loadRadioButtonClickManager();

            },
            /**
             * Valida el contenido actual y activa o desactiva el botón "Done"
             * en función de los valores.
             */
            validateDoneBtn: function () {
                var ret = false;

                if (this.isValid()) {
                    if (this.isRDFa()) {
                        // is attribute value set?
                        ret = ret || $("#attributeTypeValue").val() != "" ? true
                            : false;
                    } else {
                        ret = true;
                    }
                }

                if (ret) {
                    DescriptionInspector.progressBar.enableStepFowardBtn();
                } else {
                    DescriptionInspector.progressBar.disableStepFowardBtn();
                }

                return ret;
            },
            loadRadioButtonClickManager: function () {

                var _this = this;

                $("input:radio[name=rdfa]").change(function () {
                    var value = $(this).context.id;
                    if (value === "rdfaYes") {
                        if ($(".rdfaContent").is(":hidden")) {
                            $(".rdfaContent").slideDown(function () {
                                $("#newElement").click();
                            });
                        }
                    } else {
                        // simple binding
                        if (!$(".rdfaContent").is(":hidden")) {
                            $(".rdfaContent").slideUp(function () {
                                // Enable all select options
                                $("#selectBindingType").prop("disabled", false);
                                $("#selectBindingType option:disabled").each(function () {
                                    $(this).prop("disabled", false);
                                });
                                $("#selectBindingAttributeType").prop("disabled", false);
                                $("#selectBindingAttributeType option:disabled").each(function () {
                                    $(this).prop("disabled", false);
                                });
                            });
                        }
                    }
                    _this.validateDoneBtn();
                });
                $("input:radio[name=rdfaElement]").change(function () {
                    var value = $(this).context.id;
                    if (value === "newElement") {
                        // new element case

                        // Enable all select options
                        $("#selectBindingType").prop("disabled", false);
                        $("#selectBindingType option:disabled").each(function () {
                            $(this).prop("disabled", false);
                        });
                        $("#selectBindingAttributeType").prop("disabled", false);
                        $("#selectBindingAttributeType option:disabled").each(function () {
                            $(this).prop("disabled", false);
                        });

                        if ($(".rdfaElementContent").is(":hidden")) {
                            // $(".rdfaElementContent").slideDown();
                        }
                    } else {
                        // current element case
                        var $ancestorBinding = _this.getStep2AncestorBinding();

                        if ($ancestorBinding != null) {
                            $("#selectBindingType").val("div"); // check if "div" exists
                            $("#selectBindingType").prop("disabled", true); // disabling select element
                            _this.disableListOfOptions($("#selectBindingAttributeType"), _this.getRDFaDeclaredElements($ancestorBinding));
                        } else {
                            alert("No vocabulary found");
                            $("#newElement").click();
                        }

                        if (!$(".rdfaElementContent").is(":hidden")) {
                            // $(".rdfaElementContent").slideUp();
                        }
                    }
                    _this.validateDoneBtn();
                });

                $("#attributeTypeValue").keyup(function () {
                    _this.validateDoneBtn();
                });

            },
            disableListOfOptions: function ($select, listOfOptions) {

                if ($select.prop("tagName").toLowerCase() === "select") {

                    var disabledItems_i = 0;
                    for (var i = 0; i < listOfOptions.length; i++) {
                        var option_i = "option[value=" + listOfOptions[i] + "]";
                        if ($select.find(option_i) && $select.find(option_i).length > 0) {
                            $select.find(option_i).prop("disabled", true);
                        }
                    }
                    // Disables select element if necessary
                    if (disabledItems_i == $select.find("option").length) {
                        $select.prop("disabled", true);
                    } else {
                        // Auto-select the first available select option
                        $("#selectBindingAttributeType").val($("#selectBindingAttributeType option:enabled").first().val());
                    }
                } else {
                    console.log("Unexpected html select param");
                }

            },
            /**
             * Get which RDFa attributes are declared in the param.
             */
            getRDFaDeclaredElements: function ($HTMLObject) {

                var ret = [];

                // vocab
                var value = $HTMLObject.attr("vocab");
                if (value && value != "") {
                    ret.push("vocab");
                }

                // typeof
                var value = $HTMLObject.attr("typeof");
                if (value && value != "") {
                    ret.push("typeof");
                }

                // property
                var value = $HTMLObject.attr("property");
                if (value && value != "") {
                    ret.push("property");
                }

                // prefix
                var value = $HTMLObject.attr("prefix");
                if (value && value != "") {
                    ret.push("prefix");
                }

                // resource
                var value = $HTMLObject.attr("resource");
                if (value && value != "") {
                    ret.push("resource");
                }

                return ret;

            },
            /**
             * Get the first "div.ideas-binding" ancestor from the selected text
             * on step2.
             */
            getStep2AncestorBinding: function () {

                var binding = DescriptionInspector.vars.progressBar.step2,
                    ancestorElement = binding.commonAncestorContainer.parentElement,
                    ret = null;

                if (ancestorElement != null) {
                    var $ancestor = $(ancestorElement);
                    if (!$ancestor.hasClass("ideas-binding-vocab") || $ancestor.prop("tagName").toLowerCase() != "div") {
                        $ancestor = $ancestor.parents("div.ideas-binding-vocab");
                    }
                    if ($ancestor.length > 0) {
                        ret = $ancestor.first();
                    }
                }

                return ret;

            },
            isRDFa: function () {
                return $("input:radio[name=rdfa]:checked").attr("id") === "rdfaYes";
            },
            isRDFaNewElement: function () {
                return this.isRDFa()
                    && $("input:radio[name=rdfaElement]:checked")
                        .attr("id") === "newElement";
            },
            /**
             * Finaliza el step 3 y guarda los bindings creados.
             *
             * @memberof DescriptionInspector.progressBar.step3
             */
            stepForwardManager: function () {

                var binding = DescriptionInspector.vars.progressBar.step1,
                    contentToBind = this.getSelectedHtml(DescriptionInspector.vars.progressBar.step2),
                    regex = /(<([^>]+)>)/ig,
                    typeOfBinding = $("#selectBindingType").val(),
                    textNote = $("#bindingNote").val();

                if (typeOfBinding != "span" && typeOfBinding != "a" && typeOfBinding != "div" && typeOfBinding != "button") {
                    typeOfBinding = "span";
                }

                // Build binding html element
                var el = document.createElement(typeOfBinding);
                el.innerHTML = contentToBind;

                $(el).attr({
                    "id": "ideasBinding_" + binding.id,
                    "data-start-row": binding.startRows[0],
                    "data-end-row": binding.endRows[0],
                    "data-start-column": binding.startColumns[0],
                    "data-end-column": binding.endColumns[0]
                });

                if (textNote != "")
                    $(el).attr("data-text", textNote);


                // RDFa properties
                if (this.isRDFa()) {
                    var rdfaElementType = $("#selectBindingType"),
                        rdfaElementValue = $("#typeValue"),
                        rdfaAttributeType = $("#selectBindingAttributeType"),
                        rdfaAttributeValue = $("#attributeTypeValue");

                    if (rdfaAttributeType.val() === "vocab") {
                        $(el).addClass("ideas-binding-vocab");
                    } else {
                        $(el).addClass("ideas-binding highlighted-fragment");
                    }

                    if (this.isRDFaNewElement()) {
                        // Create a new binding
                        if (rdfaAttributeValue.val() != "") {
                            $(el).attr(rdfaAttributeType.val(),
                                rdfaAttributeValue.val());
                        }
                        var frag = document.createDocumentFragment(), node, lastNode;
                        $(el).wrap("<div/>");
                        $(el).html($(el).parent().html());
                        while ((node = el.firstChild)) {
                            lastNode = frag.appendChild(node);
                        }

                        DescriptionInspector.vars.progressBar.step2
                            .deleteContents();
                        DescriptionInspector.vars.progressBar.step2
                            .insertNode(frag);

                    } else {
                        // Updated RDFa current binding
                        var $ancestor = this.getStep2AncestorBinding();
                        if (rdfaAttributeValue.val() != "") {
                            $ancestor.attr(rdfaAttributeType.val(),
                                rdfaAttributeValue.val());
                        }
                        if (textNote != "")
                            $ancestor.attr("data-text", textNote);

                    }
                } else {
                    // create simple binding
                    var frag = document.createDocumentFragment(), node, lastNode;
                    $(el).addClass("ideas-binding highlighted-fragment")
                        .wrap("<div/>");
                    $(el).html($(el).parent().html());
                    while ((node = el.firstChild)) {
                        lastNode = frag.appendChild(node);
                    }

                    DescriptionInspector.vars.progressBar.step2
                        .deleteContents();
                    DescriptionInspector.vars.progressBar.step2
                        .insertNode(frag);

                }

                DescriptionInspector.progressBar.finish();
                DescriptionInspector.progressBar.cancel();

            }

        }
        // step 3 ends

    },
    // -------------------------------------------------------
    // BINDING REMOVER

    /**
     * @namespace DescriptionInspector.removeBindingInspector
     */
    removeBindingInspector: {
        /**
         * Inicia el menú de eliminación de binding.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        start: function () {
            this.setDefaultContent();
            this.onCancel();

            DescriptionInspector.scrollToFirstBinding();
        },
        // ------------------------------------------------
        // private

        /**
         * Resalta los bindings y los pasa a modo de eliminación.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        _highlightBindingsToRemove: function (callback) {
            DescriptionInspector.highlightAllBindings();

            $("#inspectorContent .ideas-binding")
                .addClass("to-remove highlighted-fragment")
                .removeClass("ideas-binding");

            // rdfa vocab binding
            $("#inspectorContent .ideas-binding-vocab")
                .addClass("to-remove");

            if (typeof callback == "function")
                callback();
        },
        /**
         * Vuelve al estado inicial, quitando el resaltado.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        _unhighlightBindingsToRemove: function (callback) {
            $("#inspectorContent .to-remove").not(".ideas-binding-vocab")
                .addClass("ideas-binding")
                .removeClass("to-remove highlighted-fragment")
                .unbind("click");

            // rdfa vocab binding
            $("#inspectorContent .to-remove.ideas-binding-vocab")
                .removeClass("to-remove highlighted-fragment");

            DescriptionInspector.loaders.bindings();

            if (typeof callback == "function")
                callback();
        },
        /**
         * Realiza unwrap sobre el objeto jQuery pasado por parámetro.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         * @returns {jQuery} $obj
         */
        unwrap: function ($obj) {
            return $obj.contents().unwrap()
        },
        /**
         * Muestra ventana modal para remover un bindign en concreto. Se
         * muestran los parámetros básicos del binding seleccionado.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         * @param {jQuery}
         *            $obj
         */
        _showModalRemoveBinding: function ($obj) {

            var bind = Binding.getBinding($obj),
                rowAux = parseInt(bind.startRows[0]) + 1,
                ace = DescriptionInspector.ace,
                _this = this;

            showContentAsModal("app/modalWindows/removeBinding", function () {
                _this.unwrap($obj.unbind("click"));

                DescriptionInspector.removeBinding($obj);
                ace.getSelection().clearSelection();
                ace.getSession().clearAnnotations();
                DescriptionInspector.clearAllEditorMarkers();

                hideModal();

                DescriptionInspector.saveInspectorContentToDescriptionFile();
                $bindingMap.removeBinding(bind);
                _this.loadAvailableBindingsToRemoveMessage();
                DescriptionInspector.scrollToFirstBinding();
                DescriptionInspector.highlightAllBindings();
            }, // primary
                function () {
                    hideModal();
                }, // cancel
                function () {
                    hideModal();
                }, // close
                {
                    textNote: bind.textNote,
                    targetText: bind.getTargetText(),
                    rowAux: rowAux
                });

            DescriptionInspector.onEnterPressSubmitModalForm();
        },
        /**
         * Muestra la ventana modal para realizar "Bulk remove" listando todos
         * los bindings posibles para eliminar.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        _showModalBulkRemoveBindings: function () {

            var $listContainer = $("<div>"),
                _this = this;

            // List of bindings to remove
            $("#inspectorContent .to-remove").each(function (index) {
                var bindObj = Binding.getBinding($(this));
                var textNote = $(this).hasClass("ideas-binding-vocab") ? $(this).attr("vocab") : bindObj.textNote;
                $listContainer.append(
                    "<div title='" + bindObj.startRows[0] + ": " + bindObj.getTargetText() + "'>" +
                    "  <input id='bulkremove" + index + "' class='to-bulkremove' name='checkboxBulkRemover' data-id-remover='" + index + "' type='checkbox' />" +
                    "  <label for='bulkremove" + index + "'>" + textNote + "</label>" +
                    "</div>");
            });

            showContenAsModal('app/modalWindows/bulkRemoveBinding', function () {

                $(".modal input.to-bulkremove:checked").each(function () {
                    var $this = this, hasBindingToRemove = true;
                    $("#inspectorContent .to-remove").each(function (index) {
                        if (index == $($this).data("id-remover")) {
                            var bindingObj = Binding.getBinding($(this));
                            $(this).addClass("to-bulkremove");
                            $bindingMap.removeBinding(bindingObj);
                        }
                    });
                });

                _this.unwrap($("#inspectorContent .to-bulkremove").unbind(
                    "click"));

                DescriptionInspector.ace.getSession().clearAnnotations();
                DescriptionInspector.ace.getSelection().clearSelection();
                DescriptionInspector.clearAllEditorMarkers();

                hideModal();

                if ($(".modal input.to-bulkremove:checked").length > 0) {
                    DescriptionInspector
                        .saveInspectorContentToDescriptionFile();
                    _this.loadAvailableBindingsToRemoveMessage();
                    DescriptionInspector.scrollToFirstBinding();
                    DescriptionInspector.highlightAllBindings();
                } else {
                    console.log("No binding selected to remove.");
                }
            }, // primary
                function () {
                    hideModal();
                }, // cancel
                function () {
                    hideModal();
                }, // close
                {
                    hasBindingToRemove: $listContainer.find("div").length > 0,
                    listContainer: $listContainer.html()
                });

            DescriptionInspector.onEnterPressSubmitModalForm();
        },
        // ------------------------------------------------
        // public

        /**
         * Establece el contenido por defecto para el menú de eliminación de
         * bindings.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        setDefaultContent: function () {

            var removeBindingInspector = this;

            removeBindingInspector._highlightBindingsToRemove();

            $("#inspectorContent .to-remove").not(".ideas-binding-vocab").unbind("click").click(function () {
                removeBindingInspector._showModalRemoveBinding($(this));
            });

            $("#descriptionInspectorHeaderRemove .proceedAction").removeClass(
                "inactive").empty().append("Bulk delete").unbind("click")
                .click(function () {
                    removeBindingInspector._showModalBulkRemoveBindings();
                });

            this.loadAvailableBindingsToRemoveMessage();
            DescriptionInspector.ace.getSelection().clearSelection();
        },
        /**
         * Muestra un mensaje u otro en el description si todavía quedan
         * bindings en el inspector.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        loadAvailableBindingsToRemoveMessage: function () {
            DescriptionInspector.expandableMenu.setTitle("Binding remover");

            if (DescriptionInspector.existBinding()) {
                DescriptionInspector.expandableMenu
                    .setDescription('Please select a binding on the text below or press "<u>Bulk delete</u>" to remove more than one binding at one time.');
            } else {
                DescriptionInspector.expandableMenu
                    .setDescription('There is no binding to remove on the text below.');
            }
        },
        /**
         * Cancela el menú de eliminación de bindings.
         *
         * @memberof DescriptionInspector.removeBindingInspector
         */
        onCancel: function () {
            var removeBindingInspector = this;

            $(".cancelExpandedMenu").unbind("click").click(function () {
                DescriptionInspector.unexpandInspectorMenu(function () {
                    removeBindingInspector._unhighlightBindingsToRemove();
                });
            });
        },
    },
    /**
     * @namespace DescriptionInspector.descriptionFormatView
     */
    descriptionFormatView: {
        /**
         * Inicializa todo el ecosistema de descriptionFormatView y su
         * contenido.
         *
         * @memberof DescriptionInspector.descriptionFormatView
         */
        init: function () {

            var _this = this;

            setTimeout(function () {

                if (_this.mayBuild()) {

                    _this.build();
                    _this.formatTab.build();

                    _this.show();

                    _this.customEvents();

                    // CommandApi.echo("Description file content loaded");

                }

            }, 100);

        },
        mayBuild: function () {
            // TODO: comprueba que existe un fichero descriptor relacionado.
            var currentExt = ModeManager.calculateExtFromFileUri(EditorManager.currentUri);
            return this.getHtmlObj().length === 0
                && DescriptionInspector.angularFormatView.getHtmlObj() === 0
                && currentExt !== "ang" && currentExt !== "html"
                && DescriptionInspector.existCurrentDescriptionFile()
                && DescriptionInspector.isEditorMode();
        },
        build: function () {
            this.setDefaultStructure();
            this.setDefaultContent();
            this.binding.onClick();
        },
        /**
         * @memberof DescriptionInspector.descriptionFormatView
         */
        getHtmlObj: function () {
            return $("#ideasBinding-descriptionBoardContent");
        },
        /**
         * @memberof DescriptionInspector.descriptionFormatView
         */
        setDefaultStructure: function () {
            if (this.getHtmlObj().length === 0) {
                $("#editorWrapper")
                    .append(
                        '<div id="ideasBinding-descriptionBoardContent"></div>');
            }
        },
        /**
         * @memberof DescriptionInspector.descriptionFormatView
         */
        setDefaultContent: function () {
            var content = $("#inspectorContent").html();
            if (content) {
                content = content.replace(/(middle-highlighted-fragment)/g, "");
                content = content.replace(/(ideas-binding)/g,
                    "ideas-binding highlighted-fragment");

                return this.getHtmlObj().html(content);
            }
        },
        /**
         * Comprueba si el estamos en la situación de mostrar el
         * DescriptionFullView: el inspector está oculto
         *
         * @memberof DescriptionInspector.descriptionFormatView
         */
        mayShow: function () {
            return this.getHtmlObj().length === 1
                && this.formatTab.getHtmlObj().length === 1;
        },
        /**
         * Activa el descriptionFormatView
         *
         * @memberof DescriptionInspector.descriptionFormatView
         */
        show: function () {

            if (!EditorManager.currentDocumentHasProblems()) {

                if (this.mayShow()) {
                    setTimeout(function () {
                        EditorManager.changeFormat(EditorManager.currentUri, $(
                            "#editorFormats li.formatTab").first().text()
                            .toLowerCase());
                    }, 1);

                    setTimeout(function () {
                        if (!$("#editorInspector").hasClass("hdd")) {
                            $("#editorToggleInspector").click();
                        }
                    }, 100);

                    // TODO: carga el contenido a través de "BindableFormat"
                    // this.getHtmlObj().css("display", "block");
                    this.getHtmlObj().show();
                    this.formatTab.activate();

                } else {
                    this.hide();
                }

            } else {
                CommandApi
                    .echo('<span style="color:red;">Can not show desc main view while errors exist.</span>');
            }

        },
        /**
         * Oculta el DescriptionFullView
         *
         * @memberof DescriptionInspector.descriptionFormatView
         */
        hide: function () {

            var _this = this;

            this.getHtmlObj().fadeOut(function () {
                _this.getHtmlObj().remove();

                if ($("#editorFormats .formatTab.active").text()
                    .toLowerCase() === "desc") {
                    $("#editorFormats .formatTab").first().click();
                    DescriptionInspector.highlightAllBindings();
                }

                setTimeout(function () {
                    if ($("#editorInspector").hasClass("hdd")) {
                        $("#editorToggleInspector").click();
                    }
                    DescriptionInspector.inspectorContent.resize()
                }, 100);

            });

        },
        /**
         * @memberof DescriptionInspector.descriptionFormatView
         */
        destroy: function () {
            this.getHtmlObj().remove();
            this.formatTab.getHtmlObj().remove();
        },
        /**
         * @memberof DescriptionInspector.descriptionFormatView
         */
        customEvents: function () {
            var _this = this, inspector = $("#editorInspector");

            /*
             * inspector.bind("inspectorChangeState", function (e, open) { if
             * (open) { _this.hide(); } });
             */
        },
        /**
         * @namespace DescriptionInspector.descriptionFormatView.binding
         */
        binding: {
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            modifyTextInputBindingValue: function () {
            },
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            transformBinding2TextInput: function (bindingHtmlObj) {
                $(this).replaceWith($('<input />').val($this.text()));
            },
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            onClick: function () {

                $("#ideasBinding-descriptionBoardContent .ideas-binding").unbind("click").click(function () {
                    // transformBinding2TextInput( $(this) );
                }).unbind("dblclick").dblclick(function () {
                    var binding = Binding.getBinding($(this));

                    console.log("Showing Binding Modifier modal window");

                    window.getSelection().removeAllRanges();

                    var _this = this;

                    showContentAsModal("app/modalWindows/bindValue", function () {
                        var value = $("#ideasBindingValueToModify").val();

                        DescriptionInspector.modifyBindings($(_this), value.toString());

                        var content = DescriptionInspector.descriptionFormatView.getHtmlObj().html();
                        var preparedContent = DescriptionInspector.prepareDescriptionContentToSave(content);

                        if (preparedContent !== null) {
                            DescriptionInspector.saveContentToDescriptionFile(preparedContent);
                            $("#inspectorContent")
                                .html(preparedContent);
                            DescriptionInspector.loaders.bindings();
                            CommandApi.echo("New content saved.");
                        }

                        // TODO: update description
                        // inspector

                        hideModal();

                    }, function () {
                        // primary
                        hideModal();
                    }, function () {
                        // cancel
                        hideModal();
                    }, // close,
                        {
                            bindingText: binding.getTargetText(),
                            valueToModifyText: $(this).text()
                        });

                    $("#ideasBindingValueToModify").focus();
                    DescriptionInspector.onEnterPressSubmitModalForm();

                });
            }
        },
        /**
         * @namespace DescriptionInspector.descriptionFormatView.formatTab
         */
        formatTab: {
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            build: function () {
                if (this.getHtmlObj().length === 0) {
                    $("#editorFormats").append(this.getDefaultContent());
                    this.onClick();
                }
            },
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            getHtmlObj: function () {
                return $("#editorFormats li.formatTab.desc");
            },
            /**
             * Obtiene la lista de los demás formatos disponibles en lista de
             * objetos html.
             */
            getOthersHtmlObj: function () {
                return $("#editorFormats li.formatTab").not(".desc");
            },
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            getDefaultContent: function () {
                return '<li class="formatTab desc"><a>DESC</a></li>';
            },
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            activate: function () {
                this.getHtmlObj().addClass("active");
                this.getOthersHtmlObj().removeClass("active");
            },
            /**
             * @memberof DescriptionInspector.descriptionFormatView.formatTab
             */
            onClick: function () {

                var descView = DescriptionInspector.descriptionFormatView,
                    angView = DescriptionInspector.angularFormatView;

                this.getHtmlObj().unbind("click").click(function () {
                    //					if (!$(this).hasClass("active")) {
                    angView.hide();
                    //                        descView.formatTab.activate();
                    descView.init();
                    //					}
                });

                this.getOthersHtmlObj().click(function () {
                    // _this.getHtmlObj().removeClass("active");
                    // if ( !$(this).hasClass("active") ) {
                    descView.hide();
                    // }
                });
                // TODO: si selecciona otro formatTab, desactiva fullView
                // TODO: si abre/cierra fichero
            }
        }
    },
    /**
     * @namespace DescriptionInspector.angularFormatView
     */
    angularFormatView: {
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        init: function () {

            var angularFormatView = this,
                tabs = DescriptionInspector.tabs;

            setTimeout(function () {

                if (angularFormatView.mayBuild()) {
                    angularFormatView.build();

                    DescriptionInspector.setInspectorModelContent(
                        $("#modelBoardContent"),
                        function () {
                            tabs.angularCompileModelInspectorFormatView();
                            angularFormatView.formatTab.build();
                            angularFormatView.show();
                        }
                    );
                }
            }, 250);
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        mayBuild: function () {
            var currentExt = ModeManager.calculateExtFromFileUri(EditorManager.currentUri),
                formatSessions = EditorManager.sessionsMap[EditorManager.currentUri].getFormatsSessions();

            return currentExt !== "ang" && currentExt !== "html"
                && DescriptionInspector.existCurrentAngularFile()
                && "json" in formatSessions || "yaml" in formatSessions;
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        build: function () {
            this.setDefaultStructure();
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        getHtmlObj: function () {
            return $("#modelBoardContent");
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        setDefaultStructure: function () {
            if (this.getHtmlObj().length === 0) {
                $("#editorWrapper").append('<div id="modelBoardContent"/>');
            }
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        mayShow: function () {
            return this.getHtmlObj().length === 1
                && this.formatTab.getHtmlObj().length === 1;
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        show: function () {

            if (!EditorManager.currentDocumentHasProblems()) {

                setTimeout(function () {
                    if (!$("#editorInspector").hasClass("hdd")) {
                        $("#editorToggleInspector").click();
                    }
                }, 100);

                this.getHtmlObj().show();
                this.formatTab.activate();

            } else {
                //				CommandApi.echo('<span style="color:red;">Cannot show form view while editor contains syntax errors.</span>');
                this.hide();
            }

        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        hide: function () {
            this.getHtmlObj().hide();
        },
        /**
         * @memberof DescriptionInspector.angularFormatView
         */
        destroy: function () {
            this.getHtmlObj().remove();
            this.formatTab.getHtmlObj().remove();
        },
        /**
         * @namespace DescriptionInspector.angularFormatView.formatTab
         */
        formatTab: {
            /**
             * @memberof DescriptionInspector.angularFormatView.formatTab
             */
            build: function () {
                if (this.getHtmlObj().length === 0) {
                    $("#editorFormats").append(this.getDefaultContent());
                    this.onClick();
                }
            },
            /**
             * @memberof DescriptionInspector.angularFormatView.formatTab
             */
            getHtmlObj: function () {
                return $("#editorFormats li.formatTab.angular");
            },
            /**
             * Obtiene la lista de los demás formatos disponibles en lista de
             * objetos html.
             */
            getOthersHtmlObj: function () {
                return $("#editorFormats li.formatTab").not(".angular");
            },
            /**
             * @memberof DescriptionInspector.angularFormatView.formatTab
             */
            getDefaultContent: function () {
                return '<li class="formatTab angular"><a>FORM</a></li>';
            },
            /**
             * @memberof DescriptionInspector.angularFormatView.formatTab
             */
            activate: function () {
                this.getHtmlObj().addClass("active");
                this.getOthersHtmlObj().removeClass("active");
            },
            /**
             * @memberof DescriptionInspector.angularFormatView.formatTab
             */
            onClick: function () {

                var descView = DescriptionInspector.descriptionFormatView,
                    formatTab = this;

                this.getHtmlObj().unbind("click").click(function () {
                    if (!EditorManager.currentDocumentHasProblems()) {
                        DescriptionInspector.angularFormatView.show();
                        formatTab.activate();
                        if (!$("#editorInspector").hasClass("hdd")) {
                            $("#editorToggleInspector").click();
                        }
                        //                        descView.hide();
                    } else {
                        CommandApi.echo(
                            '<span style="color:red;">' +
                            '   Can not show form view while errors exist.' +
                            '</span>'
                        );
                    }
                });

                this.getOthersHtmlObj().click(function () {
                    DescriptionInspector.angularFormatView.hide();
                });
            }
        }
    },
    // --------------------------------------------------------
    // onLoad functions

    /**
     * @namespace DescriptionInspector.loaders
     */
    loaders: {
        customEvents: function () {

            var inspector = $("#editorInspector");

            inspector.bind("inspectorChangeState",
                function (e, open) {
                    if (open) {
                        if (document.editor) {
                            DescriptionInspector.highlightAllBindings();
                        }
                        if (DescriptionInspector.angularFormatView.getHtmlObj().is(":visible")) {
                            //								DescriptionInspector.descriptionFormatView.hide();
                            DescriptionInspector.angularFormatView.hide();
                            var baseFormat = EditorManager.sessionsMap[EditorManager.currentUri].getBaseFormat();
                            $("#editorFormats li:contains('" + baseFormat.toUpperCase() + "')").click();
                        }

                    } else {
                        if (document.editor) {
                            DescriptionInspector.unHighlightAllBidings();
                        }
                    }
                });

        },
        /**
         * TODO:
         *
         * @memberof DescriptionInspector.loaders
         */
        descriptionFormatView: function () {
            DescriptionInspector.descriptionFormatView.init();
        },
        angularFormatView: function () {
            DescriptionInspector.angularFormatView.init();
        },
        /**
         * Código a ejecutar cuando se abre un fichero.
         *
         * @memberof DescriptionInspector.loaders
         */
        onEditorOpenFile: function () {
            // FormatView
            DescriptionInspector.descriptionFormatView.destroy();
            DescriptionInspector.angularFormatView.destroy();

            // Highlight description binding
            setTimeout(function () {
                if (DescriptionInspector.existCurrentDescriptionFile())
                    DescriptionInspector.highlightAllBindings();
            }, 100);

            // Model
            DescriptionInspector.resetModel();
        },
        /**
         * Código a ejecutar cuando se cierra un fichero.
         *
         * @memberof DescriptionInspector.loaders
         */
        onEditorCloseFile: function () {
            DescriptionInspector.empty();
        },
        /**
         * Código a ejecutar cuando se inicializa el Ace Editor.
         *
         * @memberof DescriptionInspector.loaders
         */
        onInitAceEditor: function () {
            if (DescriptionInspector.isEditorMode()) {
                var loaders = DescriptionInspector.loaders;
                loaders.onEditorChangeBindingValue();
                loaders.customEvents();
            }
        },
        /**
         * Código a ejecutar cuando se realiza una modificación del contenido
         * del editor.
         *
         * @memberof DescriptionInspector.loaders
         */
        onEditorChangeBindingValue: function () {

            DescriptionInspector.ace
                .getEditor()
                .on(
                    "change",
                    function (e) {

                        if (DescriptionInspector.existCurrentDescriptionFile() &&
                            DescriptionInspector.vars.mode === "editor" &&
                            DescriptionInspector.isFirstEditorFormatSelected() &&
                            DescriptionInspector.vars.modificationFlag === 0) {

                            if (!$("#appGenericModal").is(":visible")) { // TODO:
                                // check
                                // if
                                // the
                                // change
                                // comes
                                // from
                                // a
                                // modal
                                // window.

                                var value = e.data.text,
                                    range = e.data.range,
                                    actionType = e.data.action,
                                    objHtmlBindingRange = DescriptionInspector.getHtmlBindingFromRange(range),
                                    colIndex = 0;

                                if (objHtmlBindingRange
                                    && value !== ""
                                    && value.replace(/\s+/i, " ") !== " "
                                    && value !== ";" && value !== ":"
                                    && value !== "," && value !== "="
                                    && value !== ")" && value !== "("
                                    && value !== "<" && value !== ">") { // FIXME:
                                    // regexp
                                    // it!!

                                    // Modification will change a
                                    // Binding fragment value

                                    if (actionType === "insertText"
                                        && range.start.column <= objHtmlBindingRange.range.end.column) {
                                        colIndex = value.length;
                                    } else if (actionType === "removeText"
                                        && range.start.column < objHtmlBindingRange.range.end.column) {
                                        colIndex = -value.length;
                                    }

                                    objHtmlBindingRange.range.end.column += colIndex;

                                    if (objHtmlBindingRange.range.end.column < objHtmlBindingRange.range.start.column) {
                                        objHtmlBindingRange.range.end.column = objHtmlBindingRange.range.start.column;
                                    }

                                    var endCols = objHtmlBindingRange.htmlBinding
                                        .attr("data-end-column")
                                        .split(",")
                                    endCols[objHtmlBindingRange.rangeIndex] = objHtmlBindingRange.range.end.column

                                    objHtmlBindingRange.htmlBinding
                                        .attr("data-end-column",
                                            endCols.toString());
                                    objHtmlBindingRange.htmlBinding
                                        .text(document.editor.session
                                            .getTextRange(objHtmlBindingRange.range));

                                    DescriptionInspector
                                        .middleHighlightBindingExcept(objHtmlBindingRange.htmlBinding);
                                    DescriptionInspector
                                        .scrollInspectorToBinding(
                                            objHtmlBindingRange.htmlBinding,
                                            false);
                                    Binding
                                        .getBinding(
                                            objHtmlBindingRange.htmlBinding)
                                        .displaceRange(
                                            objHtmlBindingRange.range.start.row,
                                            objHtmlBindingRange.range.end.column,
                                            colIndex);

                                    DescriptionInspector
                                        .modifyBindings(
                                            objHtmlBindingRange.htmlBinding,
                                            document.editor.session
                                                .getTextRange(objHtmlBindingRange.range));

                                    var binding = Binding
                                        .getBinding(objHtmlBindingRange.htmlBinding);

                                    setTimeout(
                                        function () {
                                            DescriptionInspector
                                                .setEditorAnnotation(binding
                                                    .getAnnotations());
                                        }, 1200); // After
                                    // checking
                                    // syntax.

                                } else if (actionType !== "removeLines"
                                    && actionType !== "insertLines") {
                                    // Modification wont change a
                                    // Binding fragment value
                                    if (actionType === "insertText") {
                                        colIndex = value.length;
                                    } else if (actionType === "removeText") {
                                        colIndex = -value.length;
                                    }
                                    DescriptionInspector
                                        .displaceBindingRanges(
                                            range.start.row,
                                            range.end.column
                                            - (value.length + 1),
                                            colIndex);
                                }

                                // Save description file content
                                if (descriptionCheckerTimer
                                    && clearTimeout)
                                    clearTimeout(descriptionCheckerTimer);

                                descriptionCheckerTimer = setTimeout(
                                    function () {
                                        DescriptionInspector
                                            .saveInspectorContentToDescriptionFile();
                                    }, 800);

                            } else {
                                // Modification comes from a modal
                                // window
                                setTimeout(function () {
                                    DescriptionInspector
                                        .highlightAllBindings();
                                }, 1200);
                            }

                        }
                    });

        },
        /**
         * Carga estructura de la tabla del inspector.
         *
         * @memberof DescriptionInspector.loaders
         */
        buildInspectorTabs: function () {

            // html content
            $(DescriptionInspector.vars.selectors.inspectorLoader).append('' +
                // Inspector tabs
                '<div id="editorHeader" style="position:relative">' +
                '  <div id="editorTabsContainer">' +
                '    <ul id="editorTabs" class="nav nav-tabs inspectorTabs" style="width:100%;display:inline-block;">' +
                '    </ul>' +
                '  </div>' +
                '</div>' +
                // Content container
                '<div class="descriptionInspectorContent"></div>' +
                '<div class="modelInspectorContent"></div>' +
                '<div class="moduleInspectorContent"></div>');

            DescriptionInspector.tabs.buildDescriptionTab();
            DescriptionInspector.tabs.buildFormTab();
            DescriptionInspector.tabs.activateDefaultTab();

        },
        /**
         * TODO:
         *
         * @memberof DescriptionInspector.loaders
         */
        onEditorAnnotationChange: function () {
            DescriptionInspector.ace.getSession()
                .on(
                    "changeAnnotation",
                    function () {
                        // console.log("Annotation changed");
                        DescriptionInspector.loaders
                            .onEditorBindingLineClick();
                    });
        },
        /**
         * Muestra los cursores del editor si se selecciona el editor y se
         * permite el cambio de contenido.
         *
         * @memberof DescriptionInspector.loaders
         */
        onEditorSelectionShowCursors: function () {
            DescriptionInspector.ace.getSelection()
                .on(
                    "changeCursor",
                    function () {
                        if (!DescriptionInspector.ace.getEditor()
                            .getReadOnly()) {
                            DescriptionInspector.showEditorCursors();
                        }
                    });

            $("div.ace_gutter-cell").click(function () {
                DescriptionInspector.showEditorCursors();
            });
        },
        /**
         * Realiza un scroll hasta el binding correspondiente haciendo clic
         * sobre la línea resaltada de un Binding.
         *
         * @memberof DescriptionInspector.loaders
         */
        onEditorBindingLineClick: function () {

            function activateBindingFromLine() {

                $(".ace_gutter-cell").not(".ace_ideas_binding").unbind("click");
                $(".ace_ideas_binding")
                    .unbind("click")
                    .click(
                        function () {
                            console
                                .log("Activating Binding from row...");

                            // TODO: activate binding lines from a row.

                            var row = parseInt($(this).text()) - 1, bindingHTMLs = $bindingMap
                                .getHTMLBindingsFromRow(row);

                            if (bindingHTMLs && bindingHTMLs.length > 0) {

                                // DescriptionInspector.hideEditorCursors();
                                var ranges = [], annotations = [];

                                DescriptionInspector
                                    .unHighlightAllBidings();

                                for (var i = 0; i < bindingHTMLs.length; i++) {
                                    var binding = Binding
                                        .getBinding(bindingHTMLs[i]);
                                    if (binding.isValid()) {
                                        ranges = ranges.concat(binding
                                            .getRanges());
                                        annotations = annotations
                                            .concat(binding
                                                .getAnnotations());

                                        if (i == 0) {
                                            DescriptionInspector
                                                .middleHighlightBindingExcept(bindingHTMLs[i]);
                                        } else {
                                            DescriptionInspector
                                                .highlightBinding(bindingHTMLs[i]);
                                        }
                                    }
                                }

                                if (ranges.length > 0) {
                                    DescriptionInspector
                                        .addRangesToEditor(ranges,
                                            annotations);
                                    DescriptionInspector
                                        .scrollInspectorToBinding(bindingHTMLs[0]);
                                } else {
                                    console
                                        .log(
                                            "No binding found to activate from line",
                                            row - 1);
                                }
                            }

                            document.editor.blur();
                            DescriptionInspector.hideEditorCursors();

                        });
            }

            setTimeout(function () {
                activateBindingFromLine();
            }, 150);

            // FIXME: identify which event specifically wants "unbind".
            $(document.editor).unbind("mousewheel").on("mousewheel",
                function () {
                    setTimeout(function () {
                        activateBindingFromLine();
                    }, 150);
                });
        },
        /**
         * Da funcionalidad a todos los botones del menú contextual del
         * inspector.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        _contextActions: function (inspectorLoader) {
            // window.getSelection().empty();
            window.getSelection().removeAllRanges(); // works with all
            // browsers

            this._allBindings(inspectorLoader);
            this._clearBindings(inspectorLoader);
            this._zoomIn(inspectorLoader);
            this._zoomOut(inspectorLoader);
            this._wrappableWord(inspectorLoader);
            this._multiBinding();
        },
        /**
         * Da funciondalidad al botón contextual "All bindings". Seleccionará y
         * resaltará todos los bindings del inspector.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        _allBindings: function (inspectorLoader) {
            $("#contextMenu .all").unbind("click").click(function (e) {
                e.preventDefault();
                DescriptionInspector.highlightAllBindings();
            });
        },
        /**
         * Da funcionalidad al botón contextual "Clear bindings". Quitará el
         * resaltado de todos los bindigns.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        _clearBindings: function (inspectorLoader) {
            $("#contextMenu .clear").unbind("click").click(function (e) {
                e.preventDefault();
                DescriptionInspector.unHighlightAllBidings();
            });
        },
        /**
         * Da funcionalidad al botón contextual "Zoom in". Incrementará el zoom
         * del inspector al doble del existente.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        _zoomIn: function (inspectorLoader) {

            $("#contextMenu .zoomIn").unbind("click").click(function (e) {
                e.preventDefault();
                var zoom = $("#inspectorContent").css("zoom");

                if (zoom <= 1) {
                    $("#inspectorContent").css("zoom", zoom * 2);
                    DescriptionInspector.inspectorContent.resize();
                }
            });
        },
        /**
         * Da funcionalidad al botón contextual "Zoom out". Disminuirá el zoom
         * del contenido del inspector a la mitad del actual.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        _zoomOut: function (inspectorLoader) {

            $("#contextMenu .zoomOut").unbind("click").click(function (e) {
                e.preventDefault();
                var zoom = $("#inspectorContent").css("zoom");

                if (zoom >= 0.25) {
                    $("#inspectorContent").css("zoom", zoom / 2);
                    DescriptionInspector.inspectorContent.resize();
                }
            });
        },
        /**
         * Realiza el ajuste de línea del contenido del inspector en función del
         * valor actual del atributo "white-space".
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        _wrappableWord: function (inspectorLoader) {
            var $inspector = $("#inspectorContent"), wrapText = "Wrap word", unWrapText = "Unwrap word";

            $("#contextMenu .toggleWrapWord")
                .unbind("click")
                .click(
                    function (e) {
                        e.preventDefault();

                        var isWrapped = $inspector.css("white-space") == "normal"
                            || $inspector.css("white-space") == "initial";

                        if (isWrapped) {
                            $(this).find("a").text(wrapText);
                            $inspector.css("white-space", "nowrap");
                        } else {
                            $(this).find("a").text(unWrapText);
                            $inspector.css("white-space", "normal");
                        }
                    });
        },
        /*
         * Crea un MultiBinding a partir de otro Binding activo @memberof
         * DescriptionInspector
         */
        _multiBinding: function () {

            $("#contextMenu .multiBinding")
                .unbind("click")
                .click(
                    function (e) {

                        e.preventDefault();

                        if (DescriptionInspector
                            .isMultiBindingEnabled()) {
                            var activeBindingHtmlObj = $(
                                ".ideas-binding, highlighted-fragment")
                                .not(".middle-highlighted-fragment"), range = document.editor
                                    .getSelectionRange().clone();

                            if (activeBindingHtmlObj.length == 1
                                && !range.isEmpty()) {

                                Binding.addRange(activeBindingHtmlObj,
                                    range);

                                console
                                    .log("New range added to the selected binding.");

                                DescriptionInspector
                                    .saveInspectorContentToDescriptionFile();

                                DescriptionInspector.loaders.bindings();
                                activeBindingHtmlObj.click();

                                var binding = Binding
                                    .getBinding(activeBindingHtmlObj);
                                $bindingMap.addBinding(binding);

                            } else {
                                console
                                    .log("Please, activate one binding for multiBinding.");
                            }
                        } else {
                            console
                                .log("MultiBinding is disabled. Please activate a Binding.");
                        }

                    });
        },
        /**
         * Crea el menú contextual del inspector.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        contextMenu: function (inspectorLoader) {
            var _this = this;
            ctxMenu = $('<div id="contextMenu" style="z-index:20;"><ul class="dropdown-menu" role="context-menu"><li class="zoomIn"><a href="#zoomIn">Zoom In</a></li><li class="zoomOut"><a href="#zoomOut">Zoom Out</a></li><li class="multiBinding separator"><a href="#m">MultiBinding</a></li><li class="all separator"><a>All bindings</a></li><li class="clear"><a>Clear bindings</a></li><li class="toggleWrapWord separator"><a href="#toggleWrapWord">Unwrap word</a></li></ul></div>');

            $("#editorInspectorLoader").append(ctxMenu);
            $("#contextMenu").css("cursor", "pointer");

            $("#inspectorContent").unbind("contextmenu").bind("contextmenu",
                function (e) {
                    $("#contextMenu").css("position", "fixed"); // changing
                    // value to
                    // be able
                    // to view
                    // the
                    // contextmenu

                    e.preventDefault();

                    if (DescriptionInspector.mayEnableMultiBinding()) {
                        DescriptionInspector.enableMultiBinding();
                    } else {
                        DescriptionInspector.disableMultiBinding();
                    }

                    ContextAction.show(e, e.target);

                    _this._contextActions(inspectorLoader);

                    $(document).click(function (e) {
                        if (e.button != 2) {
                            hideMenu();
                            $("#contextMenu").css("position", "absolute"); // default
                            // value
                        }
                    });
                });

        },
        /**
         * Habilitar el seleccionado de texto en el inspector.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        allowSelection: function (inspectorLoader) {
            var inspectorContent = inspectorLoader.find("#inspectorContent");

            inspectorContent.css("-webkit-user-select", "initial");
            inspectorContent.css("-moz-user-select", "text");
        },
        /**
         * Permite que el inspectorContent sea redimensionable.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        resizable: function (inspectorLoader) {
            var _this = DescriptionInspector;
            _this.inspectorContent.resize();

            $(window).resize(function () {
                _this.inspectorContent.resize();
                // _this.progressBar.resize();
            });
        },
        // onClick functions

        /**
         * Cambia el valor de un binding en el fichero en el inspector y en el
         * fichero que se le está haciendo binding.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        modifyBinding: function (inspectorLoader) {

            inspectorLoader
                .find(".ideas-binding")
                .unbind("dblclick")
                .dblclick(
                    function () {
                        var binding = Binding.getBinding($(this));

                        console
                            .log("Showing Binding Modifier modal window");

                        window.getSelection().removeAllRanges();

                        var _this = this;

                        showContentAsModal("app/modalWindows/bindValue",
                            function () {
                                var value = $(
                                    "#ideasBindingValueToModify")
                                    .val();

                                DescriptionInspector
                                    .modifyBindings($(_this),
                                        value.toString());

                                DescriptionInspector
                                    .saveInspectorContentToDescriptionFile();

                                DescriptionInspector
                                    .unHighlightAllBidings();
                                DescriptionInspector
                                    .highlightAllBindings();
                                hideModal();

                                // $(_this).click();

                            }, // primary
                            function () {
                                hideModal();
                            }, // cancel
                            function () {
                                hideModal();
                            }, // close
                            {
                                bindingText: binding.getTargetText(),
                                valueToModifyText: $(this).text()
                            }
                        );

                        $("#ideasBindingValueToModify").focus();
                        DescriptionInspector
                            .onEnterPressSubmitModalForm();

                    });
        },
        buildExampleFormCreator: function () {
            // Config button
            var selectorId = "inspectorModelContent",
                buttonClass = "buildExampleFormCreator",
                buttonText = "Create a form file";

            // html content
            $("#editorInspector .modelInspectorContent")
                .append(
                    '<article id="' + selectorId + '">' +
                    '  <a class="btn ' + buttonClass + ' emptyMsg" style="color: #428bca;">' + buttonText + '</a>' +
                    '</article>'
                );

            var buttonElement = $("#editorInspector a." + buttonClass);
            buttonElement
                .css({
                    // Centering button
                    "margin-left": "-".concat((buttonElement.width() / 2).toString()),
                    "margin-top": "-".concat((buttonElement.height() / 2).toString())
                }).click(function () {
                    // Config form
                    var modelFileUri = DescriptionInspector.getCurrentModelFileUri(),
                        fileName = modelFileUri.replace(/\.[^/.]+$/, "").split("/").pop(),
                        content = '' +
                            '<span style="display: block;font-size: 16px;font-family: serif, Arial;">\n' +
                            '    This is a sample content for a <strong>FORM</strong> file.\n' +
                            '    You can make angular references based on your <strong>JSON</strong> description.\n' +
                            '    All references hang from <strong>$scope.model</strong>.\n' +
                            '    Assuming your <strong>JSON</strong> content is:\n' +
                            '</span>\n' +
                            '<xmp style="background: rgb(254, 254, 254);padding: 20px;border: 1px solid rgb(247, 247, 247);overflow-x: auto;">{\n' +
                            '  "creationConstraints": {\n' +
                            '    "C3": {\n' +
                            '      "slo": {\n' +
                            '        "expression": {"_type": "ParenthesisExpression",\n' +
                            '          "properties": {\n' +
                            '            "exp": {\n' +
                            '              "_type": "LogicalExpression",\n' +
                            '                "properties": {\n' +
                            '                  ...\n' +
                            '                }\n' +
                            '      ...\n' +
                            '}</xmp>\n' +
                            '<span style="display: block;font-size: 16px;font-family: serif, Arial;">Here is a <strong>FORM</strong> example:</span>\n' +
                            '<xmp style="background: rgb(254, 254, 254);padding: 20px;border: 1px solid rgb(247, 247, 247);overflow-x: auto;"><div ng-repeat="key in model.creationConstraints" class="ag-template">\n' +
                            '  <ul>\n' +
                            '    <li>\n' +
                            '      <h4>\n' +
                            '        <input ng-bind="key.slo.expression.properties.exp.properties.exp2.properties.exp2.properties.value" \n' +
                            '               ng-model="key.slo.expression.properties.exp.properties.exp2.properties.exp2.properties.value" /> € / mo\n' +
                            '      </h4>\n' +
                            '    </li>\n' +
                            '    <li>\n' +
                            '      <input ng-model="key.slo.expression.properties.exp.properties.exp1.properties.exp1.properties.exp2.properties.value" /> requests allowed\n' +
                            '    </li>\n' +
                            '    <li>\n' +
                            '      <input ng-model="key.slo.expression.properties.exp.properties.exp1.properties.exp2.properties.exp2.properties.value" /> ms guaranteed response time\n' +
                            '    </li>\n' +
                            '    <li>\n' +
                            '      <a ng-click="removeModel(key.id, $event)">&times;</a>\n' +
                            '    </li>\n' +
                            '  </ul>\n' +
                            '</div></xmp>';

                    // Create new file
                    EditorManager.createNode(modelFileUri, fileName, ".ang", function () {
                        FileApi.saveFileContents(modelFileUri, content, function () {
                            currentSelectedNode.getParent().sortChildren();
                            CommandApi.openFile(modelFileUri, function () { });
                        });
                    });
                }
                );
        },
        /**
         * Da funcionalidad a los botones de para accionar los menúes de "Add" y
         * "Remove".
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        expandableMenu: function (inspectorLoader) {

            inspectorLoader.find(".inspectorButtonAdd").click(function (event) {
                DescriptionInspector.expandInspectorMenu("add");
            });

            inspectorLoader.find(".inspectorButtonRemove").click(
                function (event) {
                    DescriptionInspector.expandInspectorMenu("remove");
                });
        },
        /**
         * Da funcionalidad a los bindings para que cuando se le haga clic
         * seleccione el fragmento de texto asociado en ace.
         *
         * @memberof DescriptionInspector.loaders
         * @param {jQuery}
         *            inspectorLoader - Contenedor del inspector de descripción.
         */
        bindings: function (inspectorLoader) {

            $("#editorInspector .ideas-binding")
                .unbind("click")
                .click(
                    function () {

                        if ($(this).find(".ideas-binding").length == 0
                            && DescriptionInspector.vars.mode == "editor"
                            && DescriptionInspector
                                .isFirstEditorFormatSelected()) {

                            var binding = Binding.getBinding($(this)), annotations = [], ranges = [], ace = DescriptionInspector.ace;

                            if (binding.isValid()) {
                                annotations = binding.getAnnotations();
                                ranges = binding.getRanges();

                                DescriptionInspector
                                    .clearAllEditorMarkers();
                                DescriptionInspector.ace.getEditor()
                                    .navigateTo(ranges[0], -1); // new
                                // make
                                // new
                                // mark
                                // always
                                // visible.
                                for (var i = 0; i < ranges.length; i++) {
                                    DescriptionInspector.ace
                                        .getEditor().multiSelect
                                        .addRange(ranges[i], false);
                                }

                                DescriptionInspector
                                    .middleHighlightBindingExcept($(this));

                                ace.getSession().clearAnnotations();
                                DescriptionInspector
                                    .setEditorAnnotation(annotations);
                                ace.getSelection().clearSelection();

                                DescriptionInspector
                                    .scrollEditorToRange(ranges[0]);

                                DescriptionInspector
                                    .hideEditorCursors();

                            } else {
                                console
                                    .log(
                                        "Couldn\'t highlight a invalid Binding",
                                        $(this))
                            }
                        }
                    });
        }
    },
    // --------------------------------------------------------
    // Utils

    /**
     * Comprueba si el editor tiene una anotación de error.
     *
     * @memberof DescriptionInspector
     * @returns {boolean} Indica si el editor contiene alguna anotación de error
     *          de sintaxis.
     */
    editorHasErrorAnnotation: function () {
        var actualAnnotations = document.editor.session.getAnnotations(), hasErrorAnnotation = false;

        for (var i = 0; i < actualAnnotations.length; i++) {
            if (actualAnnotations[i].type === "error") {
                hasErrorAnnotation = true;
                break;
            }
        }

        return hasErrorAnnotation;
    },
    /**
     * Carga sobre el editor las anotaciones pasadas por parámetro, sólo en el
     * caso de que no exista ninguna anotación de error previamente.
     *
     * @param {array}
     *            annotations - Lista de anotaciones a cargar sobre el editor.
     * @memberof DescriptionInspector
     * @returns {boolean} Indica si se han cargado las modificaciones pasadas
     *          por parámetro.
     */
    setEditorAnnotation: function (annotations) {
        var session = document.editor.session, ret = false;

        if (!this.editorHasErrorAnnotation()) {
            session.clearAnnotations();
            session.setAnnotations(annotations);
            ret = true;
        } else {
            console
                .log("Couldn\'t set annotations because there is an systax error.");
        }

        return ret;
    },
    /**
     * Desplaza los rangos de los bindings asociados a una fila, a partir de una
     * columna determinada.
     *
     * @memberof DescriptionInspector
     */
    displaceBindingRanges: function (row, fromColumn, n) {
        var bindingIds = [], _thisId = this.id;

        if (row in $bindingMap.rowBindingsMap) {

            var bindingIds = $bindingMap.rowBindingsMap[row];

            bindingIds = $.unique($.unique(bindingIds)); // FIXME: double
            // call to reorder.

            for (var i = 0; i < bindingIds.length; i++) {
                var bindingId = bindingIds[i], bindingHtmlObj = DescriptionInspector
                    .getHTMLBinding(bindingId), startColumns = bindingHtmlObj
                        .attr("data-start-column").split(","), endColumns = bindingHtmlObj
                            .attr("data-end-column").split(","), binding = Binding
                                .getBinding(bindingHtmlObj);

                // Find out which positions to update
                var occurrencePositions = [], pos = 0;
                while (pos < binding.startRows.length) {

                    var index = binding.startRows.indexOf(row);

                    if (index !== -1
                        && occurrencePositions.indexOf(index) === -1
                        && parseInt(binding.startColumns[index]) > parseInt(fromColumn)) {

                        occurrencePositions.push(index);

                    }

                    pos++;

                }

                // Displace horizontal positions (columns)
                for (var pos = 0; pos < occurrencePositions.length; pos++) {
                    var startColumns = bindingHtmlObj.attr("data-start-column")
                        .split(","), endColumns = bindingHtmlObj.attr(
                            "data-end-column").split(","), occurrencePos = occurrencePositions[pos];

                    startColumns[occurrencePos] = parseInt(startColumns[occurrencePos])
                        + parseInt(n);
                    endColumns[occurrencePos] = parseInt(endColumns[occurrencePos])
                        + parseInt(n);

                    bindingHtmlObj.attr("data-start-column", startColumns
                        .toString());
                    bindingHtmlObj.attr("data-end-column", endColumns
                        .toString());

                }
            }
        } else {
            //			console.log("No binding found to displace.");
        }
    },
    /**
     * /** Obtiene el html del Binding involucrado al rango que se le pasa por
     * parámetro.
     *
     * @memberof DescriptionInspector
     */
    getHtmlBindingFromRange: function (range) {
        var row = range.start.row;

        if (row in $bindingMap.rowBindingsMap) {
            var bindingIds = $bindingMap.rowBindingsMap[row], ret;

            for (var i = 0; i < bindingIds.length; i++) {
                var htmlBinding = DescriptionInspector
                    .getHTMLBinding(bindingIds[i]), binding = Binding
                        .getBinding(htmlBinding), ranges = binding.getRanges();

                for (var r = 0; r < ranges.length; r++) {
                    var rangeR = ranges[r];
                    if (range.start.row === rangeR.start.row
                        && range.end.row === rangeR.end.row &&
                        range.start.column >= rangeR.start.column
                        && range.start.column <= rangeR.end.column) {

                        ret = {
                            "htmlBinding": htmlBinding,
                            "range": rangeR,
                            "rangeIndex": r
                        };
                        break;
                    }
                }

            }
        } else {
            //			console.log("Row not found on map", row);
        }

        return ret;

    },
    /**
     * Modifica el valor de todos rangos del Binding.
     *
     * @param {jQuery}
     *            htmlObj
     * @param {string}
     *            value
     * @memberof DescriptionInspector
     */
    modifyBindings: function (htmlObj, value) {
        var binding = Binding.getBinding(htmlObj), ranges = binding.getRanges();

        DescriptionInspector.vars.modificationFlag = 1;

        for (var i = 0; i < ranges.length; i++) {

            var range = ranges[i];
            if (document.editor.session.getTextRange(range) !== value) {
                // updating binding column
                var startCol = binding.startColumns[i], finalEndCol = parseInt(startCol)
                    + value.length, diffEndCol = finalEndCol
                        - range.end.column;

                // htmlObj.data("end-column", finalEndCol); // logical change
                var array = htmlObj.attr("data-end-column").split(",");
                array[i] = finalEndCol;
                htmlObj.attr("data-end-column", array.toString());

                DescriptionInspector.ace.getSession().replace(range, "");

                range.end.column = finalEndCol;

                var rngTxt = DescriptionInspector.ace.getSession()
                    .getTextRange(range);
                // console.log("rngTxt", rngTxt);
                DescriptionInspector.ace.getSession().replace(range,
                    value.concat(rngTxt));

                binding.displaceRange(binding.startRows[i],
                    binding.endColumns[i], diffEndCol);

            }

        }

        // changing inspector value
        if (value.length == 0)
            value = "x";
        htmlObj.text(value);

        DescriptionInspector.vars.modificationFlag = 0;

    },
    /**
     * TODO
     *
     * @memberof DescriptionInspector
     */
    getHTMLBinding: function (bindingId) {
        return $("#ideasBinding_" + bindingId);
    },
    /**
     * Deshabilitar menu contextual de MultiBinding
     *
     * @memberof DescriptionInspector
     */
    disableMultiBinding: function () {
        $("#contextMenu .multiBinding a").addClass("disabled");
    },
    /**
     * Habilitar menu contextual de MultiBinding
     *
     * @memberof DescriptionInspector
     */
    enableMultiBinding: function () {
        $("#contextMenu .multiBinding a").removeClass("disabled");
    },
    /**
     * Comprueba si se puede habilitar o no el menú contextual de multibinding.
     *
     * @memberof DescriptionInspector
     */
    mayEnableMultiBinding: function () {
        return DescriptionInspector.vars.mode === "editor"
            && DescriptionInspector.isOneBindingActivated();
    },
    /**
     * Comprueba que existe un solo binding activado.
     *
     * @memberof DescriptionInspector
     */
    isOneBindingActivated: function () {
        return $("#editorInspector .highlighted-fragment").length === 1
            && $("#editorInspector .to-remove").length === 0;
    },
    /**
     * Deshabilitar menu contextual de MultiBinding
     *
     * @memberof DescriptionInspector
     */
    isMultiBindingEnabled: function () {
        return !$("#contextMenu .multiBinding a").hasClass("disabled");
    },
    /**
     * Resalta los rangos pasados por parámetro sobre el editor.
     *
     * @memberof DescriptionInspector
     */
    addRangesToEditor: function (ranges, annotations) {

        if (ranges.length > 0) {
            var ace = DescriptionInspector.ace;

            DescriptionInspector.clearAllEditorMarkers();
            DescriptionInspector.ace.getEditor().navigateTo(ranges[0], -1); // new
            // make
            // new
            // mark
            // always
            // visible.

            for (var i = 0; i < ranges.length; i++) {
                // console.log("adding range", ranges[i]);
                DescriptionInspector.ace.getEditor().multiSelect.addRange(
                    ranges[i], false);
            }

            // DescriptionInspector.middleHighlightBindingExcept($(this));

            DescriptionInspector.setEditorAnnotation(annotations);
            ace.getSelection().clearSelection();

            DescriptionInspector.hideEditorCursors();

        }

    },
    /**
     * Realiza un scroll del editor al rango pasado por parametro.
     *
     * @memberof DescriptionInspector
     */
    scrollEditorToRange: function (range) {
        var editor = DescriptionInspector.ace.getEditor();
        // editor.navigateTo(parseInt(range.start.row),
        // parseInt(range.start.column) );
        editor.scrollToLine(parseInt(range.start.row), true, true, function () {
            // DescriptionInspector.loaders.onEditorBindingLineClick();
        });
    },
    /**
     * Transforma el texto seleccionado en un rango de selección sobre el
     * editor. Sólo se realiza esta operación si no se trata de una selección
     * múltiple.
     *
     * @memberof DescriptionInspector
     */
    editorSelectedFragmentToRangeSelection: function () {

        var selection = DescriptionInspector.ace.getSelection();

        if (!selection.isMultiLine()) {
            var selectedRange = selection.getRange();

            selection.addRange(selectedRange, false);
            selection.clearSelection();
        }

    },
    /**
     * Oculta los cursores del editor.
     *
     * @memberof DescriptionInspector
     */
    hideEditorCursors: function () {
        document.editor.renderer.hideCursor();
    },
    /**
     * Muestran los cursores del editor.
     *
     * @memberof DescriptionInspector
     */
    showEditorCursors: function () {
        document.editor.renderer.showCursor();
    },
    /**
     * Elimina todas las marcas del editor.
     *
     * @memberof DescriptionInspector
     */
    clearAllEditorMarkers: function () {
        var session = DescriptionInspector.ace.getSession(), markerIds = Object
            .keys(session.getMarkers());
        for (var i = 0; i < markerIds.length; i++) {
            session.removeMarker(markerIds[i]);
        }
    },
    /**
     * Cuando el editor ha chequeado el lenguaje
     *
     * @memberof DescriptionInspector
     */
    onEditorCheckedLanguage: function () {
        // console.log("Executing all editor checked language Description
        // functions");

        if (document.editor && DescriptionInspector.existBinding()) {
            /*
             * if ($("#editorInspector").hasClass("hdd")) {
             * $("#editorToggleInspector").click(); }
             */

        }

    },
    /*
     * Comprueba si la primera pestaña de formato del editor está activada.
     * @memberof DescriptionInspector
     */
    isFirstEditorFormatSelected: function () {
        return $("#editorFooter .formatTab").first().hasClass("active");
    },
    /**
     * Muestra los demás binding con menos intensidad que el pasado por
     * parámetro. Sirve para cuando hacemos clic sobre un binding específico y
     * lo queremos resaltar más que los demás.
     *
     * @memberof DescriptionInspector
     */
    middleHighlightBindingExcept: function ($binding) {
        $("#editorInspector .ideas-binding").each(
            function () {
                if (!$(this).is($binding)) {
                    // console.log(">>>>>>> middlehighlighting:", $(this),
                    // $(this).is($binding));

                    $(this).removeClass("highlighted-fragment").addClass(
                        "middle-highlighted-fragment");
                } else {
                    $(this).addClass("highlighted-fragment").removeClass(
                        "middle-highlighted-fragment");
                }
            });
    },
    /**
     * Realiza "submit" de un formulario ubicado en la ventana modal, desde la
     * sección de un campo de dicho formulario.
     *
     * @memberof DescriptionInspector
     */
    onEnterPressSubmitModalForm: function () {
        $("div.modal-content").unbind("keypress").keypress(function (event) {
            if (event.which == 13) {
                event.preventDefault();
                $("#appGenericModal .modal-footer .continue").click();
            }
        });

        $("div.modal-content").focus();
    },
    /**
     * Desplaza el scroll al primer binding del inspector.
     *
     * @memberof DescriptionInspector
     */
    scrollToFirstBinding: function () {
        DescriptionInspector
            .scrollInspectorToBinding($(".highlighted-fragment").first());
    },
    /**
     * Desplaza el scroll al primer binding del inspector.
     *
     * @memberof DescriptionInspector
     */
    scrollInspectorToBinding: function ($htmlObj, click) {
        if (DescriptionInspector.existBinding()) {
            // isVisible
            var topView = $("#inspectorContent").scrollTop(),
                topElement = $htmlObj.position().top,
                value = topView + topElement - 50, time = Math.abs(1500 - value * 0.4);

            if (time > 2000) {
                time = 1500;
            } // if the inspectorContent is too large.

            // $("#inspectorContent").scrollTop(0);
            $("#inspectorContent").stop().animate({
                scrollTop: value
            }, time);

            if ($("#editorInspector .to-remove").length === 0 && click) {
                $htmlObj.click();
            }
        }

    },
    /**
     * Devuelve una lista de objetos jQuery con los bindings actuales en el
     * inspector. Los Bindings disponibles son aquellos que se pueden activar o
     * borrar.
     *
     * @memberof DescriptionInspector
     * @returns {jQuery}
     */
    listOfBinding: function () {
        return $("#editorInspector").find(".ideas-binding, .to-remove");
    },
    /**
     * Devuelve el numero de bindings actuales en el inspector.
     *
     * @memberof DescriptionInspector
     * @returns {Number}
     */
    getNumberOfBindings: function () {
        return DescriptionInspector.listOfBinding().length;
    },
    /**
     * Comprueba si existe al menos un binding en el inspector.
     *
     * @memberof DescriptionInspector
     * @retuns {Boolean}
     */
    existBinding: function () {
        return DescriptionInspector.getNumberOfBindings() > 0;
    },
    /**
     * Resalta el objeto jQuery pasado por parámetro.
     *
     * @memberof DescriptionInspector
     * @param {jQuery}
     *            $obj
     */
    highlightBinding: function ($obj) {
        $obj.addClass("highlighted-fragment").removeClass(
            "middle-highlighted-fragment");
    },
    /**
     * Elimina el resaltado del objeto jQuery pasado por parámetro.
     *
     * @memberof DescriptionInspector
     * @param {jQuery}
     *            $obj
     */
    unHighlightBinding: function ($obj) {
        $obj.removeClass("highlighted-fragment middle-highlighted-fragment");
    },
    /**
     * Resalta todos los bindings.
     *
     * @memberof DescriptionInspector
     */
    highlightAllBindings: function () {

        var annotations = [], ranges;

        if (DescriptionInspector.existBinding()) {

            DescriptionInspector.clearAllEditorMarkers();
            DescriptionInspector.ace.getEditor().navigateTo(
                document.editor.getSelectionRange().start.row, -1);

            $("#editorInspector").find(".ideas-binding, .ideas-binding-vocab, .to-remove").each(function () {

                var binding = Binding.getBinding($(this));

                if (binding.isValid()) {
                    annotations = (annotations.length == 0) ? binding
                        .getAnnotations()
                        : annotations.concat(binding
                            .getAnnotations());

                    if (!$(this).hasClass("ideas-binding-vocab")) {
                        DescriptionInspector.highlightBinding($(this));
                    }

                    if (DescriptionInspector.vars.mode == "editor" && DescriptionInspector.isFirstEditorFormatSelected()) {
                        ranges = binding.getRanges();
                        for (var i = 0; i < ranges.length; i++) {
                            DescriptionInspector.ace.getEditor().multiSelect.addRange(ranges[i], false);
                        }
                    }

                } else {
                    console
                        .log(
                            "Couldn\'t highlight a invalid Binding",
                            $(this))
                }
            });

            if (DescriptionInspector.vars.mode == "editor" && DescriptionInspector.isFirstEditorFormatSelected()) {

                if (ranges.length > 0) {
                    DescriptionInspector.scrollEditorToRange(ranges[ranges.length - 1]);
                }

                DescriptionInspector.setEditorAnnotation(annotations);
                DescriptionInspector.ace.getSelection().clearSelection();

                if ($("#editorInspector .to-remove").length === 0) {
                    DescriptionInspector.loaders.bindings();
                }
                DescriptionInspector.hideEditorCursors();

            }

            document.editor.blur();
            DescriptionInspector.hideEditorCursors();

        }

    },
    /**
     * Eliminar el resaltado de todos los bindings.
     *
     * @memberof DescriptionInspector
     */
    unHighlightAllBidings: function () {
        $("#editorInspector .ideas-binding").each(function () {
            DescriptionInspector.unHighlightBinding($(this));
        });

        DescriptionInspector.clearAllEditorMarkers();
        // TODO: remove the lines below
        DescriptionInspector.ace.getSession().clearAnnotations();
        DescriptionInspector.ace.getSelection().clearSelection();

    },
    /**
     * Elimina el binding del objeto jQuery pasado por parámetro.
     *
     * @memberof DescriptionInspector
     * @param {jQuery}
     *            $obj - Representación de Binding en HTML.
     */
    removeBinding: function ($obj) {
        var text = $obj.text();
        if (text == "") {
            $obj.val();
        }
        $obj.replaceWith(text);
    },
    /**
     * Muestra u oculta el menu expandible del inspector. Para ello, se le pasa
     * un valor como parámetro que indicará si se debe mostrar o no el menú.
     *
     * @memberof DescriptionInspector
     * @param {boolean}
     *            show - Se muestra si es verdadero, en caso contrario, se
     *            oculta.
     */
    setExpandableMenuVisibility: function (show) {
        return show ? $("#expandableMenu").show() : $("#expandableMenu").hide();
    }
};

/**
 * Representa un binding. Un binding debe tener una anotación asociada, fila
 * inicial y final, y columna inicial y final.
 *
 * @namespace Binding
 * @constructor
 * @param {number}
 *            id - Identificador de Binding.
 * @param {string}
 *            textNote - Anotación del binding.
 * @param {array}
 *            startRows - Filas inicial del binding.
 * @param {array}
 *            endRows - Filas final del binding.
 * @param {array}
 *            startColumns - Columnas inicial del binding.
 * @param {array}
 *            endColumns - Columnas final del binding.
 */
function Binding(id, textNote, startRows, endRows, startColumns, endColumns) {
    this.id = parseInt(id);
    this.textNote = textNote;

    var types = ["number", "string"];

    this.startRows = (types.indexOf(typeof startRows) !== -1) ? [startRows]
        : startRows;
    this.endRows = (types.indexOf(typeof endRows) !== -1) ? [endRows]
        : endRows;
    this.startColumns = (types.indexOf(typeof startColumns) !== -1) ? [startColumns]
        : startColumns;
    this.endColumns = (types.indexOf(typeof endColumns) !== -1) ? [endColumns]
        : endColumns;

    // Transform all params to array of integer.
    for (var i = 0; i < this.startRows.length; i++) {
        this.startRows[i] = parseInt(this.startRows[i]);
        this.endRows[i] = parseInt(this.endRows[i]);
        this.startColumns[i] = parseInt(this.startColumns[i]);
        this.endColumns[i] = parseInt(this.endColumns[i]);
    }
}

Binding.clearModel = function () {
    angular.element("#appBody").scope().clearModel();
};

/*
 * Obtiene el ID asociado a un Binding a partir de su objeto HTML. @memberof
 * Binding
 */
Binding.getBindingID = function (htmlObj) {
    return htmlObj.attr("id").replace(/ideasBinding_/i, "");
};

/**
 * Devuelve un objeto Binding a partir de un objeto html. Para ello, se saca
 * toda la información necesaria a partir de los atributos "data" del parámetro.
 *
 * @memberof Binding
 * @param {jQuery}
 *            htmlObj
 */
Binding.getBinding = function (htmlObj) {
    return new Binding(Binding.getBindingID(htmlObj), DescriptionInspector
        .getBindingText(htmlObj), htmlObj.attr("data-start-row").toString()
            .split(","), htmlObj.attr("data-end-row").toString().split(","),
        htmlObj.attr("data-start-column").toString().split(","), htmlObj
            .attr("data-end-column").toString().split(","));
};

/**
 * Comprueba si la instancia contiene todos los valores correctamente
 * inicializados.
 *
 * @memberof Binding
 */
Binding.prototype.isValid = function () {
    return typeof this.textNote != "undefined" && this.textNote != ""
        && typeof this.startRows != "undefined"
        && this.startRows.toString().split(",").length >= 0
        && typeof this.endRows != "undefined"
        && this.endRows.toString().split(",").length >= 0
        && typeof this.startColumns != "undefined"
        && this.startColumns.toString().split(",").length >= 0
        && typeof this.endColumns != "undefined"
        && this.endColumns.toString().split(",").length >= 0;
};

/**
 * Obtiene las anotaciones de ace del objeto instanciado.
 *
 * @memberof Binding
 */
Binding.prototype.getAnnotations = function () {

    var annotations = [];

    for (var i = 0; i < this.startRows.length; i++) {

        var selectionNumLines = Math.abs(this.startRows[i] - this.endRows[i]) + 1;

        if (selectionNumLines == 1) {
            // One-line annotation
            annotations.push({
                row: this.startRows[i],
                type: "binding",
                text: this.textNote
            });
        } else {
            // Multi-line annotation
            for (var j = 0; j < selectionNumLines; j++) {
                annotations.push({
                    row: this.startRows[i] + j,
                    type: "binding",
                    text: this.textNote
                });
            }
        }

    }

    return annotations;
};

/**
 * Obtiene el objeto Range de Ace del objeto instanciado.
 *
 * @memberof Binding
 */
Binding.prototype.getRange = function () {
    var range = document.editor.getSelectionRange().clone();

    range.setStart({
        row: this.startRows[0],
        column: this.startColumns[0]
    });
    range.setEnd({
        row: this.endRows[0],
        column: this.endColumns[0]
    });

    return range;
};

/**
 * Obtiene todos los rangos de un Binding.
 *
 * @memberof Binding
 */
Binding.prototype.getRanges = function () {
    var ranges = [];

    for (var i = 0; i < this.startRows.length; i++) {
        var range = document.editor.getSelectionRange().clone();

        range.setStart({
            row: this.startRows[i],
            column: this.startColumns[i]
        });
        range.setEnd({
            row: this.endRows[i],
            column: this.endColumns[i]
        });

        ranges = (ranges.length === 0) ? [].concat(range) : ranges
            .concat(range);
    }

    return ranges;
};

/**
 * Obtiene los rangos del binding que pertenezcan a una fila inicial dada.
 *
 * @memberof Binding
 */
Binding.prototype.getRowRanges = function (row) {
    var rowRanges = [], ranges = this.getRanges();

    for (var i = 0; i < ranges.length; i++) {
        var range = ranges[i];
        if (parseInt(range.start.row) === parseInt(row)) {
            rowRanges.push(range);
        }
    }

    return rowRanges;
};

/**
 * Obtiene los rangos del binding que pertenezcan a una fila inicial
 * dada omitiendo el del objeto instanciado.
 *
 * @memberof Binding
 */
Binding.prototype.getOmmitedRowRanges = function (row) {
    var distinctBindingIds = [], _thisId = this.id, ranges = [];

    if (row in $bindingMap.rowBindingsMap) {

        var distinctBindingIds = $bindingMap.rowBindingsMap[row]
            .filter(function (elto) {
                return elto != _thisId;
            });

        distinctBindingIds = $.unique(distinctBindingIds);

        for (var i = 0; i < distinctBindingIds.length; i++) {
            var bindingId = distinctBindingIds[i], binding = Binding
                .getBinding(DescriptionInspector
                    .getHTMLBinding(bindingId));

            if (ranges.length > 0) {
                ranges.concat(binding.getRowRanges(row));
            } else {
                ranges = binding.getRowRanges(row);
            }

        }

    }

    return ranges;
},
    /**
     * Obtiene el fragmento de texto de Ace asociado al binding instanciado.
     *
     * @memberof Binding
     */
    Binding.prototype.getTargetText = function () {
        return DescriptionInspector.ace.getSession().getTextRange(
            this.getRange());
    };

/**
 * Comprueba si el rango del editor pertecene al Binding pasado por parámetro
 *
 * @memberof Binding
 */
Binding.prototype.hasRange = function (range) {
    return this.startRows.indexOf(range.start.row) >= 0
        && this.endRows.indexOf(range.end.row) >= 0
        && this.startColumns.indexOf(range.start.column) >= 0
        && this.endColumns.indexOf(range.end.column) >= 0;
};

/**
 * Devuelve las filas que pertenece un Binding
 *
 * @returns {array} Filas que contiene un binding.
 * @memberof Binding
 */
Binding.prototype.getRows = function () {
    var array = [], contRow = 0;

    for (var i = 0; i < this.startRows.length; i++) {
        var startRow = this.startRows[i], endRow = this.endRows[i];

        if (startRow <= endRow) {
            for (var j = startRow; j <= endRow; j++) {
                array.push(startRow);
            }
        } else {
            // Reverse selection
            for (var j = endRow; j <= startRow; j++) {
                array.push(endRow);
            }
        }

    }

    return array;
};

/**
 * Comprueba si el Binding tiene más de una fila asociado.
 *
 * @memberof Binding
 */
Binding.prototype.isMultiBinding = function () {
    return this.startRows.length > 1;
};

/**
 * Añade un nuevo rango a un Binding en HTML.
 *
 * @memberof Binding
 */
Binding.addRange = function (bindingHtmlObj, range) {
    var bAdded = false;

    if (!range.isEmpty()) {

        var binding = Binding.getBinding(bindingHtmlObj), newStartRow = range.start.row, newEndRow = range.end.row, newStartColumn = range.start.column, newEndColumn = range.end.column;

        if (binding.isValid() && !binding.hasRange(range)) {

            // Modifies original Binding
            bindingHtmlObj.attr("data-start-row", bindingHtmlObj.attr(
                "data-start-row").concat("," + newStartRow));
            bindingHtmlObj.attr("data-end-row", bindingHtmlObj.attr(
                "data-end-row").concat("," + newEndRow));
            bindingHtmlObj.attr("data-start-column", bindingHtmlObj.attr(
                "data-start-column").concat("," + newStartColumn));
            bindingHtmlObj.attr("data-end-column", bindingHtmlObj.attr(
                "data-end-column").concat("," + newEndColumn));

            bAdded = true;

        } else {
            console.log("Please, select a different valid range.");
        }

    }

    return bAdded;

};

/**
 * Obtiene el Binding en HTML a partir de una instancia de Binding.
 *
 * @memberof Binding
 */
Binding.prototype.getBindingHtml = function () {
    return DescriptionInspector.getHTMLBinding(this.id); // FIXME: desacoplar
    // de
    // DescriptionInspector
};

/**
 * Desplaza los rangos de una línea n caracteres hacia la derecha, excepto el
 * objeto instanciado.
 *
 * @memberof Binding
 */
Binding.prototype.displaceRange = function (row, thisEndColumn, n) {
    var distinctBindingIds = [], _thisId = this.id;

    if (row in $bindingMap.rowBindingsMap) {

        var distinctBindingIds = $bindingMap.rowBindingsMap[row]
            .filter(function (elto) {
                return elto != _thisId;
            });

        distinctBindingIds = $.unique($.unique(distinctBindingIds)); // FIXME:
        // double
        // call
        // to
        // reorder.

        for (var i = 0; i < distinctBindingIds.length; i++) {
            var bindingId = distinctBindingIds[i], bindingHtmlObj = DescriptionInspector
                .getHTMLBinding(bindingId), startColumns = bindingHtmlObj
                    .attr("data-start-column").split(","), endColumns = bindingHtmlObj
                        .attr("data-end-column").split(","), binding = Binding
                            .getBinding(bindingHtmlObj);

            // Find out which positions to update
            var occurrencePositions = [], pos = 0;
            while (pos < binding.startRows.length) {

                var index = binding.startRows.indexOf(row);

                if (index !== -1
                    && occurrencePositions.indexOf(index) === -1
                    && parseInt(binding.startColumns[index]) > parseInt(thisEndColumn)) {

                    occurrencePositions.push(index);

                }

                pos++;

            }

            // Displace horizontal positions (columns)
            for (var pos = 0; pos < occurrencePositions.length; pos++) {
                var startColumns = bindingHtmlObj.attr("data-start-column")
                    .split(","), endColumns = bindingHtmlObj.attr(
                        "data-end-column").split(","), occurrencePos = occurrencePositions[pos];

                startColumns[occurrencePos] = parseInt(startColumns[occurrencePos])
                    + parseInt(n);
                endColumns[occurrencePos] = parseInt(endColumns[occurrencePos])
                    + parseInt(n);

                bindingHtmlObj.attr("data-start-column", startColumns
                    .toString());
                bindingHtmlObj.attr("data-end-column", endColumns.toString());

            }
        }
    }
};

$bindingMap = new BindingMap();

/**
 * Objeto que contiene la información sobre todos los Bindings actuales.
 *
 * @namespace BindingMap
 */
function BindingMap() {
    this.rowBindingsMap = {};
    this.bindingRowsMap = {};
}

/**
 * Obtiene la instancia global actual del objeto BindingMap.
 *
 * @memberof BindingMap
 */
BindingMap.getInstance = function () {
    return $bindingMap;
};

/**
 * Devuelve la estructura estándar para todos los items de bindingRowsMap
 *
 * @memberof BindingMap
 */
BindingMap.getBindingRowsStructure = function () {
    return {
        "filas": []
    };
};

/**
 * Obtiene el objeto html equivalente al objeto Binding.
 *
 * @param {Binding}
 * @returns {array}
 * @memberof Binding
 */
BindingMap.prototype.getHTMLBinding = function (row) {
    return $("#ideasBinding_" + this.rowBindingsMap[row]);
};

/**
 * Calcula un ID no existente para un Binding que se quiera crear.
 *
 * @returns {Number} Nuevo id disponible para creación de un Binding.
 * @memberof BindingMap
 */
BindingMap.prototype.calculateNewId = function () {
    var lastId = Object.keys(this.bindingRowsMap).pop();
    return lastId ? parseInt(lastId) + 1 : 1;
};

/**
 * Comprueba si el id del Binding pasado por parámetro existe dentro de
 * BindingMap
 *
 * @param {Binding}
 *            bindingObj
 * @returns {boolean} Si existe o no el Binding.
 * @memberof BindingMap
 */
BindingMap.prototype.existBinding = function (bindingObj) {
    return this.bindingRowsMap[bindingObj.id] !== undefined;
};

/**
 * Borra todos los datos del mapa de bindings.
 *
 * @memberof BindingMap
 */
BindingMap.prototype.empty = function () {
    if ($bindingMap && Object.keys($bindingMap).length > 0) {
        delete $bindingMap.rowBindingsMap;
        delete $bindingMap.bindingRowsMap;
        $bindingMap = new BindingMap();
    }
};

/**
 * Carga sobre el objeto el mapeo de todos los Bindings disponibles en el
 * sistema.
 *
 * @memberof BindingMap
 */
BindingMap.prototype.fetchBindings = function () {

    if (DescriptionInspector.vars.mode === "editor") {

        $bindingMap.empty();

        var b = false;

        $("#editorInspector").find(".ideas-binding, .to-remove").each(
            function () {
                // TODO: si no se encuentra dentro del inicializado, cargalo
                // con el ID del objeto
                if (($(this).attr("id") && $(this).attr("id").search(
                    /ideasBinding_/i) === -1)
                    || typeof ($(this).attr("id")) === "undefined") {
                    // Create an ID for Binding
                    var newBindingId = $bindingMap.calculateNewId();

                    $(this).attr("id", "ideasBinding_" + newBindingId);

                    b = true;
                }

                var binding = Binding.getBinding($(this));
                $bindingMap.addBinding(binding);

            });

        if (b) {
            DescriptionInspector.saveInspectorContentToDescriptionFile();
            DescriptionInspector.highlightAllBindings();
        }
    }

};

/**
 * Añade nuevo Binding a la base de datos de mapeo.
 *
 * @memberof BindingMap
 */
BindingMap.prototype.addBinding = function (bindingObj) {

    var rows = bindingObj.getRows();

    // Add to bindingRowsMap
    if (this.bindingRowsMap[bindingObj.id] === undefined) {
        this.bindingRowsMap[bindingObj.id] = BindingMap
            .getBindingRowsStructure();
    }
    this.bindingRowsMap[bindingObj.id]["filas"] = rows;

    // Add to rowBindingsMap
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        if (this.rowBindingsMap[row] === undefined) {
            this.rowBindingsMap[row] = [];
        }
        this.rowBindingsMap[row].push(bindingObj.id);
    }

};

/**
 * Borra un Binding existente en BindingMap.
 *
 * @memberof BindingMap
 */
BindingMap.prototype.removeBinding = function (bindingObj) {

    if (this.existBinding(bindingObj)) {

        var rows = bindingObj.getRows();

        // Remove from bindingRowsMap
        delete this.bindingRowsMap[bindingObj.id];

        // Remove from rowBindingsMap
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            if (this.rowBindingsMap[row] !== undefined) {
                var index = this.rowBindingsMap[row].indexOf(bindingObj.id);
                this.rowBindingsMap[row].splice(index, 1);

                if (this.rowBindingsMap[row].length === 0) {
                    delete this.rowBindingsMap[row];
                }

            }
        }

    }

};

/**
 * Obtiene los Bindings en HTML a partir de una fila.
 *
 * @memberof BindingMap
 */
BindingMap.prototype.getHTMLBindingsFromRow = function (row) {

    if (row in this.rowBindingsMap) {
        var bindingIds = this.rowBindingsMap[row], array = [];
        for (var i = 0; i < bindingIds.length; i++) {
            var e = $("#ideasBinding_" + bindingIds[i]);
            if (e && array.indexOf(e) === -1)
                array.push(e);
        }

        return array != [] ? array : undefined;
    } else {
        console.log("Row not found", row);
    }

};
