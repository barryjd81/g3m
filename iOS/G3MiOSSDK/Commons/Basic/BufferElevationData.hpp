//
//  BufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

#ifndef __G3MiOSSDK__BufferElevationData__
#define __G3MiOSSDK__BufferElevationData__

#include "ElevationData.hpp"
class Interpolator;

class BufferElevationData : public ElevationData {
private:
  mutable Interpolator*  _interpolator;

  const int _bufferSize;
protected:
  Interpolator*  getInterpolator() const;

  virtual double getValueInBufferAt(int index) const = 0;

public:
  BufferElevationData(const Sector& sector,
                      const Vector2I& resolution,
                      double noDataValue,
                      int bufferSize);

  virtual ~BufferElevationData();

  double getElevationAt(const Angle& latitude,
                        const Angle& longitude) const;

  virtual double getElevationAt(int x, int y) const;

};

#endif