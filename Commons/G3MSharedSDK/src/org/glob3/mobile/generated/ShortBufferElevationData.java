package org.glob3.mobile.generated; 
//
//  ShortBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

//
//  ShortBufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//


//class IShortBuffer;

public class ShortBufferElevationData extends BufferElevationData
{
  private IShortBuffer _buffer;

  protected final double getValueInBufferAt(int index)
  {
    return _buffer.get(index);
  }

  public ShortBufferElevationData(Sector sector, Vector2I resolution, double noDataValue, IShortBuffer buffer)
  {
     super(sector, resolution, noDataValue, buffer.size());
     _buffer = buffer;
    if (_buffer.size() != (_width * _height))
    {
      ILogger.instance().logError("Invalid buffer size");
    }
  }

  public void dispose()
  {
    if (_buffer != null)
       _buffer.dispose();
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(ShortBufferElevationData extent=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(" sector=");
    isb.addString(_sector.description());
    int unusedType = -1;
    if (detailed)
    {
      isb.addString("\n");
      for (int row = 0; row < _width; row++)
      {
        //isb->addString("   ");
        for (int col = 0; col < _height; col++)
        {
          isb.addDouble(getElevationAt(col, row, unusedType));
          isb.addString(",");
        }
        isb.addString("\n");
      }
    }
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final Vector3D getMinMaxAverageHeights()
  {
    final IMathUtils mu = IMathUtils.instance();
    short minHeight = mu.maxInt16();
    short maxHeight = mu.minInt16();
    double sumHeight = 0.0;
  
    final int bufferSize = _buffer.size();
    for (int i = 0; i < bufferSize; i++)
    {
      final short height = _buffer.get(i);
  //    if (height != _noDataValue) {
      if (height < minHeight)
      {
        minHeight = height;
      }
      if (height > maxHeight)
      {
        maxHeight = height;
      }
      sumHeight += height;
  //    }
    }
  
    if (minHeight == mu.maxInt16())
    {
      minHeight = 0;
    }
    if (maxHeight == mu.minInt16())
    {
      maxHeight = 0;
    }
  
    return new Vector3D(minHeight, maxHeight, sumHeight / (_width * _height));
  }

}