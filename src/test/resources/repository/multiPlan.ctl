$scope.createNewModel = function () {
    var constraintName = "";
    var i = 1;
    var const_i = "";
    
    if ( getCreationConstraints() ) {
        
        while (constraintName === "") {
            const_i = "C"+i;
            if ( !(const_i in getCreationConstraints()) ) {
                constraintName += const_i;
                break;
            }
            i++;
        }

        if (constraintName !== "") {
            getCreationConstraints()[const_i] = creationConstraintStructureWithId(const_i);
        }
        
    } else {
        console.log("Please, define a creationConstraint in the model");
    }
    
};

var getCreationConstraints = function () {
    return $scope.model.creationConstraints;
};

$scope.removeModel = function (modelId, $event) {
    delete getCreationConstraints()[modelId];
};

var creationConstraintStructureWithId = function (constraintId) {
    return {
        "slo": {
            "expression": {
                "_type": "ParenthesisExpression",
                "properties": {
                    "exp": {
                        "_type": "LogicalExpression",
                        "properties": {
                            "exp1": {
                                "_type": "LogicalExpression",
                                "properties": {
                                    "exp1": {
                                        "_type": "RelationalExpression",
                                        "properties": {
                                            "exp1": {
                                                "_type": "Var",
                                                "properties": {
                                                    "id": "MaxRequests"
                                                }
                                            },
                                            "exp2": {
                                                "_type": "Atomic",
                                                "properties": {
                                                    "value": "1000"
                                                }
                                            },
                                            "operator": "EQ"
                                        }
                                    },
                                    "exp2": {
                                        "_type": "RelationalExpression",
                                        "properties": {
                                            "exp1": {
                                                "_type": "Var",
                                                "properties": {
                                                    "id": "MaxResponseTime"
                                                }
                                            },
                                            "exp2": {
                                                "_type": "Atomic",
                                                "properties": {
                                                    "value": "100"
                                                }
                                            },
                                            "operator": "EQ"
                                        }
                                    },
                                    "operator": "AND"
                                }
                            },
                            "exp2": {
                                "_type": "RelationalExpression",
                                "properties": {
                                    "exp1": {
                                        "_type": "Var",
                                        "properties": {
                                            "id": "PlanPrice"
                                        }
                                    },
                                    "exp2": {
                                        "_type": "Atomic",
                                        "properties": {
                                            "value": "10"
                                        }
                                    },
                                    "operator": "EQ"
                                }
                            },
                            "operator": "AND"
                        }
                    }
                }
            }
        },
        "qc": {
            "condition": {
                "_type": "RelationalExpression",
                "properties": {
                    "exp1": {
                        "_type": "Var",
                        "properties": {
                            "id": "PlanType"
                        }
                    },
                    "exp2": {
                        "_type": "Atomic",
                        "properties": {
                            "value": "pro"
                        }
                    },
                    "operator": "EQ"
                }
            }
        },
        "id": constraintId
    };
};
