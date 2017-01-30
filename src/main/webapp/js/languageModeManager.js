
var DEPRECATED_CONVERTER_URI = "/language/convert";
var DEPRECATED_CHECK_LANGUAGE_URI = "/language/format/$format/checkLanguage";
var DEPRECATED_EXEC_OP_URI = "/language/operation/$opId/execute";

var CONVERTER_URI = "/models/$modelId/syntaxes/$srcSyntaxId/translate/$destSyntaxId";
var CHECK_LANGUAGE_URI = "/models/$modelId/syntaxes/$syntaxId/check";
var EXEC_OP_URI = "/models/$modelId/operations/$operationId";

var setupModels = function (configuration) {
    for (var moduleId in configuration.modules) {
        var uri = configuration.modules[moduleId];
        ModeManager.idUriMap[moduleId] = uri;
        console.log("Setting up model: " + moduleId + " (" + configuration.modules[moduleId] + ")");
        configureModel(uri);
    }
};

var configureModel = function (uri) {
    $.ajax(uri + "/language", {
        "type": "get",
        "success": function (moduleManifest) {
            moduleManifest.apiVersion = 1;
            ModeManager.registerNewModule(uri, moduleManifest);
        },
        "error": function (result) {
            $.ajax(uri, {
                "type": "get",
                "success": function (moduleManifest) {
                    ModeManager.registerNewModule(uri, moduleManifest);
                }
            });
        },
        "async": true,
    });
};

// Global studio configuration:
var studioConfiguration;

// Mode Manager

