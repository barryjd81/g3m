package org.glob3.mobile.generated; 
//
//  HereLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//

//
//  HereLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/7/13.
//
//





public class HereLayer extends Layer
{
  private final Sector _sector ;
  private final String _appId;
  private final String _appCode;


  public HereLayer(String appId, String appCode, TimeInterval timeToCache, int initialLevel)
  {
     this(appId, appCode, timeToCache, initialLevel, null);
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache)
  {
     this(appId, appCode, timeToCache, 2, null);
  }
  public HereLayer(String appId, String appCode, TimeInterval timeToCache, int initialLevel, LayerCondition condition)
  {
     super(condition, "here", timeToCache, new LayerTilesRenderParameters(Sector.fullSphere(), 1, 1, initialLevel, 20, new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), true));
     _appId = appId;
     _appCode = appCode;
     _sector = new Sector(Sector.fullSphere());
  
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final Sector tileSector = tile.getSector();
    if (!_sector.touchesWith(tileSector))
    {
      return petitions;
    }
  
    final Sector sector = tileSector.intersection(_sector);
    if (sector.getDeltaLatitude().isZero() || sector.getDeltaLongitude().isZero())
    {
      return petitions;
    }
  
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("http://m.nok.it/");
  
    isb.addString("?app_id=");
    isb.addString(_appId);
  
    isb.addString("&app_code=");
    isb.addString(_appCode);
  
    isb.addString("&nord");
    isb.addString("&nodot");
  
    isb.addString("&w=");
    isb.addInt(_parameters._tileTextureResolution._x);
  
    isb.addString("&h=");
    isb.addInt(_parameters._tileTextureResolution._y);
  
    isb.addString("&ctr=");
    isb.addDouble(tileSector.getCenter().latitude().degrees());
    isb.addString(",");
    isb.addDouble(tileSector.getCenter().longitude().degrees());
  
  //  isb->addString("&poi=");
  //  isb->addDouble(tileSector.lower().latitude().degrees());
  //  isb->addString(",");
  //  isb->addDouble(tileSector.lower().longitude().degrees());
  //  isb->addString(",");
  //  isb->addDouble(tileSector.upper().latitude().degrees());
  //  isb->addString(",");
  //  isb->addDouble(tileSector.upper().longitude().degrees());
  //  isb->addString("&nomrk");
  
    isb.addString("&z=");
    final int level = tile.getLevel();
    isb.addInt(level);
  
  //  isb->addString("&t=3");
  
    /*
     0 (normal.day)
     Normal map view in day light mode.
  
     1 (satellite.day)
     Satellite map view in day light mode.
  
     2 (terrain.day)
     Terrain map view in day light mode.
  
     3 (hybrid.day)
     Satellite map view with streets in day light mode.
  
     4 (normal.day.transit)
     Normal grey map view with public transit in day light mode.
  
     5 (normal.day.grey)
     Normal grey map view in day light mode (used for background maps).
  
     6 (normal.day.mobile)
     Normal map view for small screen devices in day light mode.
  
     7 (normal.night.mobile)
     Normal map view for small screen devices in night mode.
  
     8 (terrain.day.mobile)
     Terrain map view for small screen devices in day light mode.
  
     9 (hybrid.day.mobile)
     Satellite map view with streets for small screen devices in day light mode.
  
     10 (normal.day.transit.mobile)
     Normal grey map view with public transit for small screen devices in day light mode.
  
     11 (normal.day.grey.mobile)
     12 (carnav.day.grey) Map view designed for navigation devices.
     13 (pedestrian.day) Map view designed for pedestrians walking by day.
     14 (pedestrian.night) Map view designed for pedestrians walking by night.
     Normal grey map view for small screen devices in day light mode (used for background maps).
  
     By default normal map view in day light mode (0) is used for non-mobile clients. For mobile clients the default is normal map view for small screen devices in day light mode (6).
    
  
     */
  
    final String path = isb.getString();
  
    if (isb != null)
       isb.dispose();
  
    petitions.add(new Petition(tileSector, new URL(path, false), _timeToCache, true));
  
    return petitions;
  }

  public final URL getFeatureInfoURL(Geodetic2D position, Sector sector)
  {
    return new URL();
  }


}