
var CONVERTER_URI = "/language/convert";
var CHECK_LANGUAGE_URI = "/language/format/$format/checkLanguage";
var EXEC_OP_URI = "/language/operation/$opId/execute";

var setupLanguages = function (configuration) {
    for (var moduleId in configuration.modules) {
        var uri = configuration.modules[moduleId];
        ModeManager.idUriMap[moduleId] = uri;
        configureModule(moduleId, uri);
    }
};

var configureModule = function (moduleId, uri) {
    $.ajax(uri + "/language", {
        "type": "GET",
        "success": function (languageManifest) {
            console.log("Setting up module: " + moduleId);
            ModeManager.registerNewMode(uri, languageManifest);
            // TODO Cargar lenguajes aquí (quitar el SEDLSetup) y rellenar "ModeManager.idUriMap"!!
        },
        "error": function (result) {
            console.error("Could not load module from " + uri + ": " + result.statusText);
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
    languageModeMap: {},
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
                setupLanguages(configuration);
            },
            "error": function (result) {
                console.error("Could not load app configuration: " + result.statusText);
            },
            "async": true,
        });
    },
    registerNewMode: function (uri, languageMode) {

        var languageId = languageMode.id;
        if (languageId in ModeManager.languageModeMap)
            console.error("Language with ID " + languageId + " already registered. Ignoring...");
        else {
            ModeManager.idUriMap[languageId] = uri;
            ModeManager.languageModeMap[languageId] = languageMode;
            ModeManager.operationsMap[languageId] = languageMode.operations;
            ModeManager.inspectorLoadersMap[languageId] = eval('(' + languageMode.inspectorLoader + ')');				// Eval is necessary as the received type is String.
            ModeManager.extIdMap[languageMode.extension] = languageId;
            ModeManager.converterMap[languageId] = ModeManager.idUriMap[languageMode.id] + CONVERTER_URI;

            if (!(languageId in ModeManager.formatsBiMap))
                ModeManager.formatsBiMap[languageId] = {};

            if (typeof languageMode.commands !== 'undefined' && languageMode.commands !== null)
                ModeManager.commandsMap[languageId] = languageMode.commands;



            for (var i = 0; i < languageMode.formats.length; i++) {
                var format = languageMode.formats[i];
                ModeManager.formatsBiMap[languageId][format.format] = format;
            }

        }

    },
    getInspectorLoader: function (languageId) {
        return ModeManager.inspectorLoadersMap[languageId];
    },
    getCheckLanguageURI: function (fileUri, format) {
        var ext = ModeManager.calculateExtFromFileUri(fileUri);

        // TODO
        if (ext == undefined)
            ext = "txt";

        var languageId = ModeManager.calculateLanguageIdFromExt(ext);

//		return ModeManager.formatsBiMap[ext][format];
        return ModeManager.formatsBiMap[languageId][format];
    },
    getMode: function (languageId) {
        return ModeManager.languageModeMap[languageId];
    },
    getModes: function () {
        return ModeManager.languageModeMap;
    },
    getConverter: function (languageId) {
        return ModeManager.converterMap[languageId];
    },
    getOperations: function (languageId) {
        return ModeManager.operationsMap[languageId];
    },
    getCommands: function (languageId) {
        return ModeManager.commandsMap[languageId];
    },
    getBaseUri: function (languageId) {
        return ModeManager.idUriMap[languageId];
    },
    getExtension: function (languageId, optionalArgForExt) {		// TODO: optionalArgForExt is for the future when regexp in exensions are supported
        var ext = "";

        for (potentialExt in ModeManager.extIdMap) {
            if (ModeManager.extIdMap[potentialExt] == languageId) {
                ext = "." + potentialExt;
                break;
            }

        }

        return ext;
    },
    createSessionAggregationForEditor: function (uri, content) {

        var ext = ModeManager.calculateExtFromFileUri(uri);

        if (ext == undefined)
            ext = "txt"; 	// Default

        var languageId = ModeManager.calculateLanguageIdFromExt(ext);

        if (languageId in ModeManager.languageModeMap) {
            var sessionAg = new SessionAggregation();
            var allFormats = ModeManager.languageModeMap[languageId].formats;
            for (var i = 0; i < allFormats.length; i++) {
                var f = allFormats[i];
                if (i == 0) {
                    sessionAg.setCurrentFormat(f.format);
                    sessionAg.setBaseSession(f.format);
                    sessionAg.setFormatSession(f.format, ace.createEditSession(content, f.editorModeId));
                } else
                    sessionAg.setFormatSession(f.format, ace.createEditSession("", f.editorModeId));

                if (eval(f.checkLanguage))
                    sessionAg.setCheckLanguageURI(f.format, ModeManager.idUriMap[languageId] + CHECK_LANGUAGE_URI.replace("$format", f.format));
            }

            return sessionAg;

        } else {
            console.error("No mode defined for " + ext + "!");
        }

    },
    // Utils

    calculateLanguageIdFromExt: function (ext) {
        return ModeManager.extIdMap[ext];
    },
    calculateExtFromFileUri: function (fileUri) {
        var regexp = /(?:\.([^.]+))?$/;	// For getting the extension
        return regexp.exec(fileUri)[1];
    }
};