var ModeManager = {
    idUriMap: {},
    extIdMap: {}, // For performance
    modelMap: {},
    converterMap: {},
    operationsMap: {},
    commandsMap: {},
    formatsBiMap: {},
    inspectorLoadersMap: {},
    setup: function () {

        $.ajax("module/configuration", {
            "type": "get",
            "success": function (configuration) {
                studioConfiguration = configuration;
                setupModels(configuration);
                
                // Filter file extensions
                CONFIG_FILE_EXTENSIONS_TO_FILTER = configuration.extensionsFilter;
                CONFIG_EXTENSIONS_PREFERENCES = configuration.extensionsPreferences;
            },
            "error": function (result) {
                console.error("Could not load app configuration: " + result.statusText);
            },
            "async": true
        });
    },
    registerNewModule: function (uri, moduleManifest) {
        var moduleId = moduleManifest.id;
        var apiVersion = parseInt(moduleManifest.apiVersion);
        if (moduleId in ModeManager.modelMap)
            console.error("Model with ID " + moduleId + " already registered. Ignoring...");
        else {
            if (apiVersion <= 1) {
                ModeManager.idUriMap[moduleId] = uri;
                ModeManager.modelMap[moduleId] = moduleManifest;
                ModeManager.operationsMap[moduleId] = moduleManifest.operations;
                ModeManager.inspectorLoadersMap[moduleId] = eval('(' + moduleManifest.inspectorLoader + ')'); // Eval is necessary as the received type is String.
                ModeManager.extIdMap[moduleManifest.extension] = moduleId;
                ModeManager.converterMap[moduleId] = ModeManager.idUriMap[moduleManifest.id] + DEPRECATED_CONVERTER_URI;

                if (!(moduleId in ModeManager.formatsBiMap))
                    ModeManager.formatsBiMap[moduleId] = {};

                for (var i = 0; i < moduleManifest.formats.length; i++) {
                    var format = moduleManifest.formats[i];
                    ModeManager.formatsBiMap[moduleId][format.format] = format;
                }
            } else {
                for (var key in moduleManifest.models) {
                    var model = moduleManifest.models[key];
                    var modelId = model.id;
                    model.module = moduleManifest.id;
                    model.apiVersion = apiVersion; // Reference to the parent module
                    ModeManager.idUriMap[modelId] = uri;
                    ModeManager.modelMap[modelId] = model;
                    ModeManager.operationsMap[modelId] = model.operations;
                    ModeManager.inspectorLoadersMap[modelId] = function(loader, format) {}; // Required by Binding 1.0
                    ModeManager.extIdMap[model.extension] = modelId;
                    ModeManager.converterMap[modelId] = ModeManager.idUriMap[modelId] + CONVERTER_URI.replace("$modelId", modelId);

                    if (!(modelId in ModeManager.formatsBiMap))
                        ModeManager.formatsBiMap[modelId] = {};

                    for (var i = 0; i < model.syntaxes.length; i++) {
                        var syntax = model.syntaxes[i];
                        ModeManager.formatsBiMap[modelId][syntax.id] = syntax;
                    }
                }
            }

            if (typeof moduleManifest.commands !== 'undefined' && moduleManifest.commands !== null)
                ModeManager.commandsMap[moduleId] = moduleManifest.commands;
        }

    },
    getInspectorLoader: function (modelId) {
        return ModeManager.inspectorLoadersMap[modelId];
    },
    getCheckModelURI: function (fileUri, format) {
        var ext = ModeManager.calculateExtFromFileUri(fileUri);

        // TODO
        if (ext == undefined)
            ext = "txt";

        var modelId = ModeManager.calculateModelIdFromExt(ext);

//		return ModeManager.formatsBiMap[ext][format];
        return ModeManager.formatsBiMap[modelId][format];
    },
    getMode: function (modelId) {
        return ModeManager.modelMap[modelId];
    },
    getModes: function () {
        return ModeManager.modelMap;
    },
    getConverter: function (modelId) {
        return ModeManager.converterMap[modelId];
    },
    getOperations: function (modelId) {
        return ModeManager.operationsMap[modelId];
    },
    getCommands: function (modelId) {
        return ModeManager.commandsMap[modelId];
    },
    getBaseUri: function (modelId) {
        return ModeManager.idUriMap[modelId];
    },
    getExtension: function (modelId, optionalArgForExt) {		// TODO: optionalArgForExt is for the future when regexp in exensions are supported
        var ext = "";

        for (potentialExt in ModeManager.extIdMap) {
            if (ModeManager.extIdMap[potentialExt] == modelId) {
                ext = "." + potentialExt;
                break;
            }

        }

        return ext;
    },
    createSessionAggregationForEditor: function (uri, content) {

        var ext = ModeManager.calculateExtFromFileUri(uri);

        if (ext === undefined)
            ext = "txt"; 	// Default

        var modelId = ModeManager.calculateModelIdFromExt(ext);

        if (modelId in ModeManager.modelMap) {
            var sessionAg = new SessionAggregation();
            var model = ModeManager.modelMap[modelId];
            if (model.apiVersion <= 1) {
                var formats = model.formats;
                for (var i = 0; i < formats.length; i++) {
                    var f = formats[i];
                    if (i == 0) {
                        sessionAg.setCurrentFormat(f.format);
                        sessionAg.setBaseSession(f.format);
                        sessionAg.setFormatSession(f.format, ace.createEditSession(content, f.editorModeId));
                    } else
                        sessionAg.setFormatSession(f.format, ace.createEditSession("", f.editorModeId));

                    if (eval(f.checkLanguage))
                        sessionAg.setCheckLanguageURI(f.format, ModeManager.idUriMap[modelId] + DEPRECATED_CHECK_LANGUAGE_URI.replace("$format", f.format));
                }
            } else {
                var syntaxes = model.syntaxes;
                for (var i = 0; i < syntaxes.length; i++) {
                    var s = syntaxes[i];
                    if (i === 0) {
                        sessionAg.setCurrentFormat(s.id);
                        sessionAg.setBaseSession(s.id);
                        sessionAg.setFormatSession(s.id, ace.createEditSession(content, s.editorModeId));
                    } else
                        sessionAg.setFormatSession(s.id, ace.createEditSession("", s.editorModeId));

                    if (eval(s.syntaxCheck))
                        sessionAg.setCheckLanguageURI(s.id, ModeManager.idUriMap[modelId] + CHECK_LANGUAGE_URI.replace("$modelId", model.id)
                                .replace("$syntaxId", s.id));
                }
            }

            return sessionAg;

        } else {
            console.error("No mode defined for " + ext + "!");
        }

    },
    // Utils

    calculateModelIdFromExt: function (ext) {
        return ModeManager.extIdMap[ext];
    },
    calculateExtFromFileUri: function (fileUri) {
        var regexp = /(?:\.([^.]+))?$/;	// For getting the extension
        return regexp.exec(fileUri)[1];
    }
};


