export let featureConfig = {
  "focus": "multi",
  "toOmitCompletely": {
    "type": "all",
    "include": false,
    "implemented": true
  },
  "mainPage": {
    "type": "all",
    "include": true,
    "implemented": true
  },
  "mainNavigation": {
    "type": "all",
    "include": true,
    "implemented": true
  },
  "publishedDesigns": {
    "type": "design3DCover",
    "include": false,
    "implemented": false
  },
  "gameStatistics": {
    "type": "puzzleToPlay",
    "include": false,
    "implemented": false
  },
  "environmentModule": {
    "focus": "multi",
    "puzzleAlgorithmType": {
      "type": "puzzleToPlay",
      "include": true,
      "data": {
        "strategy": "jigsaw",
      },
      "includeOptions": ["JIGSAW", "ANTI-JIGSAW"],
      "availableOptions": ["JIGSAW", "ANTI-JIGSAW", "JIGSAW2"],
      "implemented": true
    },
    "imageLoader": {
      "type": "all",
      "include": false,
      "implemented": true
    },
    "imageGallery": {
      "type": "all",
      "include": false,
      "data": {
        "url": ""
      },
      "implemented": true
    },
    "navigation": {
      "type": "all",
      "include": false,
      "implemented": false
    },
    "modelLoader": {
      "type": "design3DCover",
      "include": false,
      "implemented": false
    },
    "textureLoader": {
      "type": "design3DCover",
      "include": false,
      "implemented": false
    },
    "designGallery": {
      "type": "design3DCover",
      "include": false,
      "implemented": false
    },
    "applicationCore": {
      "focus": "multi",
      "designBoard": {
        "focus": "multi-value",
        "type": "all",
        "include": true,
        "implemented": true,
        "zoomManagement": {
          "focus": "multi",
          "zoom": {
            "type": "all",
            "include": true,
            "implemented": true,
          },
          "zoomValue": {
            "type": "all",
            "include": true,
            "implemented": true,
            "enebaleReset": true,
            "enableSettingValue": true,
            "enableChoosingZoomPoint": true
          },
          "zoomCoordinates": {
            "type": "all",
            "include": false,
            "implemented": true,
            "enebaleReset": true,
            "enableSettingValue": true,
            "enableChoosingZoomPoint": true
          }
        },
        "borderingBoardAreas": {
          "type": "all",
          "include": true,
          "implemented": true
        }
      },
      "shuffleItems": {
        "type": "puzzleToPlay",
        "include": true,
        "implemented": true,
        "shuffleByRandomAngle": true,
        "changeOrderOfItems": true
      },
      "appNavigation": {
        "type": "all",
        "include": false,
        "implemented": false
      },
      "designTo3DMapper": {
        "type": "design3DCover",
        "include": true,
        "implemented": true
      },
      "preview3D": {
        "focus": "multi-value",
        "type": "design3DCover",
        "include": true,
        "implemented": true,
        "scaleItem": {
          "type": "design3DCover",
          "include": true,
          "implemented": true
        },
        "rotateItem": {
          "type": "design3DCover",
          "include": true,
          "implemented": true
        },
        "translateItem": {
          "type": "design3DCover",
          "include": true,
          "implemented": true
        },
        "3Dzoom": {
          "type": "design3DCover",
          "include": true,
          "implemented": true,
          "enebaleReset": true,
          "enableSettingValue": true,
          "enableChoosingZoomPoint": true
        }
      },
      "item": {
        "focus": "multi",
        "type": "all",
        "include": true,
        "implemented": true,
        "canMoveOnBoard": true,
        "selectable": true,
        "data": {
          "type": "square",
          "possibleTypes3DCover": "square|circle|text|polygon|triangle|image",
          "possibleTypesPuzzle": "puzzle",
        },
        "controls": {
          "focus": "multi",
          "deleteItem": {
            "type": "design3DCover",
            "include": true,
            "implemented": true
          },
          "cloneItem": {
            "type": "design3DCover",
            "include": true,
            "implemented": true
          },
          "bevelItem": {
            "type": "design3DCover",
            "include": true,
            "implemented": true
          },
          "scaleItem": {
            "type": "design3DCover",
            "include": true,
            "implemented": true
          },
          "showPositionOn3D": {
            "type": "design3DCover",
            "include": true,
            "implemented": true
          },
          "toFront": {
            "type": "all",
            "include": true,
            "implemented": true
          },
          "toBack": {
            "type": "all",
            "include": true,
            "implemented": true
          },
          "rotateItem": {
            "type": "all",
            "include": true,
            "implemented": true
          },
          "returnItem": {
            "type": "puzzleToPlay",
            "include": true,
            "implemented": true
          },
          "showPositionOnBoard": {
            "type": "puzzleToPlay",
            "include": true,
            "implemented": true
          }
        }
      }
    }
  }
}
