//
//  GLFeatureGroup.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/07/13.
//
//

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"

GLFeatureGroupName GLFeatureGroup::getGroupName(int i) {
  switch (i) {
    case 0:
      return NO_GROUP;
    case 1:
      return CAMERA_GROUP;
    case 2:
      return COLOR_GROUP;
    case 3:
      return LIGHTING_GROUP;
    default:
      return UNRECOGNIZED_GROUP;
  }
}

GLFeatureGroup* GLFeatureGroup::createGroup(GLFeatureGroupName name) {
  switch (name) {
    case UNRECOGNIZED_GROUP:
      return NULL;
    case NO_GROUP:
      return new GLFeatureNoGroup();
    case CAMERA_GROUP:
      return new GLFeatureCameraGroup();
    case COLOR_GROUP:
      return new GLFeatureColorGroup();
    case LIGHTING_GROUP:
      return new GLFeatureLightingGroup();
    default:
      return NULL;
  }
}

void GLFeatureNoGroup::applyOnGlobalGLState(GLGlobalState* state) {
  for(int i = 0; i < _nFeatures; i++) {
    const GLFeature* f = _features[i];
    if (f != NULL) {
      f->applyOnGlobalGLState(state);
    }
  }
}

void GLFeatureColorGroup::applyOnGlobalGLState(GLGlobalState* state) {

  int priority = -1;
  for (int i = 0; i < _nFeatures; i++) {
    PriorityGLFeature* f = ((PriorityGLFeature*) _features[i]);
    if (f->getPriority() > priority) {
      priority = f->getPriority();
    }
  }

  for (int i = 0; i < _nFeatures; i++) {
    PriorityGLFeature* f = ((PriorityGLFeature*) _features[i]);
    if (f->getPriority() == priority) {
      f->applyOnGlobalGLState(state);
    }
  }
}

void GLFeatureNoGroup::addToGPUVariableSet(GPUVariableValueSet* vs) {
  for(int i = 0; i < _nFeatures; i++) {
    const GLFeature* f = _features[i];
    if (f != NULL) {
      vs->combineWith(f->getGPUVariableValueSet());
    }
  }
}

void GLFeatureCameraGroup::addToGPUVariableSet(GPUVariableValueSet* vs) {
#ifdef C_CODE
  const Matrix44DHolder** matrixHolders = new const Matrix44DHolder*[_nFeatures];
#endif
#ifdef JAVA_CODE
  final Matrix44DHolder[] matrixHolders = new Matrix44DHolder[_nFeatures];
#endif
  for (int i = 0; i < _nFeatures; i++) {
    GLCameraGroupFeature* f = ((GLCameraGroupFeature*) _features[i]);
    matrixHolders[i] = f->getMatrixHolder();
    if (matrixHolders[i] == NULL) {
      ILogger::instance()->logError("MatrixHolder NULL");
    }
  }

  vs->addUniformValue(MODELVIEW, new GPUUniformValueModelview(matrixHolders, _nFeatures), false);
}

void GLFeatureColorGroup::addToGPUVariableSet(GPUVariableValueSet *vs) {

  int priority = -1;
  for (int i = 0; i < _nFeatures; i++) {
    PriorityGLFeature* f = ((PriorityGLFeature*) _features[i]);
    if (f->getPriority() > priority) {
      priority = f->getPriority();
    }
  }

  for (int i = 0; i < _nFeatures; i++) {
    PriorityGLFeature* f = ((PriorityGLFeature*) _features[i]);
    if (f->getPriority() == priority) {
      priority = f->getPriority();
      vs->combineWith(f->getGPUVariableValueSet());
    }
  }
}


void GLFeatureSet::add(const GLFeature* f) {
  _features[_nFeatures++] = f;
  f->_retain();

  if (_nFeatures >= MAX_CONCURRENT_FEATURES_PER_GROUP) {
    ILogger::instance()->logError("MAX_CONCURRENT_FEATURES_PER_GROUP REACHED");
  }
}

void GLFeatureSet::clearFeatures(GLFeatureGroupName g) {

  for (int i = 0; i < _nFeatures; i++) {
    const GLFeature* f = _features[i];
    if (f->getGroup() == g) {
      f->_release();

      for (int j = i; j < _nFeatures; j++) {
        if (j+1 >= MAX_CONCURRENT_FEATURES_PER_GROUP) {
          _features[j] = NULL;
        } else{
          _features[j] = _features[j+1];
        }
      }
      i--;
      _nFeatures--;
    }
  }
  
}

void GLFeatureSet::add(const GLFeatureSet* fs) {
  const int size = fs->size();
  for (int i = 0; i < size; i++) {
    add(fs->get(i));
  }
}

GLFeatureSet::~GLFeatureSet() {
  for (int i = 0; i < _nFeatures; i++) {
    _features[i]->_release();
  }

  JAVA_POST_DISPOSE
}

void GLFeatureLightingGroup::applyOnGlobalGLState(GLGlobalState* state) {
  
}

void GLFeatureLightingGroup::addToGPUVariableSet(GPUVariableValueSet* vs) {
  
}


