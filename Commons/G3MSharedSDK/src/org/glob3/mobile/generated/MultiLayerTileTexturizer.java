package org.glob3.mobile.generated; 
//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  MultiLayerTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//



//class IGLTextureId;
//class TileTextureBuilder;
//class LayerSet;
//class IDownloader;
//class LeveledTexturedMesh;
//class IFloatBuffer;
//class TileRasterizer;
//class TextureIDReference;
//class G3MEventContext;

public class MultiLayerTileTexturizer extends TileTexturizer
{
  private LeveledTexturedMesh getMesh(Tile tile)
  {
    TileTextureBuilderHolder tileBuilderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
    return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getMesh();
  }

  public MultiLayerTileTexturizer()
  {
  
  }

  public void dispose()
  {
    super.dispose();
  
  }

  public final boolean isReady(G3MRenderContext rc, LayerSet layerSet)
  {
    if (layerSet != null)
    {
      return layerSet.isReady();
    }
    return true;
  }

  public final void initialize(G3MContext context, TilesRenderParameters parameters)
  {
    //  _layerSet->initialize(ic);
  }

  public final Mesh texturize(G3MRenderContext rc, TileTessellator tessellator, TileRasterizer tileRasterizer, LayerTilesRenderParameters layerTilesRenderParameters, LayerSet layerSet, boolean isForcedFullRender, long texturePriority, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
    TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
    if (builderHolder == null)
    {
      builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this, tileRasterizer, rc, layerTilesRenderParameters, layerSet.createTileMapPetitions(rc, tile), rc.getDownloader(), tile, tessellatorMesh, tessellator, texturePriority));
      tile.setTexturizerData(builderHolder);
    }
  
    TileTextureBuilder builder = builderHolder.get();
    if (isForcedFullRender)
    {
      builder.start();
    }
    else
    {
      rc.getFrameTasksExecutor().addPreRenderTask(new TileTextureBuilderStartTask(builder));
    }
  
    tile.setTexturizerDirty(false);
    return builder.getMesh();
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
  
    TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
    if (builderHolder != null)
    {
      TileTextureBuilder builder = builderHolder.get();
      builder.cancel();
      builder.cleanTile();
      builder.cleanMesh();
    }
  //  else {
  //    if (mesh != NULL) {
  //      ILogger::instance()->logInfo("break (point) on me 4\n");
  //    }
  //  }
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
    return false;
  }

  public final void justCreatedTopTile(G3MRenderContext rc, Tile tile, LayerSet layerSet)
  {
  }

  public final void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved)
  {
    if (!textureSolved)
    {
      return;
    }
  
    if (tile.isTextureSolved())
    {
      return;
    }
  
    LeveledTexturedMesh ancestorMesh = getMesh(ancestorTile);
    if (ancestorMesh == null)
    {
      return;
    }
  
    final TextureIDReference glTextureId = ancestorMesh.getTopLevelTextureId();
    if (glTextureId == null)
    {
      return;
    }
  
    LeveledTexturedMesh tileMesh = getMesh(tile);
    if (tileMesh == null)
    {
      return;
    }
  
  //  _texturesHandler->retainGLTextureId(glTextureId);
    final TextureIDReference glTextureIdRetainedCopy = glTextureId.createCopy();
  
    final int level = tile._level - ancestorTile._level;
    if (!tileMesh.setGLTextureIdForLevel(level, glTextureIdRetainedCopy))
    {
  //    _texturesHandler->releaseGLTextureId(glTextureId);
      if (glTextureIdRetainedCopy != null)
         glTextureIdRetainedCopy.dispose();
    }
  }

  public final TextureIDReference getTopLevelTextureIdForTile(Tile tile)
  {
    LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();
  
    return (mesh == null) ? null : mesh.getTopLevelTextureId();
  }

  public final boolean onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile, LayerSet layerSet)
  {
    if (layerSet == null)
    {
      return false;
    }
  
    return layerSet.onTerrainTouchEvent(ec, position, tile);
  }

  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
    TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
    if (builderHolder != null)
    {
      TileTextureBuilder builder = builderHolder.get();
      builder.cancel();
      builder.cleanMesh();
    }
  //  else {
  //    if (mesh != NULL) {
  //      ILogger::instance()->logInfo("break (point) on me 5\n");
  //    }
  //  }
  }

}