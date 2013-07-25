//
//  GEO2DPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#include "GEO2DPolygonGeometry.hpp"

#include "GEOSymbolizationContext.hpp"
#include "GEOSymbolizer.hpp"
#include "GEOSymbolizationContext.hpp"
#include "Geodetic2D.hpp"

GEO2DPolygonGeometry::~GEO2DPolygonGeometry() {
  const int coordinatesCount = _coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    Geodetic2D* coordinate = _coordinates->at(i);
    delete coordinate;
  }
  delete _coordinates;
}


std::vector<GEOSymbol*>* GEO2DPolygonGeometry::createSymbols(const G3MRenderContext* rc,
                                                             const GEOSymbolizationContext& sc) const {
  return sc.getSymbolizer()->createSymbols(this);
}