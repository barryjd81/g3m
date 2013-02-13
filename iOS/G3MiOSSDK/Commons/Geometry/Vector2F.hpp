//
//  Vector2F.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/9/13.
//
//

#ifndef __G3MiOSSDK__Vector2F__
#define __G3MiOSSDK__Vector2F__

#include "IMathUtils.hpp"

class Vector2F {
private:


  Vector2F& operator=(const Vector2F& v);

public:
  const float _x;
  const float _y;


  Vector2F(const float x,
           const float y): _x(x), _y(y) {

  }

  Vector2F(const Vector2F &v): _x(v._x), _y(v._y) {

  }

  float x() const {
    return _x;
  }

  float y() const {
    return _y;
  }

  static Vector2F nan() {
    return Vector2F(IMathUtils::instance()->NanF(), IMathUtils::instance()->NanF());
  }

};

#endif