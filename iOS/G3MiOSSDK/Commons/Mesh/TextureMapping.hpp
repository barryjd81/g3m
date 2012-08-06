//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

#include "MutableVector2D.hpp"
#include <vector>

#include "TexturesHandler.hpp"

class RenderContext;


class TextureMapping
{
private:
  const int          _textureId;
  const float const* _texCoords;
  MutableVector2D    _translation, _scale;
  TexturesHandler* const _texHandler;
  
public:
  
  TextureMapping(int textureId,
                 float texCoords[], TexturesHandler* const texHandler) :
  _textureId(textureId),
  _texCoords(texCoords),
  _texHandler(texHandler)
  {
    _translation = MutableVector2D(0, 0);
    _scale       = MutableVector2D(1, 1);
  }
  
  TextureMapping(int textureId,
                 std::vector<MutableVector2D> texCoords, TexturesHandler* const texHandler);
  
  void setTranslationAndScale(const Vector2D& translation,
                              const Vector2D& scale){
    _translation = translation.asMutableVector2D();
    _scale       = scale.asMutableVector2D();
  }
  
  ~TextureMapping() {
#ifdef C_CODE
    delete[] _texCoords;
#endif
    
    if (_texHandler != NULL){
      _texHandler->takeTexture(_textureId);
    }
  }
  
  int getTextureId() const {
    return _textureId;
  }
  
  const float* getTexCoords() const {
    return _texCoords;
  }
  
  void bind(const RenderContext* rc) const;  
};

#endif