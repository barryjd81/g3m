package org.glob3.mobile.generated; 
//
//  PlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  PlanetRenderer.h
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class Tile;
//class TileTessellator;
//class TileTexturizer;
//class LayerSet;
//class VisibleSectorListenerEntry;
//class VisibleSectorListener;
//class ElevationDataProvider;
//class LayerTilesRenderParameters;
//class TerrainTouchListener;


//class EllipsoidShape;
//class TileRasterizer;


public class TilesStatistics
{
  private int _tilesProcessed;
  private int _tilesVisible;
  private int _tilesRendered;

  private static final int _maxLOD = 128;

  private int[] _tilesProcessedByLevel = new int[_maxLOD];
  private int[] _tilesVisibleByLevel = new int[_maxLOD];
  private int[] _tilesRenderedByLevel = new int[_maxLOD];

  private int _splitsCountInFrame;
  private int _buildersStartsInFrame;

  private Sector _renderedSector;


  public TilesStatistics()
  {
     _tilesProcessed = 0;
     _tilesVisible = 0;
     _tilesRendered = 0;
     _splitsCountInFrame = 0;
     _buildersStartsInFrame = 0;
     _renderedSector = null;
    for (int i = 0; i < _maxLOD; i++)
    {
      _tilesProcessedByLevel[i] = _tilesVisibleByLevel[i] = _tilesRenderedByLevel[i] = 0;
    }
  }

  public void dispose()
  {
    //    if (_buildersStartsInFrame > 0) {
    //      printf("buildersStartsInFrame=%d\n", _buildersStartsInFrame);
    //    }
    if (_renderedSector != null)
       _renderedSector.dispose();
  }

  public final void clear()
  {
    _tilesProcessed = 0;
    _tilesVisible = 0;
    _tilesRendered = 0;
    _splitsCountInFrame = 0;
    _buildersStartsInFrame = 0;
    if (_renderedSector != null)
       _renderedSector.dispose();
    _renderedSector = null;
    for (int i = 0; i < _maxLOD; i++)
    {
      _tilesProcessedByLevel[i] = _tilesVisibleByLevel[i] = _tilesRenderedByLevel[i] = 0;
    }
  }

  public final int getSplitsCountInFrame()
  {
    return _splitsCountInFrame;
  }

  public final void computeSplitInFrame()
  {
    _splitsCountInFrame++;
  }

  public final int getBuildersStartsInFrame()
  {
    return _buildersStartsInFrame;
  }

  public final void computeBuilderStartInFrame()
  {
    _buildersStartsInFrame++;
  }

  public final void computeTileProcessed(Tile tile)
  {
    _tilesProcessed++;

    final int level = tile._level;
    _tilesProcessedByLevel[level] = _tilesProcessedByLevel[level] + 1;
  }

  public final void computeVisibleTile(Tile tile)
  {
    _tilesVisible++;

    final int level = tile._level;
    _tilesVisibleByLevel[level] = _tilesVisibleByLevel[level] + 1;
  }

  public final void computeRenderedSector(Tile tile)
  {
    final Sector sector = tile._sector;
    if (_renderedSector == null)
    {
      _renderedSector = sector;
    }
    else
    {
      if (!_renderedSector.fullContains(sector))
      {
        Sector previous = _renderedSector;

        _renderedSector = _renderedSector.mergedWith(sector);

        if (previous != null)
           previous.dispose();
      }
    }
  }

  public final void computePlanetRenderered(Tile tile)
  {
    _tilesRendered++;

    final int level = tile._level;
    _tilesRenderedByLevel[level] = _tilesRenderedByLevel[level] + 1;


    computeRenderedSector(tile);
  }

  public final Sector getRenderedSector()
  {
    return _renderedSector;
  }

  //  bool equalsTo(const TilesStatistics& that) const {
  //    if (_tilesProcessed != that._tilesProcessed) {
  //      return false;
  //    }
  //    if (_tilesRendered != that._tilesRendered) {
  //      return false;
  //    }
  //    if (_tilesRenderedByLevel != that._tilesRenderedByLevel) {
  //      return false;
  //    }
  //    if (_tilesProcessedByLevel != that._tilesProcessedByLevel) {
  //      return false;
  //    }
  //    return true;
  //  }


  public static String asLogString(int[] m, int nMax)
  {

    boolean first = true;
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    for(int i = 0; i < nMax; i++)
    {
      final int level = i;
      final int counter = m[i];
      if (counter != 0)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          isb.addString(",");
        }
        isb.addString("L");
        isb.addInt(level);
        isb.addString(":");
        isb.addInt(counter);
      }
    }

    String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final void log(ILogger logger)
  {
    logger.logInfo("Tiles processed:%d (%s), visible:%d (%s), rendered:%d (%s).", _tilesProcessed, asLogString(_tilesProcessedByLevel, _maxLOD), _tilesVisible, asLogString(_tilesVisibleByLevel, _maxLOD), _tilesRendered, asLogString(_tilesRenderedByLevel, _maxLOD));
//    logger->logInfo("Tiles processed:%d, visible:%d, rendered:%d.",
//                    _tilesProcessed,
//                    _tilesVisible,
//                    _tilesRendered);
  }

}