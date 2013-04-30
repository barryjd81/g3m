//
//  FloatBufferBuilderFromCartesian3D.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_FloatBufferBuilderFromCartesian3D_hpp
#define G3MiOSSDK_FloatBufferBuilderFromCartesian3D_hpp

#include "Vector3D.hpp"
#include "FloatBufferBuilder.hpp"

class FloatBufferBuilderFromCartesian3D: public FloatBufferBuilder {
private:
  const int _centerStrategy;
  float _cx;
  float _cy;
  float _cz;

  void setCenter(double x, double y, double z){
    _cx = (float) x;
    _cy = (float) y;
    _cz = (float) z;
  }

public:

  FloatBufferBuilderFromCartesian3D(int centerStrategy,
                                    const Vector3D& center):
  _centerStrategy(centerStrategy)
  {
    setCenter(center._x, center._y, center._z);
  }

  void add(const Vector3D& vector) {
    add(vector._x,
        vector._y,
        vector._z);
  }

  void add(double x, double y, double z) {
    if (_centerStrategy == CenterStrategy::firstVertex()) {
      if (_values.size() == 0) {
        setCenter(x, y, z);
      }
    }

    if (_centerStrategy == CenterStrategy::noCenter()) {
      _values.push_back( (float) x );
      _values.push_back( (float) y );
      _values.push_back( (float) z );
    }
    else {
      _values.push_back( (float) (x - _cx) );
      _values.push_back( (float) (y - _cy) );
      _values.push_back( (float) (z - _cz) );
    }
  }

  void add(float x, float y, float z) {
    if (_centerStrategy == CenterStrategy::firstVertex()) {
      if (_values.size() == 0) {
        setCenter(x, y, z);
      }
    }

    if (_centerStrategy == CenterStrategy::noCenter()) {
      _values.push_back( x );
      _values.push_back( y );
      _values.push_back( z );
    }
    else {
      _values.push_back( x - _cx );
      _values.push_back( y - _cy );
      _values.push_back( z - _cz );
    }
  }

  Vector3D getCenter(){
    return Vector3D(_cx, _cy, _cz);
  }
  
};

#endif
